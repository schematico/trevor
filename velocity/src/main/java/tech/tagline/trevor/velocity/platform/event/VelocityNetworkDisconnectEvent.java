package tech.tagline.trevor.velocity.platform.event;

import tech.tagline.trevor.api.network.payload.DisconnectPayload;
import tech.tagline.trevor.api.network.event.NetworkDisconnectEvent;

import java.util.UUID;

public class VelocityNetworkDisconnectEvent extends VelocityNetworkEvent implements NetworkDisconnectEvent {

  private final UUID uuid;
  private final long timestamp;

  public VelocityNetworkDisconnectEvent(DisconnectPayload payload) {
    this.uuid = payload.getUUID();
    this.timestamp = payload.getTimestamp();
  }

  @Override
  public UUID getUUID() {
    return uuid;
  }

  @Override
  public long getTimestamp() {
    return timestamp;
  }
}
