package co.schemati.trevor.api.network.payload;

import co.schemati.trevor.api.network.event.EventProcessor;
import co.schemati.trevor.api.network.event.NetworkEvent;

/**
 * Represents a network message.
 *
 * @param <S> the source type
 */
public abstract class NetworkPayload<S> {

  private final S source;

  /**
   * Constructs a new NetworkPayload with no source.
   */
  protected NetworkPayload() {
    this.source = null;
  }

  /**
   * Constructs a new NetworkPayload.
   *
   * @param source the source
   */
  protected NetworkPayload(S source) {
    this.source = source;
  }

  /**
   * Returns the payload source.
   *
   * @return the source
   */
  public S source() {
    return source;
  }

  /**
   * Allows the {@link EventProcessor} to wrap the payload to allow compatibility with platform
   * event buses.
   *
   * @param processor the event processor
   *
   * @return the event action
   */
  public abstract EventProcessor.EventAction<? extends NetworkEvent> process(EventProcessor processor);
}
