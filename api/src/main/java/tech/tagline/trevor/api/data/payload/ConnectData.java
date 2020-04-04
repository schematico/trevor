package tech.tagline.trevor.api.data.payload;

import java.util.UUID;

public class ConnectData {

  private final String address;

  public ConnectData(String address) {
    this.address = address;
  }

  public String getAddress() {
    return address;
  }

  public static IntercomPayload craft(String instanceID, UUID uuid, String address) {
    return new IntercomPayload(IntercomPayload.Type.CONNECT, uuid, instanceID,
            new ConnectData(address));
  }
}
