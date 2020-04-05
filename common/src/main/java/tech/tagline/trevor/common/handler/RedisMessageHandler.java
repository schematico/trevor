package tech.tagline.trevor.common.handler;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.exceptions.JedisConnectionException;
import tech.tagline.trevor.api.Keys;
import tech.tagline.trevor.api.data.payload.ConnectData;
import tech.tagline.trevor.api.data.payload.DisconnectData;
import tech.tagline.trevor.api.data.payload.IntercomPayload;
import tech.tagline.trevor.api.data.payload.ServerChangeData;
import tech.tagline.trevor.api.event.NetworkEvent;
import tech.tagline.trevor.api.event.NetworkMessageEvent;
import tech.tagline.trevor.common.TrevorCommon;
import tech.tagline.trevor.common.platform.EventProcessor;

import java.util.*;
import java.util.concurrent.CompletableFuture;
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
              intercom(common.getPlatform().fromJson(message, IntercomPayload.class));
            } else {
              common.getPlatform().getEventProcessor().onMessage(channel, message).post();
            }
          }
        }
      };

      resource.subscribe(pipeline, channels.toArray(new String[0]));
    } catch (JedisConnectionException exception) {
      exception.printStackTrace();
    }
  }

  private void intercom(IntercomPayload payload) {
    switch(payload.getType()) {
      case CONNECT:
        ConnectData connectData = (ConnectData) payload.getData();

        // TODO: Update local cache

        common.getPlatform().getEventProcessor().onConnect(payload.getUuid());
      case DISCONNECT:
        DisconnectData diconnectData = (DisconnectData) payload.getData();

        // TODO: Update local cache

        common.getPlatform().getEventProcessor().onDisconnect(payload.getUuid());
      case SERVERCHANGE:
        ServerChangeData serverChangeData = (ServerChangeData) payload.getData();

        // TODO: Update local cache

        common.getPlatform().getEventProcessor().onServerChange(payload.getUuid(),
                serverChangeData.getServer(), serverChangeData.getPreviousServer());
      default:
        // TODO: Notify console of illegal intercom
    }
  }
}
