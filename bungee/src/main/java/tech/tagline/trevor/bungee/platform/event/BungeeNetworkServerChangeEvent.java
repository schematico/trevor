package tech.tagline.trevor.bungee.platform.event;

import tech.tagline.trevor.api.data.payload.ServerChangePayload;
import tech.tagline.trevor.api.event.NetworkServerChangeEvent;

import java.util.UUID;

public class BungeeNetworkServerChangeEvent extends BungeeNetworkEvent implements NetworkServerChangeEvent {

  private final UUID uuid;
  private final String server;
  private final String previousServer;

  public BungeeNetworkServerChangeEvent(ServerChangePayload payload) {
    this.uuid = payload.getUUID();
    this.server = payload.getServer();
    this.previousServer = payload.getPreviousServer();
  }

  @Override
  public UUID getUUID() {
    return uuid;
  }

  @Override
  public String getServer() {
    return server;
  }

  @Override
  public String getPreviousServer() {
    return previousServer;
  }
}
