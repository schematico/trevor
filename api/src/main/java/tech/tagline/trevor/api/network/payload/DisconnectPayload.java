package tech.tagline.trevor.api.network.payload;

import tech.tagline.trevor.api.network.event.EventProcessor;
import tech.tagline.trevor.api.network.event.NetworkDisconnectEvent;

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
