package tech.tagline.trevor.velocity.platform.event;

import tech.tagline.trevor.api.event.NetworkDisconnectEvent;

import java.util.UUID;

public class VelocityNetworkDisconnectEvent extends VelocityNetworkEvent implements NetworkDisconnectEvent {

  private final UUID uuid;

  public VelocityNetworkDisconnectEvent(UUID uuid) {
    this.uuid = uuid;
  }

  @Override
  public UUID getUUID() {
    return uuid;
  }
}
