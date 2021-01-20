package co.schemati.trevor.api.network.payload;

import co.schemati.trevor.api.network.event.EventProcessor;
import co.schemati.trevor.api.network.event.NetworkDisconnectEvent;

import java.util.UUID;

public class DisconnectPayload extends OwnedPayload {

  private final long timestamp;

  protected DisconnectPayload(String source, UUID uuid, long timestamp) {
    super(source, uuid);

    this.timestamp = timestamp;
  }

  public long timestamp() {
    return timestamp;
  }

  @Override
  public EventProcessor.EventAction<NetworkDisconnectEvent> process(EventProcessor processor) {
    return processor.onDisconnect(this);
  }

  public static DisconnectPayload of(String source, UUID uuid, long timestamp) {
    return new DisconnectPayload(source, uuid, timestamp);
  }
}
