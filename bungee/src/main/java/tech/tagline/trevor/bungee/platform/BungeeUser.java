package tech.tagline.trevor.bungee.platform;

import tech.tagline.trevor.api.data.User;

import java.util.UUID;

public class BungeeUser extends User {

  public BungeeUser(UUID uuid, String address) {
    super(uuid, address);
  }
}
