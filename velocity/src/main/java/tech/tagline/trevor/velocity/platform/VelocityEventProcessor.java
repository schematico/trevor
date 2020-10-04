package tech.tagline.trevor.velocity.platform;

import tech.tagline.trevor.api.network.payload.ConnectPayload;
import tech.tagline.trevor.api.network.payload.DisconnectPayload;
import tech.tagline.trevor.api.network.payload.NetworkPayload;
import tech.tagline.trevor.api.network.payload.ServerChangePayload;
import tech.tagline.trevor.api.network.event.EventProcessor;
import tech.tagline.trevor.velocity.TrevorVelocity;
import tech.tagline.trevor.velocity.platform.event.*;

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
  public EventAction<VelocityNetworkMessageEvent> onMessage(NetworkPayload payload) {
    return wrap(new VelocityNetworkMessageEvent(payload));
  }

  private <T extends VelocityNetworkEvent> EventAction<T> wrap(T event) {
    return new EventAction<>(event, plugin.getProxy().getEventManager()::fire);
  }
}
