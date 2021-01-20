package co.schemati.trevor.bungee.platform.event;

import co.schemati.trevor.api.network.event.NetworkIntercomEvent;
import co.schemati.trevor.api.network.payload.NetworkPayload;

public class BungeeNetworkMessageEvent extends BungeeNetworkEvent implements NetworkIntercomEvent {

  private final NetworkPayload<?> payload;

  public BungeeNetworkMessageEvent(NetworkPayload<?> payload) {
    this.payload = payload;
  }

  @Override
  public NetworkPayload<?> payload() {
    return payload;
  }
}
