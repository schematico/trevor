package tech.tagline.trevor.common.api.database.redis;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import redis.clients.jedis.JedisPubSub;
import tech.tagline.trevor.api.Keys;
import tech.tagline.trevor.api.data.payload.ConnectPayload;
import tech.tagline.trevor.api.data.payload.DisconnectPayload;
import tech.tagline.trevor.api.data.payload.NetworkPayload;
import tech.tagline.trevor.api.data.payload.ServerChangePayload;
import tech.tagline.trevor.api.event.EventProcessor;
import tech.tagline.trevor.common.TrevorCommon;
import tech.tagline.trevor.common.api.database.Database;
import tech.tagline.trevor.common.proxy.DatabaseProxy;

public class RedisIntercom implements Database.Intercom {

  private final Database database;
  private final DatabaseProxy proxy;
  private final EventProcessor processor;

  private JedisPubSub pipeline;

  public RedisIntercom(Database database, DatabaseProxy proxy, EventProcessor processor) {
    this.database = database;
    this.proxy = proxy;
    this.processor = processor;
  }

  @Override
  public void run() {
    database.open().thenAccept(connection -> {
      this.pipeline = new JedisPubSub() {
        @Override
        public void onMessage(final String channel, final String message) {
          database.getExecutor().submit(() -> {
            if (message.trim().length() > 0) {
              if (channel.equals(Keys.CHANNEL_DATA.of())) {
                proxy.onNetworkIntercom(channel, message);
              } else {
                processor.onMessage(channel, message).post();
              }
            }
          });
        }
      };
    });
  }
}
