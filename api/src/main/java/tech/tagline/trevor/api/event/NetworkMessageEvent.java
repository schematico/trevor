package tech.tagline.trevor.api.event;

public class NetworkMessageEvent implements NetworkEvent {

  private final String channel;
  private final String message;

  public NetworkMessageEvent(String channel, String message) {
    this.channel = channel;
    this.message = message;
  }

  public String getChannel() {
    return channel;
  }

  public String getMessage() {
    return message;
  }
}
