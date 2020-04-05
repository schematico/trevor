package tech.tagline.trevor.common.handler;

import com.google.common.collect.ImmutableList;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;
import tech.tagline.trevor.api.Keys;
import tech.tagline.trevor.api.data.User;
import tech.tagline.trevor.api.data.payload.ConnectData;
import tech.tagline.trevor.api.data.payload.DisconnectData;
import tech.tagline.trevor.api.data.payload.IntercomPayload;
import tech.tagline.trevor.api.data.payload.ServerChangeData;
import tech.tagline.trevor.common.TrevorCommon;
import tech.tagline.trevor.common.platform.Platform;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

public class DataHandler {

  public final static ScheduledExecutorService executor =
          Executors.newScheduledThreadPool(8);

  private final TrevorCommon common;
  private final Platform platform;
  private final String instanceID;
  private final InstanceData instanceData;

  public DataHandler(TrevorCommon common) {
    this.common = common;
    this.platform = common.getPlatform();
    this.instanceID = platform.getInstanceConfiguration().getInstanceID();
    this.instanceData = new InstanceData();
  }

  public void beat(long timestamp) {
    try (Jedis resource = common.getPool().getResource()) {
      ImmutableList.Builder<String> builder = ImmutableList.builder();
      int totalPlayers = 0;

      Map<String, String> heartbeats = resource.hgetAll(Keys.DATABASE_HEARTBEAT.of());
      for (Map.Entry<String, String> entry : heartbeats.entrySet()) {
        long heartbeatStamp = Long.parseLong(entry.getValue());
        if (timestamp <= heartbeatStamp + 30) {
          builder.add(entry.getKey());

          totalPlayers += resource.scard(Keys.INSTANCE_PLAYERS.with(entry.getKey()));
        } else {
          // TODO: Potentially notify that the instance could be dead.
        }
      }

      instanceData.instances = builder.build();
      instanceData.playerCount.set(totalPlayers);
    }
  }

  public void create(User user, boolean post) {
    executor.submit(() -> {
      try (Jedis resource = common.getPool().getResource()) {
        if (!resource.sismember(Keys.INSTANCE_PLAYERS.with(instanceID),
                user.getUUID().toString())) {
          Map<String, String> data = user.toDatabaseMap(instanceID);

          resource.hmset(Keys.PLAYER_DATA.with(user), data);
          resource.sadd(Keys.INSTANCE_PLAYERS.with(instanceID), user.getUUID().toString());
        }

        if (post) {
          IntercomPayload payload = ConnectData.craft(instanceID, user.getUUID(),
                  user.getAddress());
          resource.publish(Keys.CHANNEL_DATA.of(), platform.toJson(payload));
        }
      } catch (JedisConnectionException exception) {
        // TODO: Notify console in a nicer way
        exception.printStackTrace();
      }
    });
  }

  public void destroy(String uuid, boolean post) {
    executor.submit(() -> {
      long timestamp = System.currentTimeMillis();
      try (Jedis resource = common.getPool().getResource()) {
        if (resource.sismember(Keys.INSTANCE_PLAYERS.with(instanceID), uuid)) {
          resource.srem(Keys.INSTANCE_PLAYERS.with(instanceID), uuid);
          resource.hdel(Keys.PLAYER_DATA.with(uuid), "server", "ip", "instance");
          resource.hset(Keys.PLAYER_DATA.with(uuid), "lastOnline", String.valueOf(timestamp));
        }

        if (post) {
          IntercomPayload payload = DisconnectData.craft(instanceID, UUID.fromString(uuid),
                  timestamp);
          resource.publish(Keys.CHANNEL_DATA.of(), platform.toJson(payload));
        }
      } catch (JedisConnectionException exception) {
        // TODO: Notify console in a nicer way
        exception.printStackTrace();
      }
    });
  }

  public void setServer(User user, String server, String previousServer, boolean post) {
    try (Jedis resource = common.getPool().getResource()) {
      resource.hset(Keys.PLAYER_DATA.with(user), "server", server);

      if (post) {
        IntercomPayload payload = ServerChangeData.craft(instanceID, user, server, previousServer);

        resource.publish(Keys.CHANNEL_DATA.of(), platform.toJson(payload));
      }
    }
  }

  public InstanceData getInstanceData() {
    return instanceData;
  }

  public static class InstanceData {

    private List<String> instances = new ArrayList<>();
    private final AtomicInteger playerCount = new AtomicInteger();

    public int getPlayerCount() {
      return playerCount.get();
    }

    public List<String> getInstances() {
      return instances;
    }
  }
}
