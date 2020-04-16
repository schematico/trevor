package tech.tagline.trevor.bungee.platform.event;

import tech.tagline.trevor.api.network.payload.ConnectPayload;
import tech.tagline.trevor.api.network.event.NetworkConnectEvent;

import java.util.UUID;

public class BungeeNetworkConnectEvent extends BungeeNetworkEvent implements NetworkConnectEvent {

  private final UUID uuid;
  private final String address;

  public BungeeNetworkConnectEvent(ConnectPayload payload) {
    this.uuid = payload.getUUID();
    this.address = payload.getAddress();
  }

  @Override
  public UUID getUUID() {
    return uuid;
  }

  @Override
  public String getAddress() {
    return address;
  }
}
