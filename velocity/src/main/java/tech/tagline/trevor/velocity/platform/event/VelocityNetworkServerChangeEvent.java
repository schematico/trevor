package tech.tagline.trevor.velocity.platform.event;

import tech.tagline.trevor.api.network.payload.ServerChangePayload;
import tech.tagline.trevor.api.network.event.NetworkServerChangeEvent;

import java.util.UUID;

public class VelocityNetworkServerChangeEvent extends VelocityNetworkEvent implements NetworkServerChangeEvent {

  private final UUID uuid;
  private final String server;
  private final String previousServer;

  public VelocityNetworkServerChangeEvent(ServerChangePayload payload) {
    this.uuid = payload.getUUID();
    this.server = payload.getServer();
    this.previousServer = payload.getPreviousServer();
  }

  @Override
  public UUID uuid() {
    return uuid;
  }

  @Override
  public String server() {
    return server;
  }

  @Override
  public String previousServer() {
    return previousServer;
  }
}
