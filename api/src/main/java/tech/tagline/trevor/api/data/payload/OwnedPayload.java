package tech.tagline.trevor.api.data.payload;

import java.util.UUID;

public class OwnedPayload extends NetworkPayload {

  private final UUID uuid;

  protected OwnedPayload(Content content, String source, UUID uuid) {
    super(content, source);

    this.uuid = uuid;
  }

  public UUID getUUID() {
    return uuid;
  }
}
