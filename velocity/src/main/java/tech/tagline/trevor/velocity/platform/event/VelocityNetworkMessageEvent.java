package tech.tagline.trevor.velocity.platform.event;

import tech.tagline.trevor.api.network.event.NetworkIntercomEvent;
import tech.tagline.trevor.api.network.payload.NetworkPayload;

public class VelocityNetworkMessageEvent extends VelocityNetworkEvent implements NetworkIntercomEvent {

  private final String channel;
  private final NetworkPayload payload;

  public VelocityNetworkMessageEvent(String channel, NetworkPayload payload) {
    this.channel = channel;
    this.payload = payload;
  }

  @Override
  public String channel() {
    return channel;
  }

  @Override
  public NetworkPayload payload() {
    return payload;
  }
}
