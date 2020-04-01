package tech.tagline.trevor.api.event;

import java.util.UUID;

public class NetworkConnectEvent implements NetworkEvent {

  private final UUID uuid;

  public NetworkConnectEvent(UUID uuid) {
    this.uuid = uuid;
  }

  public UUID getUUID() {
    return uuid;
  }
}
