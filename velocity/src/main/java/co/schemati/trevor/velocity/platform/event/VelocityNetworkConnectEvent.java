package co.schemati.trevor.velocity.platform.event;

import co.schemati.trevor.api.network.payload.ConnectPayload;
import co.schemati.trevor.api.network.event.NetworkConnectEvent;

import java.util.UUID;

public class VelocityNetworkConnectEvent extends VelocityNetworkEvent
        implements NetworkConnectEvent {

  private final UUID uuid;
  private final String address;

  public VelocityNetworkConnectEvent(ConnectPayload payload) {
    this.uuid = payload.uuid();
    this.address = payload.address();
  }

  @Override
  public UUID uuid() {
    return uuid;
  }

  @Override
  public String address() {
    return address;
  }
}
