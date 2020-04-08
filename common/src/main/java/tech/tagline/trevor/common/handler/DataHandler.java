package tech.tagline.trevor.common.handler;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;
import tech.tagline.trevor.api.Keys;
import tech.tagline.trevor.api.data.User;
import tech.tagline.trevor.api.data.payload.ConnectPayload;
import tech.tagline.trevor.api.data.payload.DisconnectPayload;
import tech.tagline.trevor.api.data.payload.ServerChangePayload;
import tech.tagline.trevor.common.TrevorCommon;
import tech.tagline.trevor.common.platform.Platform;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

public class DataHandler {

  public static final Gson gson = new Gson();
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
          ConnectPayload payload =  ConnectPayload.of(instanceID, user.getUUID(),
                  user.getAddress());

          resource.publish(Keys.CHANNEL_DATA.of(), gson.toJson(payload));
        }
      } catch (JedisConnectionException exception) {
        // TODO: Notify console in a nicer way
        exception.printStackTrace();
      }
    });
  }

  public void destroy(UUID uuid, boolean post) {
    executor.submit(() -> {
      long timestamp = System.currentTimeMillis();
      try (Jedis resource = common.getPool().getResource()) {
        if (resource.sismember(Keys.INSTANCE_PLAYERS.with(instanceID), uuid.toString())) {
          resource.srem(Keys.INSTANCE_PLAYERS.with(instanceID), uuid.toString());
          resource.hdel(Keys.PLAYER_DATA.with(uuid.toString()), "server", "ip", "instance");
          resource.hset(Keys.PLAYER_DATA.with(uuid.toString()), "lastOnline",
                  String.valueOf(timestamp));
        }

        if (post) {
          DisconnectPayload payload =  DisconnectPayload.of(instanceID, uuid, timestamp);

          resource.publish(Keys.CHANNEL_DATA.of(), gson.toJson(payload));
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
       ServerChangePayload payload =  ServerChangePayload.of(instanceID, user.getUUID(), server,
                previousServer);

       resource.publish(Keys.CHANNEL_DATA.of(), gson.toJson(payload));
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
