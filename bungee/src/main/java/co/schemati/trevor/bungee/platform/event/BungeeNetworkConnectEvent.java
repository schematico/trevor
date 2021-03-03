package co.schemati.trevor.bungee.platform.event;

import co.schemati.trevor.api.network.event.NetworkConnectEvent;
import co.schemati.trevor.api.network.payload.ConnectPayload;

import java.util.UUID;

public class BungeeNetworkConnectEvent extends BungeeNetworkEvent implements NetworkConnectEvent {

  private final UUID uuid;
  private final String name;
  private final String address;

  public BungeeNetworkConnectEvent(ConnectPayload payload) {
    this.uuid = payload.uuid();
    this.name = payload.name();
    this.address = payload.address();
  }

  @Override
  public UUID uuid() {
    return uuid;
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public String address() {
    return address;
  }
}
