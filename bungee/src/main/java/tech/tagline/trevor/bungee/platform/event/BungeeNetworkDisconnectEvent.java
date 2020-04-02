package tech.tagline.trevor.bungee.platform.event;

import net.md_5.bungee.api.plugin.Event;
import tech.tagline.trevor.api.event.NetworkDisconnectEvent;

import java.util.UUID;

public class BungeeNetworkDisconnectEvent extends BungeeNetworkEvent implements NetworkDisconnectEvent {

  private final UUID uuid;

  public BungeeNetworkDisconnectEvent(UUID uuid) {
    this.uuid = uuid;
  }

  @Override
  public UUID getUUID() {
    return uuid;
  }
}
