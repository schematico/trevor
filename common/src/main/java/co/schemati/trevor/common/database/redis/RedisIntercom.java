package co.schemati.trevor.common.database.redis;

import co.schemati.trevor.api.data.Platform;
import co.schemati.trevor.api.database.DatabaseIntercom;
import co.schemati.trevor.api.database.DatabaseProxy;
import co.schemati.trevor.api.util.Strings;
import co.schemati.trevor.common.util.Protocol;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.exceptions.JedisException;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.Future;

import static co.schemati.trevor.api.database.Database.CHANNEL_DATA;
import static co.schemati.trevor.api.database.Database.CHANNEL_INSTANCE;

public class RedisIntercom extends JedisPubSub implements DatabaseIntercom {

  private final Platform platform;
  private final RedisDatabase database;
  private final DatabaseProxy proxy;
  private final Gson gson;

  private final String instance;
  private final Set<String> channels = Sets.newHashSet();

  private Jedis connection;
  private Future<?> task;

  public RedisIntercom(Platform platform, RedisDatabase database, DatabaseProxy proxy, Gson gson) {
    this.platform = platform;
    this.database = database;
    this.proxy = proxy;
    this.gson = gson;

    this.instance = platform.getInstanceConfiguration().getID();
  }

  @Override
  public void init() {
    channels.add(Strings.replace(CHANNEL_INSTANCE, instance));
    channels.add(CHANNEL_DATA);

    this.task = database.getExecutor().submit(this);
  }

  @Override
  public void run() {
    try {
      this.connection = database.getResource();

      connection.subscribe(this, channels.toArray(String[]::new));
    } catch (JedisException exception) {
      exception.printStackTrace();
    }
  }

  public void add(String... channel) {
    channels.addAll(Arrays.asList(channel));
    super.subscribe(channels.toArray(String[]::new));
  }

  public void remove(String... channel) {
    channels.removeAll(Arrays.asList(channel));
    super.unsubscribe(channel);
  }

  public void kill() {
    channels.forEach(super::unsubscribe);
    channels.clear();

    connection.close();

    task.cancel(true);
  }

  @Override
  public void onMessage(String channel, String message) {
    database.getExecutor().submit(() -> {
      if (message.trim().length() > 0) {
        if (channel.equals(CHANNEL_DATA)) {
          proxy.onNetworkIntercom(channel, message);
        } else {
          Protocol.deserialize(message, gson).ifPresent(payload ->
                  payload.process(platform.getEventProcessor()).post()
          );
        }
      }
    });
  }
}
