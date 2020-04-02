package tech.tagline.trevor.common;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;
import tech.tagline.trevor.common.handler.RedisMessageHandler;
import tech.tagline.trevor.common.platform.Platform;
import tech.tagline.trevor.common.util.RedisIO;

import java.util.Set;

public class TrevorCommon {

  private final Platform platform;

  private JedisPool pool;
  private RedisMessageHandler messageHandler;

  public TrevorCommon(Platform platform) {
    this.platform = platform;
  }

  public boolean load() {
    // TODO: Verify instance configuration values before pool creation

    this.pool = platform.getRedisConfiguration().create();

    this.messageHandler = new RedisMessageHandler(this);

    return true;
  }

  public boolean start() {
    // Test connection and perform heartbeat
    try (Jedis resource = pool.getResource()) {
      resource.ping();

      String id = platform.getInstanceConfiguration().getInstanceID();

      if (resource.hexists("heartbeat", id)) {
        long lastBeat = Long.parseLong(resource.hget("heartbeat", id));
        long databaseTime = RedisIO.getRedisTime(resource.time());
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

      String id = platform.getInstanceConfiguration().getInstanceID();
      try (Jedis resource = pool.getResource()) {
        resource.hdel("heartbeat", id);
        if (resource.scard("proxy:" + id + ":players") > 0) {
          resource.smembers("proxy:" + id + ":players")
                  .forEach(player -> RedisIO.destroy(resource, id, player));
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
