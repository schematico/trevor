package tech.tagline.trevor.common;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;
import tech.tagline.trevor.api.Keys;
import tech.tagline.trevor.common.data.InstanceData;
import tech.tagline.trevor.common.handler.DataHandler;
import tech.tagline.trevor.common.handler.RedisMessageHandler;
import tech.tagline.trevor.common.platform.Platform;
import tech.tagline.trevor.common.util.RedisIO;

public class TrevorCommon {

  private final Platform platform;

  private JedisPool pool;
  private DataHandler dataHandler;
  private RedisMessageHandler messageHandler;
  private InstanceData instanceData;

  public TrevorCommon(Platform platform) {
    this.platform = platform;
  }

  public boolean load() {
    // TODO: Verify instance configuration values before pool creation

    this.pool = platform.getRedisConfiguration().create();

    this.dataHandler = new DataHandler(this);
    this.messageHandler = new RedisMessageHandler(this);

    return true;
  }

  public boolean start() {
    String instanceID = platform.getInstanceConfiguration().getInstanceID();
    // Test connection and perform heartbeat
    try (Jedis resource = pool.getResource()) {
      resource.ping();

      // Make sure another instance isn't running with the same ID
      if (resource.hexists(Keys.DATABASE_HEARTBEAT.of(), instanceID)) {
        long lastBeat = Long.parseLong(resource.hget(Keys.DATABASE_HEARTBEAT.of(), instanceID));
        long databaseTime = RedisIO.getTime(resource.time());
        if (databaseTime < lastBeat + 20) {
          // TODO: Shutdown and inform console
          return false;
        }
      }

      // TODO: Initiate heartbeat. Start with 0 delay to complete first beat.
    } catch (JedisConnectionException exception) {
      exception.printStackTrace();

      pool.destroy();

      return false;
    }


    return true;
  }

  public boolean stop() {
    if (pool != null) {
      messageHandler.destroy();

      String instanceID = platform.getInstanceConfiguration().getInstanceID();
      try (Jedis resource = pool.getResource()) {
        resource.hdel("heartbeat", instanceID);
        if (resource.scard(Keys.INSTANCE_PLAYERS.with(instanceID)) > 0) {
          resource.smembers(Keys.INSTANCE_PLAYERS.with(instanceID)).forEach(dataHandler::destroy);
        }
      }
    }
    return true;
  }

  public Platform getPlatform() {
    return platform;
  }

  public JedisPool getPool() {
    return pool;
  }
}
