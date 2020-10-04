package tech.tagline.trevor.api.network.event;

import tech.tagline.trevor.api.network.payload.NetworkPayload;

public interface NetworkIntercomEvent extends NetworkEvent {

  String channel();

  NetworkPayload payload();
}
