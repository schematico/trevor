package tech.tagline.trevor.api.data;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public interface User {

  UUID getUUID();

  InetAddress getAddress();

  default Map<String, String> toDatabaseMap(String instanceID) {
    Map<String, String> data = new HashMap<>(4);

    data.put("lastOnline", "0");
    data.put("ip", getAddress().getHostAddress());
    data.put("instance", instanceID);

    return data;
  }
}
