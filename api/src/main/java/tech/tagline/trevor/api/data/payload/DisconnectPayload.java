package tech.tagline.trevor.api.data.payload;

import java.util.UUID;

public class DisconnectPayload extends OwnedPayload {

  private final long timestamp;

  protected DisconnectPayload(String source, UUID uuid, long timestamp) {
    super(Content.DISCONNECT, source, uuid);

    this.timestamp = timestamp;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public static DisconnectPayload of(String source, UUID uuid, long timestamp) {
    return new DisconnectPayload(source, uuid, timestamp);
  }
}
