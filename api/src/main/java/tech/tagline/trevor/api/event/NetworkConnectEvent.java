package tech.tagline.trevor.api.event;

import java.util.UUID;

public interface NetworkConnectEvent extends NetworkEvent {

  UUID getUUID();
}
