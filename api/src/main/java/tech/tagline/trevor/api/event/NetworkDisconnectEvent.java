package tech.tagline.trevor.api.event;

import java.util.UUID;

public class NetworkDisconnectEvent implements NetworkEvent {

  private final UUID uuid;

  public NetworkDisconnectEvent(UUID uuid) {
    this.uuid = uuid;
  }

  public UUID getUUID() {
    return uuid;
  }
}
