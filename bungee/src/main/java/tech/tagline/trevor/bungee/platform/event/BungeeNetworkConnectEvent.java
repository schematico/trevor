package tech.tagline.trevor.bungee.platform.event;

import tech.tagline.trevor.api.network.payload.ConnectPayload;
import tech.tagline.trevor.api.network.event.NetworkConnectEvent;

import java.util.UUID;

public class BungeeNetworkConnectEvent extends BungeeNetworkEvent implements NetworkConnectEvent {

  private final UUID uuid;
  private final String address;

  public BungeeNetworkConnectEvent(ConnectPayload payload) {
    this.uuid = payload.uuid();
    this.address = payload.address();
  }

  @Override
  public UUID uuid() {
    return uuid;
  }

  @Override
  public String address() {
    return address;
  }
}
