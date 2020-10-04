package tech.tagline.trevor.bungee.platform.event;

import tech.tagline.trevor.api.network.event.NetworkIntercomEvent;
import tech.tagline.trevor.api.network.payload.NetworkPayload;

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
