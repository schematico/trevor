package tech.tagline.trevor.api.network.event;

public interface NetworkIntercomEvent extends NetworkEvent {

  String getChannel();

  String getMessage();
}
