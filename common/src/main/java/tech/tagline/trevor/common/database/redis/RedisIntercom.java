package tech.tagline.trevor.common.database.redis;

import com.google.common.collect.Sets;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import tech.tagline.trevor.api.data.Platform;
import tech.tagline.trevor.api.util.Keys;
import tech.tagline.trevor.api.database.DatabaseIntercom;
import tech.tagline.trevor.api.database.DatabaseProxy;

import java.util.Arrays;
import java.util.Set;

public class RedisIntercom extends JedisPubSub implements DatabaseIntercom {

  private final Platform platform;
  private final RedisDatabase database;
  private final DatabaseProxy proxy;

  private final String instance;

  private Set<String> channels = Sets.newHashSet();

  public RedisIntercom(Platform platform, RedisDatabase database, DatabaseProxy proxy) {
    this.platform = platform;
    this.database = database;
    this.proxy = proxy;

    this.instance = platform.getInstanceConfiguration().getID();
  }

  @Override
  public void run() {
    database.open().thenAccept(connection -> {
      channels.add(Keys.CHANNEL_INSTANCE.with(instance));
      channels.add(Keys.CHANNEL_SERVERS.of());
      channels.add(Keys.CHANNEL_DATA.of());

      Jedis jedis = ((RedisConnection) connection).getConnection();

      jedis.subscribe(this, channels.toArray(new String[0]));
    });
  }

  public void add(String... channel) {
    channels.addAll(Arrays.asList(channel));
    super.subscribe(channels.toArray(new String[0]));
  }

  public void remove(String... channel) {
    channels.removeAll(Arrays.asList(channel));
    super.unsubscribe(channel);
  }

  public void destroy() {
    channels.forEach(super::unsubscribe);
    channels.clear();
  }

  @Override
  public void onMessage(String channel, String message) {
    database.getExecutor().submit(() -> {
      if (message.trim().length() > 0) {
        if (channel.equals(Keys.CHANNEL_DATA.of())) {
          proxy.onNetworkIntercom(channel, message);
        } else {
          platform.getEventProcessor().onMessage(channel, message).post();
        }
      }
    });
  }
}
