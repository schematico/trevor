package co.schemati.trevor.bungee.platform.event;

import co.schemati.trevor.api.network.event.NetworkServerChangeEvent;
import co.schemati.trevor.api.network.payload.ServerChangePayload;

import java.util.UUID;

public class BungeeNetworkServerChangeEvent extends BungeeNetworkEvent implements NetworkServerChangeEvent {

  private final UUID uuid;
  private final String server;
  private final String previousServer;

  public BungeeNetworkServerChangeEvent(ServerChangePayload payload) {
    this.uuid = payload.uuid();
    this.server = payload.server();
    this.previousServer = payload.previousServer();
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
