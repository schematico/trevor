package tech.tagline.trevor.bungee.platform.event;

import net.md_5.bungee.api.plugin.Event;
import tech.tagline.trevor.api.event.NetworkConnectEvent;

import java.util.UUID;

public class BungeeNetworkConnectEvent extends BungeeNetworkEvent implements NetworkConnectEvent {

  private final UUID uuid;

  public BungeeNetworkConnectEvent(UUID uuid) {
    this.uuid = uuid;
  }

  @Override
  public UUID getUUID() {
    return uuid;
  }
}
