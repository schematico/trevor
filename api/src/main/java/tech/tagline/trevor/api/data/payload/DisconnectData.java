package tech.tagline.trevor.api.data.payload;

import java.util.UUID;

public class DisconnectData {

  private final long timestamp;

  public DisconnectData(long timestamp) {
    this.timestamp = timestamp;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public static IntercomPayload craft(String instanceID, UUID uuid, long timestamp) {
    return new IntercomPayload(IntercomPayload.Type.DISCONNECT, uuid, instanceID,
            new DisconnectData(timestamp));
  }
}
