package tech.tagline.trevor.velocity.platform;

import tech.tagline.trevor.api.data.payload.ConnectPayload;
import tech.tagline.trevor.api.data.payload.DisconnectPayload;
import tech.tagline.trevor.api.data.payload.ServerChangePayload;
import tech.tagline.trevor.api.event.EventProcessor;
import tech.tagline.trevor.velocity.TrevorVelocity;
import tech.tagline.trevor.velocity.platform.event.*;

import java.util.UUID;

public class VelocityEventProcessor implements EventProcessor {

  private final TrevorVelocity plugin;

  public VelocityEventProcessor(TrevorVelocity plugin) {
    this.plugin = plugin;
  }

  @Override
  public EventAction<VelocityNetworkConnectEvent> onConnect(ConnectPayload payload) {
    return wrap(new VelocityNetworkConnectEvent(payload));
  }

  @Override
  public EventAction<VelocityNetworkDisconnectEvent> onDisconnect(DisconnectPayload payload) {
    return wrap(new VelocityNetworkDisconnectEvent(payload));
  }

  @Override
  public EventAction<VelocityNetworkServerChangeEvent> onServerChange(ServerChangePayload payload) {
    return wrap(new VelocityNetworkServerChangeEvent(payload));
  }

  @Override
  public EventAction<VelocityNetworkMessageEvent> onMessage(String channel, String message) {
    return wrap(new VelocityNetworkMessageEvent(channel, message));
  }

  private <T extends VelocityNetworkEvent> EventAction<T> wrap(T event) {
    return new EventAction<T>(event, plugin.getProxy().getEventManager()::fire);
  }
}
