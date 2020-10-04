package tech.tagline.trevor.api.network.event;

import java.util.UUID;

public interface NetworkServerChangeEvent extends NetworkEvent {

  UUID uuid();

  String server();

  String previousServer();
}
