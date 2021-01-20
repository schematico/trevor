package co.schemati.trevor.velocity.platform.event;

import co.schemati.trevor.api.network.event.NetworkIntercomEvent;
import co.schemati.trevor.api.network.payload.NetworkPayload;

public class VelocityNetworkMessageEvent extends VelocityNetworkEvent implements NetworkIntercomEvent {

  private final NetworkPayload<?> payload;

  public VelocityNetworkMessageEvent(NetworkPayload<?> payload) {
    this.payload = payload;
  }

  @Override
  public NetworkPayload<?> payload() {
    return payload;
  }
}
