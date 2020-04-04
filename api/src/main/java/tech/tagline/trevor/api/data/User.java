package tech.tagline.trevor.api.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class User {

  private UUID uuid;
  private String address;

  protected User(UUID uuid, String address) {
    this.uuid = uuid;
    this.address = address;
  }

  public UUID getUUID() {
    return uuid;
  }

  public String getAddress() {
    return address;
  }

  public Map<String, String> toDatabaseMap(String instanceID) {
    Map<String, String> data = new HashMap<>(4);

    data.put("lastOnline", "0");
    data.put("ip", address);
    data.put("instance", instanceID);

    return data;
  }
}
