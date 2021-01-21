package co.schemati.trevor.api.network.event;

import java.util.UUID;

/**
 * Represents a {@link co.schemati.trevor.api.data.User} disconnecting from the network.
 */
public interface NetworkDisconnectEvent extends NetworkEvent {

  /**
   * The {@link co.schemati.trevor.api.data.User}'s {@link UUID}.
   *
   * @return the uuid
   */
  UUID uuid();

  /**
   * The timestamp of when the {@link co.schemati.trevor.api.data.User} disconnected.
   *
   * @return the timestamp
   */
  long timestamp();
}
