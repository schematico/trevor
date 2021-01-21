package co.schemati.trevor.api.network.payload;

import java.util.UUID;

/**
 * Represents a {@link NetworkPayload} with a {@link String} source that carries a {@link UUID}.
 */
public abstract class OwnedPayload extends NetworkPayload<String> {

  private final UUID uuid;

  /**
   * Constructs a new OwnedPayload.
   *
   * @param source the source
   * @param uuid the uuid
   */
  protected OwnedPayload(String source, UUID uuid) {
    super(source);

    this.uuid = uuid;
  }

  /**
   * Returns the uuid.
   *
   * @return the uuid
   */
  public UUID uuid() {
    return uuid;
  }
}
