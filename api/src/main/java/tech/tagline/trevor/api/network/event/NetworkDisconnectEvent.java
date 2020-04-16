package tech.tagline.trevor.api.network.event;

import java.util.UUID;

public interface NetworkDisconnectEvent extends NetworkEvent {

  UUID getUUID();

  long getTimestamp();
}
