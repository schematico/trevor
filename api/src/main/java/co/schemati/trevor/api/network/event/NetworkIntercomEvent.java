package co.schemati.trevor.api.network.event;

import co.schemati.trevor.api.network.payload.NetworkPayload;

/**
 * Represents a generic {@link NetworkPayload} received from the network.
 */
public interface NetworkIntercomEvent extends NetworkEvent {

  /**
   * Returns the {@link NetworkPayload}.
   *
   * @return the payload
   */
  NetworkPayload<?> payload();
}
