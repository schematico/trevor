package co.schemati.trevor.api.network.payload;

import co.schemati.trevor.api.network.event.EventProcessor;
import co.schemati.trevor.api.network.event.NetworkEvent;

public abstract class NetworkPayload<S> {

  private final S source;

  protected NetworkPayload() {
    this.source = null;
  }

  protected NetworkPayload(S source) {
    this.source = source;
  }

  public S source() {
    return source;
  }

  public abstract EventProcessor.EventAction<? extends NetworkEvent> process(EventProcessor processor);
}
