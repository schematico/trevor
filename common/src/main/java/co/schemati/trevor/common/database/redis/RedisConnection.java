package co.schemati.trevor.common.database.redis;

import com.google.common.collect.ImmutableList;
import redis.clients.jedis.Jedis;
import co.schemati.trevor.api.util.Keys;
import co.schemati.trevor.api.data.User;
import co.schemati.trevor.api.database.DatabaseConnection;
import co.schemati.trevor.api.network.payload.DisconnectPayload;
import co.schemati.trevor.api.instance.InstanceData;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class RedisConnection implements DatabaseConnection {

  private final String instance;
  private final Jedis connection;

  public RedisConnection(String instance, Jedis connection) {
    this.instance = instance;
    this.connection = connection;
  }

  public Jedis getConnection() {
    return connection;
  }

  @Override
  public void beat() {
    long timestamp = System.currentTimeMillis();

    connection.hset(Keys.DATABASE_HEARTBEAT.of(), instance, String.valueOf(timestamp));
  }

  @Override
  public void update(InstanceData data) {
    long timestamp = System.currentTimeMillis();

    ImmutableList.Builder<String> builder = ImmutableList.builder();
    int playerCount = 0;

    Map<String, String> heartbeats = connection.hgetAll(Keys.DATABASE_HEARTBEAT.of());
    for (Map.Entry<String, String> entry : heartbeats.entrySet()) {
      long lastBeat = Long.parseLong(entry.getValue());
      if (timestamp <= lastBeat + (30 * 1000)) { // 30 seconds
        builder.add(entry.getKey());

        playerCount += connection.scard(Keys.INSTANCE_PLAYERS.with(entry.getKey()));
      } else {
        // TODO: Potentially notify that the instance could be dead.
      }
    }

    data.update(builder.build(), playerCount);
  }

  @Override
  public boolean create(User user) {
    if (connection.sismember(Keys.INSTANCE_PLAYERS.with(instance), user.uuid().toString())) {
      return false;
    }

    connection.hmset(Keys.PLAYER_DATA.with(user), user.toDatabaseMap(instance));
    connection.sadd(Keys.INSTANCE_PLAYERS.with(instance), user.uuid().toString());

    return true;
  }

  @Override
  public DisconnectPayload destroy(UUID uuid) {
    long timestamp = System.currentTimeMillis();
    if (connection.sismember(Keys.INSTANCE_PLAYERS.with(instance), uuid.toString())) {
      connection.srem(Keys.INSTANCE_PLAYERS.with(instance), uuid.toString());
      connection.hdel(Keys.PLAYER_DATA.with(uuid.toString()), "server", "ip", "instance");
      connection.hset(Keys.PLAYER_DATA.with(uuid.toString()), "lastOnline",
              String.valueOf(timestamp));
    }

    return DisconnectPayload.of(instance, uuid, timestamp);
  }

  @Override
  public void setServer(User user, String server) {
    connection.hset(Keys.PLAYER_DATA.with(user), "server", server);
  }

  @Override
  public boolean isOnline(User user) {
    return connection.sismember(Keys.INSTANCE_PLAYERS.with(instance), user.uuid().toString());
  }

  @Override
  public boolean isInstanceAlive() {
    long timestamp = System.currentTimeMillis();
    if (connection.hexists(Keys.DATABASE_HEARTBEAT.of(), instance)) {
      long lastBeat = Long.parseLong(connection.hget(Keys.DATABASE_HEARTBEAT.of(), instance));
      return timestamp >= lastBeat + (20 * 1000); // 20 seconds in terms of milliseconds
    }
    return false;
  }

  @Override
  public Set<String> getNetworkPlayers() {
    return connection.smembers(Keys.INSTANCE_PLAYERS.with(instance));
  }

  @Override
  public long getNetworkPlayerCount() {
    return connection.scard(Keys.INSTANCE_PLAYERS.with(instance));
  }

  @Override
  public void deleteHeartbeat() {
    connection.hdel("heartbeat", instance);
  }

  @Override
  public void publish(String channel, String message) {
    connection.publish(channel, message);
  }

  @Override
  public void close() {
    connection.close();
  }
}
