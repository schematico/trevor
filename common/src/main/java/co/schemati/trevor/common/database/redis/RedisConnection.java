package co.schemati.trevor.common.database.redis;

import co.schemati.trevor.api.data.User;
import co.schemati.trevor.api.database.DatabaseConnection;
import co.schemati.trevor.api.instance.InstanceData;
import co.schemati.trevor.api.network.payload.DisconnectPayload;
import com.google.common.collect.ImmutableList;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanResult;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static co.schemati.trevor.api.util.Strings.replace;
import static co.schemati.trevor.common.database.redis.RedisDatabase.HEARTBEAT;
import static co.schemati.trevor.common.database.redis.RedisDatabase.INSTANCE_PLAYERS;
import static co.schemati.trevor.common.database.redis.RedisDatabase.PLAYER_DATA;
import static co.schemati.trevor.common.database.redis.RedisDatabase.SERVER_PLAYERS;

public class RedisConnection implements DatabaseConnection {

  private final String instance;
  private final Jedis connection;

  public RedisConnection(String instance, Jedis connection) {
    this.instance = instance;
    this.connection = connection;
  }

  @Override
  public void beat() {
    long timestamp = System.currentTimeMillis();

    connection.hset(HEARTBEAT, instance, String.valueOf(timestamp));
  }

  @Override
  public void update(InstanceData data) {
    long timestamp = System.currentTimeMillis();

    ImmutableList.Builder<String> builder = ImmutableList.builder();
    int playerCount = 0;

    Map<String, String> heartbeats = connection.hgetAll(HEARTBEAT);
    for (Map.Entry<String, String> entry : heartbeats.entrySet()) {
      long lastBeat = Long.parseLong(entry.getValue());
      if (timestamp <= lastBeat + (30 * 1000)) { // 30 seconds
        builder.add(entry.getKey());

        playerCount += connection.scard(replace(INSTANCE_PLAYERS, entry));
      } else {
        // TODO: Potentially notify that the instance could be dead.
      }
    }

    data.update(builder.build(), playerCount);
  }

  @Override
  public boolean create(User user) {
    if (isOnline(user)) {
      return false;
    }

    connection.hmset(replace(PLAYER_DATA, user), user.toDatabaseMap(instance));
    connection.sadd(replace(INSTANCE_PLAYERS, user));

    return true;
  }

  @Override
  public DisconnectPayload destroy(UUID uuid) {
    long timestamp = System.currentTimeMillis();

    setServer(uuid, null);

    connection.srem(replace(INSTANCE_PLAYERS, uuid));
    connection.hdel(replace(PLAYER_DATA, uuid), "server", "ip", "instance");
    connection.hset(replace(PLAYER_DATA, uuid), "lastOnline", String.valueOf(timestamp));

    return DisconnectPayload.of(instance, uuid, timestamp);
  }

  @Override
  public void setServer(UUID uuid, @Nullable String server) {
    String user = uuid.toString();
    String previous = connection.hget(replace(PLAYER_DATA, user), "server");
    if (previous != null) {
      connection.srem(replace(SERVER_PLAYERS, previous), user);
    }

    if (server != null) {
      connection.sadd(replace(SERVER_PLAYERS, server), user);
      connection.hset(replace(PLAYER_DATA, user), "server", server);
    }
  }

  @Override
  public void setServer(User user, @Nullable String server) {
    setServer(user.uuid(), server);
  }

  @Override
  public boolean isOnline(User user) {
    return connection.exists(replace(PLAYER_DATA, user));
  }

  @Override
  public boolean isInstanceAlive() {
    long timestamp = System.currentTimeMillis();
    if (connection.hexists(HEARTBEAT, instance)) {
      long lastBeat = Long.parseLong(connection.hget(HEARTBEAT, instance));
      return timestamp >= lastBeat + (20 * 1000); // 20 seconds in terms of milliseconds
    }
    return false;
  }

  @Override
  public Set<String> getServerPlayers(String server) {
    return connection.smembers(replace(SERVER_PLAYERS, server));
  }

  @Override
  public long getServerPlayerCount(String server) {
    return connection.scard(replace(SERVER_PLAYERS, server));
  }

  @Override
  public Set<String> getNetworkPlayers() {
    // TODO: Consider caching this to be updated with the heartbeat.
    Set<String> players = new HashSet<>();
    ScanResult<String> scanner = connection.scan("instance");
    while (!scanner.isCompleteIteration()) {
      List<String> result = scanner.getResult();

      result.forEach(instance ->
              players.addAll(connection.smembers(replace(INSTANCE_PLAYERS, instance)))
      );
    }

    return players;
  }

  @Deprecated
  @Override
  public long getNetworkPlayerCount() {
    long count = 0;
    ScanResult<String> scanner = connection.scan("instance");
    while (!scanner.isCompleteIteration()) {
      count += scanner.getResult().size();
    }

    return count;
  }

  @Override
  public void publish(String channel, String message) {
    connection.publish(channel, message);
  }

  @Deprecated
  @Override
  public void deleteHeartbeat() {
    shutdown();
  }

  @Override
  public void shutdown() {
    connection.hdel(HEARTBEAT, instance);
  }

  @Override
  public void close() {
    connection.close();
  }
}
