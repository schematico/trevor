package tech.tagline.trevor.api.data;

import java.net.InetSocketAddress;
import java.util.UUID;

public interface User {

  UUID getUUID();

  InetSocketAddress getAddress();
}
