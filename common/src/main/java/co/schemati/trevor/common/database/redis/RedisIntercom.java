package co.schemati.trevor.common.database.redis;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import redis.clients.jedis.JedisPubSub;
import co.schemati.trevor.api.data.Platform;
import co.schemati.trevor.api.network.payload.NetworkPayload;
import co.schemati.trevor.api.util.Keys;
import co.schemati.trevor.api.database.DatabaseIntercom;
import co.schemati.trevor.api.database.DatabaseProxy;
import co.schemati.trevor.common.util.Protocol;

import java.util.Arrays;
import java.util.Set;

public class RedisIntercom extends JedisPubSub implements DatabaseIntercom {

  private final Platform platform;
  private final RedisDatabase database;
  private final DatabaseProxy proxy;
  private final Gson gson;

  private final String instance;
  private final Set<String> channels = Sets.newHashSet();

  public RedisIntercom(Platform platform, RedisDatabase database, DatabaseProxy proxy, Gson gson) {
    this.platform = platform;
    this.database = database;
    this.proxy = proxy;
    this.gson = gson;

    this.instance = platform.getInstanceConfiguration().getID();
  }

  @Override
  public void run() {
    channels.add(Keys.CHANNEL_INSTANCE.with(instance));
    channels.add(Keys.CHANNEL_SERVERS.of());
    channels.add(Keys.CHANNEL_DATA.of());

    database.getResource().subscribe(this, channels.toArray(new String[0]));
  }

  public void add(String... channel) {
    channels.addAll(Arrays.asList(channel));
    super.subscribe(channels.toArray(new String[0]));
  }

  public void remove(String... channel) {
    channels.removeAll(Arrays.asList(channel));
    super.unsubscribe(channel);
  }

  public void kill() {
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
          NetworkPayload<?> payload = Protocol.deserialize(message, gson);
          if (payload != null) {
            payload.process(platform.getEventProcessor()).post();
          }
        }
      }
    });
  }
}
