package tech.tagline.trevor.common.task;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;
import tech.tagline.trevor.api.Keys;
import tech.tagline.trevor.common.TrevorCommon;

public class HeartbeatTask implements Runnable {

  private final TrevorCommon common;

  public HeartbeatTask(TrevorCommon common) {
    this.common = common;
  }

  @Override
  public void run() {
    // Grab timestamp before any operations so all timestamped values are the same
    long timestamp = System.currentTimeMillis();

    String id = common.getPlatform().getInstanceConfiguration().getInstanceID();
    try (Jedis resource = common.getPool().getResource()) {
      resource.hset(Keys.DATABASE_HEARTBEAT.of(), id, String.valueOf(timestamp));
    } catch (JedisConnectionException exception) {
      // TODO: Notify console in a nicer way
      exception.printStackTrace();
      return;
    }

    common.getDataHandler().beat(timestamp);
  }
}
