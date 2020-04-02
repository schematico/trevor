package tech.tagline.trevor.bungee.platform.event;

import net.md_5.bungee.api.plugin.Event;
import tech.tagline.trevor.api.event.NetworkServerChangeEvent;

import java.util.UUID;

public class BungeeNetworkServerChangeEvent extends BungeeNetworkEvent implements NetworkServerChangeEvent {

  private final UUID uuid;
  private final String server;
  private final String previousServer;

  public BungeeNetworkServerChangeEvent(UUID uuid, String server, String previousServer) {
    this.uuid = uuid;
    this.server = server;
    this.previousServer = previousServer;
  }

  @Override
  public UUID getUUID() {
    return uuid;
  }

  @Override
  public String getServerName() {
    return server;
  }

  @Override
  public String getPreviousServerName() {
    return previousServer;
  }
}
