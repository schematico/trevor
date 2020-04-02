package tech.tagline.trevor.velocity.platform.event;

import tech.tagline.trevor.api.event.NetworkMessageEvent;

public class VelocityNetworkMessageEvent extends VelocityNetworkEvent implements NetworkMessageEvent {

  private final String channel;
  private final String message;

  public VelocityNetworkMessageEvent(String channel, String message) {
    this.channel = channel;
    this.message = message;
  }

  @Override
  public String getChannel() {
    return channel;
  }

  @Override
  public String getMessage() {
    return message;
  }
}
