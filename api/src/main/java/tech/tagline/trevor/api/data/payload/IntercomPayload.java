package tech.tagline.trevor.api.data.payload;

import java.util.UUID;

public class IntercomPayload {

  private final Type type;
  private final UUID uuid;
  private final String source;
  private final Object data;

  public IntercomPayload(Type type, UUID uuid, String source, Object data) {
    this.type = type;
    this.uuid = uuid;
    this.source = source;
    this.data = data;
  }

  public Type getType() {
    return type;
  }

  public UUID getUuid() {
    return uuid;
  }

  public String getSource() {
    return source;
  }

  public Object getData() {
    return data;
  }

  public enum Type {
    CONNECT,
    DISCONNECT,
    SERVERCHANGE
  }
}
