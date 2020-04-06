package tech.tagline.trevor.velocity.platform.event;

import tech.tagline.trevor.api.data.payload.ConnectPayload;
import tech.tagline.trevor.api.event.NetworkConnectEvent;

import java.util.UUID;

public class VelocityNetworkConnectEvent extends VelocityNetworkEvent
        implements NetworkConnectEvent {

  private final UUID uuid;
  private final String address;

  public VelocityNetworkConnectEvent(ConnectPayload payload) {
    this.uuid = payload.getUUID();
    this.address = payload.getAddress();
  }

  @Override
  public UUID getUUID() {
    return uuid;
  }

  @Override
  public String getAddress() {
    return address;
  }
}
