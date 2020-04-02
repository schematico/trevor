package tech.tagline.trevor.api.data.payload;

import java.net.InetAddress;
import java.util.UUID;

public class ConnectData {

  private final InetAddress address;

  public ConnectData(InetAddress address) {
    this.address = address;
  }

  public InetAddress getAddress() {
    return address;
  }

  public static IntercomPayload craft(String instanceID, UUID uuid, InetAddress address) {
    return new IntercomPayload(IntercomPayload.Type.CONNECT, uuid, instanceID,
            new ConnectData(address));
  }
}
