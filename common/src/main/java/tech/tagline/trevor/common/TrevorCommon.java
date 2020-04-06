package tech.tagline.trevor.common;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;
import tech.tagline.trevor.api.Keys;
import tech.tagline.trevor.common.handler.DataHandler;
import tech.tagline.trevor.common.handler.LogicHandler;
import tech.tagline.trevor.common.handler.RedisMessageHandler;
import tech.tagline.trevor.common.platform.Platform;
import tech.tagline.trevor.common.task.HeartbeatTask;

import java.util.UUID;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class TrevorCommon {

  private final Platform platform;

  private JedisPool pool;
  private DataHandler dataHandler;
  private LogicHandler logicHandler;
  private RedisMessageHandler messageHandler;

  private Future<?> heartbeatTask;
  private Future<?> messageHandlerTask;

  public TrevorCommon(Platform platform) {
    this.platform = platform;
  }

  public boolean load() {
    // TODO: Verify instance configuration values before pool creation

    this.pool = platform.getRedisConfiguration().create();

    this.dataHandler = new DataHandler(this);
    this.logicHandler = new LogicHandler(this);
    this.messageHandler = new RedisMessageHandler(this);

    return true;
  }

  public boolean start() {
    String instanceID = platform.getInstanceConfiguration().getInstanceID();
    long timestamp = System.currentTimeMillis();

    // Test connection and perform heartbeat
    try (Jedis resource = pool.getResource()) {
      resource.ping();

      // Make sure another instance isn't running with the same ID
      if (resource.hexists(Keys.DATABASE_HEARTBEAT.of(), instanceID)) {
        long lastBeat = Long.parseLong(resource.hget(Keys.DATABASE_HEARTBEAT.of(), instanceID));
        if (timestamp < lastBeat + 20) {
          // TODO: Shutdown and inform console
          return false;
        }
      }

      this.heartbeatTask = DataHandler.executor.scheduleAtFixedRate(new HeartbeatTask(this),
              0, 5, TimeUnit.SECONDS);
    } catch (JedisConnectionException exception) {
      exception.printStackTrace();

      pool.destroy();

      return false;
    }

    this.messageHandlerTask = DataHandler.executor.submit(messageHandler);

    return true;
  }

  public boolean stop() {
    if (pool != null) {
      heartbeatTask.cancel(true);
      messageHandlerTask.cancel(true);

      messageHandler.destroy();

      String instanceID = platform.getInstanceConfiguration().getInstanceID();
      try (Jedis resource = pool.getResource()) {
        resource.hdel("heartbeat", instanceID);
        if (resource.scard(Keys.INSTANCE_PLAYERS.with(instanceID)) > 0) {
          resource.smembers(Keys.INSTANCE_PLAYERS.with(instanceID))
                  .forEach(uuid -> dataHandler.destroy(UUID.fromString(uuid), true));
        }
      }
    }
    return true;
  }

  public DataHandler getDataHandler() {
    return dataHandler;
  }

  public Platform getPlatform() {
    return platform;
  }

  public JedisPool getPool() {
    return pool;
  }

  public LogicHandler getLogicHandler() {
    return logicHandler;
  }
}
