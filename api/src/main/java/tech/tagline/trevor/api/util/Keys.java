package tech.tagline.trevor.api.util;

import tech.tagline.trevor.api.data.User;

public enum Keys {
  CHANNEL_DATA("trevor:data"),
  CHANNEL_INSTANCE("trevor:{}"),
  CHANNEL_SERVERS("trevor:servers"),

  DATABASE_HEARTBEAT("heartbeat"),

  INSTANCE_PLAYERS("instance:{}:players"),

  PLAYER_DATA("player:{}");

  private final String key;

  Keys(String key) {
    this.key = key;
  }

  public String of() {
    return key;
  }

  public String with(String text) {
    return key.replace("{}", text);
  }

  public String with(User user) {
    return key.replace("{}", user.getUUID().toString());
  }
}
