package co.schemati.trevor.api.network.payload;

import co.schemati.trevor.api.network.event.EventProcessor;
import co.schemati.trevor.api.network.event.NetworkDisconnectEvent;

import java.util.UUID;

/**
 * Represents the payload for a {@link co.schemati.trevor.api.data.User} disconnecting from the
 * network.
 */
public class DisconnectPayload extends OwnedPayload {

  private final long timestamp;

  /**
   * Constructs a new DisconnectPayload.
   *
   * @param source the instance source
   * @param uuid the user uuid
   * @param timestamp the disconnect timestamp
   */
  protected DisconnectPayload(String source, UUID uuid, long timestamp) {
    super(source, uuid);

    this.timestamp = timestamp;
  }

  /**
   * The timestamp of when the {@link co.schemati.trevor.api.data.User} disconnected.
   *
   * @return the timestamp
   */
  public long timestamp() {
    return timestamp;
  }

  @Override
  public EventProcessor.EventAction<NetworkDisconnectEvent> process(EventProcessor processor) {
    return processor.onDisconnect(this);
  }

  /**
   * Wraps the {@link DisconnectPayload} constructor.
   *
   * @see DisconnectPayload#DisconnectPayload(String, UUID, long)
   *
   * @param source the instance source
   * @param uuid the user uuid
   * @param timestamp the disconnect timestamp
   */
  public static DisconnectPayload of(String source, UUID uuid, long timestamp) {
    return new DisconnectPayload(source, uuid, timestamp);
  }
}
