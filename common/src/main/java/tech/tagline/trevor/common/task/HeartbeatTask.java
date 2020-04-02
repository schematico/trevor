package tech.tagline.trevor.common.task;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;
import tech.tagline.trevor.api.Keys;
import tech.tagline.trevor.common.TrevorCommon;
import tech.tagline.trevor.common.util.RedisIO;

public class HeartbeatTask implements Runnable {

  private final TrevorCommon common;

  public HeartbeatTask(TrevorCommon common) {
    this.common = common;
  }

  @Override
  public void run() {
    String id = common.getPlatform().getInstanceConfiguration().getInstanceID();
    try (Jedis resource = common.getPool().getResource()) {
      long databaseTime = RedisIO.getRedisTime(resource.time());
      resource.hset(Keys.DATABASE_HEARTBEAT.of(), id, String.valueOf(databaseTime));
    } catch (JedisConnectionException exception) {
      // TODO: Notify console in a nicer way
      exception.printStackTrace();
      return;
    }
    // TODO: Get server ids
    // TODO: Grab player count
  }
}
