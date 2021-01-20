package co.schemati.trevor.api.network.event;

import java.util.UUID;

public interface NetworkConnectEvent extends NetworkEvent {

  UUID uuid();

  String address();
}
