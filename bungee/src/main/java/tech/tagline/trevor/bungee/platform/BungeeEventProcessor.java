package tech.tagline.trevor.bungee.platform;

import tech.tagline.trevor.api.event.*;
import tech.tagline.trevor.bungee.TrevorBungee;
import tech.tagline.trevor.bungee.platform.event.*;
import tech.tagline.trevor.common.platform.EventProcessor;

import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class BungeeEventProcessor implements EventProcessor {

  private final TrevorBungee plugin;

  public BungeeEventProcessor(TrevorBungee plugin) {
    this.plugin = plugin;
  }

  @Override
  public EventAction<BungeeNetworkConnectEvent> onConnect(UUID uuid) {
    return wrap(new BungeeNetworkConnectEvent(uuid));
  }

  @Override
  public EventAction<BungeeNetworkDisconnectEvent> onDisconnect(UUID uuid) {
    return wrap(new BungeeNetworkDisconnectEvent(uuid));
  }

  @Override
  public EventAction<BungeeNetworkServerChangeEvent> onServerChange(UUID uuid, String server,
                                                                     String previousServer) {
    return wrap(new BungeeNetworkServerChangeEvent(uuid, server, previousServer));
  }

  @Override
  public EventAction<BungeeNetworkMessageEvent> onMessage(String channel, String message) {
    return wrap(new BungeeNetworkMessageEvent(channel, message));
  }

  private <T extends BungeeNetworkEvent> EventAction<T> wrap(T event) {
    return new EventAction<T>(event, plugin.getProxy().getPluginManager()::callEvent);
  }
}
