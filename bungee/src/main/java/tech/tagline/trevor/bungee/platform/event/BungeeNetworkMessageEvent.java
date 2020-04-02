package tech.tagline.trevor.bungee.platform.event;

import net.md_5.bungee.api.plugin.Event;
import tech.tagline.trevor.api.event.NetworkMessageEvent;

public class BungeeNetworkMessageEvent extends BungeeNetworkEvent implements NetworkMessageEvent {

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
