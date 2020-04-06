package tech.tagline.trevor.bungee.platform;

import net.md_5.bungee.api.connection.Server;
import tech.tagline.trevor.api.data.payload.ConnectPayload;
import tech.tagline.trevor.api.data.payload.DisconnectPayload;
import tech.tagline.trevor.api.data.payload.ServerChangePayload;
import tech.tagline.trevor.bungee.TrevorBungee;
import tech.tagline.trevor.bungee.platform.event.*;
import tech.tagline.trevor.api.event.EventProcessor;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class BungeeEventProcessor implements EventProcessor {

  private final TrevorBungee plugin;

  public BungeeEventProcessor(TrevorBungee plugin) {
    this.plugin = plugin;
  }

  @Override
  public EventAction<BungeeNetworkConnectEvent> onConnect(ConnectPayload payload) {
    return wrap(new BungeeNetworkConnectEvent(payload));
  }

  @Override
  public EventAction<BungeeNetworkDisconnectEvent> onDisconnect(DisconnectPayload payload) {
    return wrap(new BungeeNetworkDisconnectEvent(payload));
  }

  @Override
  public EventAction<BungeeNetworkServerChangeEvent> onServerChange(ServerChangePayload payload) {
    return wrap(new BungeeNetworkServerChangeEvent(payload));
  }

  @Override
  public EventAction<BungeeNetworkMessageEvent> onMessage(String channel, String message) {
    return wrap(new BungeeNetworkMessageEvent(channel, message));
  }

  private <T extends BungeeNetworkEvent> EventAction<T> wrap(T event) {
    return new EventAction<T>(event, e -> {
      CompletableFuture<T> future = new CompletableFuture<T>();

      plugin.getProxy().getScheduler().runAsync(plugin, () -> {
        plugin.getProxy().getPluginManager().callEvent(e);

        future.complete(e);
      });

      return future;
    });
  }
}
