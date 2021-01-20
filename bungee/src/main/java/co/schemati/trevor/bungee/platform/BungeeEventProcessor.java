package co.schemati.trevor.bungee.platform;

import co.schemati.trevor.bungee.TrevorBungee;
import co.schemati.trevor.bungee.platform.event.BungeeNetworkConnectEvent;
import co.schemati.trevor.bungee.platform.event.BungeeNetworkDisconnectEvent;
import co.schemati.trevor.bungee.platform.event.BungeeNetworkEvent;
import co.schemati.trevor.bungee.platform.event.BungeeNetworkMessageEvent;
import co.schemati.trevor.bungee.platform.event.BungeeNetworkServerChangeEvent;
import co.schemati.trevor.api.network.payload.ConnectPayload;
import co.schemati.trevor.api.network.payload.DisconnectPayload;
import co.schemati.trevor.api.network.payload.NetworkPayload;
import co.schemati.trevor.api.network.payload.ServerChangePayload;
import co.schemati.trevor.api.network.event.EventProcessor;

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
  public EventAction<BungeeNetworkMessageEvent> onMessage(NetworkPayload payload) {
    return wrap(new BungeeNetworkMessageEvent(payload));
  }

  private <T extends BungeeNetworkEvent> EventAction<T> wrap(T event) {
    return new EventAction<>(event, e -> {
      CompletableFuture<T> future = new CompletableFuture<>();

      plugin.getProxy().getScheduler().runAsync(plugin, () -> {
        plugin.getProxy().getPluginManager().callEvent(e);

        future.complete(e);
      });

      return future;
    });
  }
}
