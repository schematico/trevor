package co.schemati.trevor.velocity.platform;

import co.schemati.trevor.velocity.TrevorVelocity;
import co.schemati.trevor.velocity.platform.event.VelocityNetworkConnectEvent;
import co.schemati.trevor.velocity.platform.event.VelocityNetworkDisconnectEvent;
import co.schemati.trevor.velocity.platform.event.VelocityNetworkEvent;
import co.schemati.trevor.velocity.platform.event.VelocityNetworkMessageEvent;
import co.schemati.trevor.velocity.platform.event.VelocityNetworkServerChangeEvent;
import co.schemati.trevor.api.network.payload.ConnectPayload;
import co.schemati.trevor.api.network.payload.DisconnectPayload;
import co.schemati.trevor.api.network.payload.NetworkPayload;
import co.schemati.trevor.api.network.payload.ServerChangePayload;
import co.schemati.trevor.api.network.event.EventProcessor;

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
