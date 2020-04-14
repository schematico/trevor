package tech.tagline.trevor.api.event;

public interface NetworkIntercomEvent extends NetworkEvent {

  String getChannel();

  String getMessage();
}
