package tech.tagline.trevor.bungee.platform.event;

import tech.tagline.trevor.api.network.event.NetworkIntercomEvent;

public class BungeeNetworkMessageEvent extends BungeeNetworkEvent implements NetworkIntercomEvent {

  private final String channel;
  private final String message;

  public BungeeNetworkMessageEvent(String channel, String message) {
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
