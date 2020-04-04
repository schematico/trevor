package tech.tagline.trevor.velocity.platform;

import tech.tagline.trevor.api.data.User;

import java.net.InetAddress;
import java.util.UUID;

public class VelocityUser extends User {

  public VelocityUser(UUID uuid, String address) {
    super(uuid, address);
  }
}
