package tech.tagline.trevor.api;

public enum Keys {
  CHANNEL_DATA("trevor:data"),

  DATABASE_HEARTBEAT("heartbeat"),

  INSTANCE_PLAYERS("proxy:{}:players"),

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
}
