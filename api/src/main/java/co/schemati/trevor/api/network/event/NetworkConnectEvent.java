package co.schemati.trevor.api.network.event;

import java.util.UUID;

/**
 * Represents a {@link co.schemati.trevor.api.data.User} connecting to the network.
 */
public interface NetworkConnectEvent extends NetworkEvent {

  /**
   * The {@link co.schemati.trevor.api.data.User}'s {@link UUID}.
   *
   * @return the uuid
   */
  UUID uuid();

  /**
   * The {@link co.schemati.trevor.api.data.User}'s name.
   *
   * @return the name
   */
  String name();

  /**
   * The {@link co.schemati.trevor.api.data.User}'s address.
   *
   * @return the address
   */
  String address();
}
