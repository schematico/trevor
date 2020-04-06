package tech.tagline.trevor.common.handler;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.exceptions.JedisConnectionException;
import tech.tagline.trevor.api.Keys;
import tech.tagline.trevor.api.data.payload.ConnectPayload;
import tech.tagline.trevor.api.data.payload.DisconnectPayload;
import tech.tagline.trevor.api.data.payload.NetworkPayload;
import tech.tagline.trevor.api.data.payload.ServerChangePayload;
import tech.tagline.trevor.common.TrevorCommon;

import java.util.*;

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
    String id = common.getPlatform().getInstanceConfiguration().getInstanceID();
    try (Jedis resource = common.getPool().getResource()) {
      channels.add(Keys.CHANNEL_INSTANCE.with(id));
      channels.add(Keys.CHANNEL_SERVERS.of());
      channels.add(Keys.CHANNEL_DATA.of());

      this.pipeline = new JedisPubSub() {
        @Override
        public void onMessage(final String channel, final String message) {
          if (message.trim().length() > 0) {
            if (channel.equals(Keys.CHANNEL_DATA.of())) {
              DataHandler.executor.submit(() -> {
                try {
                  intercom(id, message);
                } catch (Exception exception) {
                  exception.printStackTrace();
                }
              });
            } else {
              DataHandler.executor.submit(() ->
                      common.getPlatform().getEventProcessor().onMessage(channel, message).post());
            }
          }
        }
      };

      resource.subscribe(pipeline, channels.toArray(new String[0]));
    } catch (JedisConnectionException exception) {
      exception.printStackTrace();
    }
  }

  private void intercom(String id, String message) {
    // TODO: Figure out why JsonParser#parseString() doesn't work
    JsonObject json = new JsonParser().parse(message).getAsJsonObject();

    NetworkPayload.Content content = NetworkPayload.Content
            .valueOf(json.get("content").getAsString());

    NetworkPayload payload = DataHandler.gson.fromJson(message, content.getContentClass());
    if (id.equals(payload.getSource())) {
      return;
    }

    switch(payload.getContent()) {
      case CONNECT:
        ConnectPayload connect = (ConnectPayload) payload;

        common.getPlatform().getEventProcessor().onConnect(connect).post();
        break;
      case DISCONNECT:
        DisconnectPayload disconnect = (DisconnectPayload) payload;

        common.getPlatform().getEventProcessor().onDisconnect(disconnect).post();
        break;
      case SERVERCHANGE:
        ServerChangePayload serverChange = (ServerChangePayload) payload;

        common.getPlatform().getEventProcessor().onServerChange(serverChange).post();
        break;
      default:
        break;
    }
  }
}
