package tech.tagline.trevor.common.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import tech.tagline.trevor.api.Keys;
import tech.tagline.trevor.api.data.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RedisIO {

  public static long getRedisTime(List<String> timeList) {
    return Long.parseLong(timeList.get(0));
  }

  public static void create(Pipeline pipeline, String id, User user, boolean post) {
    Map<String, String> data = new HashMap<>(4);

    data.put("online", "0");
    data.put("ip", user.getAddress().getAddress().getHostAddress());
    data.put("instance", id);

    pipeline.sadd(Keys.INSTANCE_PLAYERS.with(id), user.getUUID().toString());
    pipeline.hmset(Keys.PLAYER_DATA.with(user.getUUID().toString()), data);

    if (post) {
      // TODO: Publish player connect data
      pipeline.publish(Keys.CHANNEL_DATA.of(), "");
    }
  }

  public static void destroy(Jedis resource, String id, String player) {
    resource.srem(Keys.INSTANCE_PLAYERS.with(id), player);
    resource.hdel(Keys.PLAYER_DATA.with(player), "server", "ip", "instance");

    long timestamp = System.currentTimeMillis();
    resource.hset(Keys.PLAYER_DATA.with(player), "online", String.valueOf(timestamp));

    // TODO: Publish player disconnect data
    resource.publish(Keys.CHANNEL_DATA.of(), "");
  }

  public static void updateHeartbeat() {

  }
}
