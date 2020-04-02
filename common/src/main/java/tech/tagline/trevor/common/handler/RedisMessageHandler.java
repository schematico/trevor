package tech.tagline.trevor.common.handler;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.exceptions.JedisConnectionException;
import tech.tagline.trevor.api.event.NetworkMessageEvent;
import tech.tagline.trevor.common.TrevorCommon;

import java.util.*;
import java.util.stream.Collectors;

public class RedisMessageHandler implements Runnable {

  private final TrevorCommon common;

  private JedisPubSub pipeline;
  private Set<String> channels = new HashSet<>();

  public RedisMessageHandler(TrevorCommon common) {
    this.common = common;
  }

  public void add(String... channel) {
    channels.addAll(Arrays.asList(channel));
    pipeline.subscribe(channel);
  }

  public void remove(String... channel) {
    channels.removeAll(Arrays.asList(channel));
    pipeline.unsubscribe(channel);
  }

  public void destroy() {
    channels.clear();
    pipeline.unsubscribe();
  }

  @Override
  public void run() {
    try (Jedis resource = common.getPool().getResource()) {
      channels.add("trevor:" + common.getPlatform().getInstanceConfiguration().getInstanceID());
      channels.add("trevor:servers");
      channels.add("trevor:data");

      this.pipeline = new JedisPubSub() {
        @Override
        public void onMessage(final String channel, final String message) {
          if (message.trim().length() > 0) {
            common.getPlatform().post(new NetworkMessageEvent(channel, message));
          }
        }
      };

      resource.subscribe(pipeline, channels.toArray(new String[0]));
    } catch (JedisConnectionException exception) {
      exception.printStackTrace();
    }
  }
}
