package co.schemati.trevor.api.network.event;

import java.util.UUID;

public interface NetworkDisconnectEvent extends NetworkEvent {

  UUID uuid();

  long timestamp();
}
