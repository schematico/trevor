package tech.tagline.trevor.velocity.platform.event;

import tech.tagline.trevor.api.event.NetworkConnectEvent;

import java.util.UUID;

public class VelocityNetworkConnectEvent extends VelocityNetworkEvent
        implements NetworkConnectEvent {

  private final UUID uuid;

  public VelocityNetworkConnectEvent(UUID uuid) {
    this.uuid = uuid;
  }

  @Override
  public UUID getUUID() {
    return uuid;
  }
}
