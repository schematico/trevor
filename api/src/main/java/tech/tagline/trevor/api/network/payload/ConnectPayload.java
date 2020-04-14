package tech.tagline.trevor.api.data.payload;

import java.util.UUID;

public class ConnectPayload extends OwnedPayload {

  private String address;

  protected ConnectPayload(String source, UUID uuid, String address) {
    super(Content.CONNECT, source, uuid);
  }

  public String getAddress() {
    return address;
  }

  public static ConnectPayload of(String source, UUID uuid, String address) {
    return new ConnectPayload(source, uuid, address);
  }
}
