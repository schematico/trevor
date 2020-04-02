package tech.tagline.trevor.api.event;

public interface NetworkMessageEvent extends NetworkEvent {

  String getChannel();

  String getMessage();
}
