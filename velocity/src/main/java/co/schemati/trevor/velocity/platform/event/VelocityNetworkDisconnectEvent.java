package co.schemati.trevor.velocity.platform.event;

import co.schemati.trevor.api.network.payload.DisconnectPayload;
import co.schemati.trevor.api.network.event.NetworkDisconnectEvent;

import java.util.UUID;

public class VelocityNetworkDisconnectEvent extends VelocityNetworkEvent implements NetworkDisconnectEvent {

  private final UUID uuid;
  private final long timestamp;

  public VelocityNetworkDisconnectEvent(DisconnectPayload payload) {
    this.uuid = payload.uuid();
    this.timestamp = payload.timestamp();
  }

  @Override
  public UUID uuid() {
    return uuid;
  }

  @Override
  public long timestamp() {
    return timestamp;
  }
}
