package tech.tagline.trevor.bungee.platform.event;

import tech.tagline.trevor.api.data.payload.DisconnectPayload;
import tech.tagline.trevor.api.event.NetworkDisconnectEvent;

import java.util.UUID;

public class BungeeNetworkDisconnectEvent extends BungeeNetworkEvent implements NetworkDisconnectEvent {

  private final UUID uuid;
  private final long timestamp;

  public BungeeNetworkDisconnectEvent(DisconnectPayload payload) {
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
