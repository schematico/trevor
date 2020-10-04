package tech.tagline.trevor.api.network.payload;

import java.util.UUID;

public abstract class OwnedPayload extends NetworkPayload<String> {

  private final UUID uuid;

  protected OwnedPayload(String source, UUID uuid) {
    super(source);

    this.uuid = uuid;
  }

  public UUID uuid() {
    return uuid;
  }
}
