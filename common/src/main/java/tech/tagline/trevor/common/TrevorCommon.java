package tech.tagline.trevor.common;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import tech.tagline.trevor.common.platform.Platform;

public class TrevorCommon {

  private final Platform platform;

  private JedisPool pool;

  public TrevorCommon(Platform platform) {
    this.platform = platform;
  }

  public boolean load() {
    this.pool = platform.getRedisConfiguration().create();

    return true;
  }

  public boolean start() {

    return true;
  }

  public boolean stop() {

    return true;
  }

  public JedisPool getPool() {
    return pool;
  }

  public Platform getPlatform() {
    return platform;
  }
}
