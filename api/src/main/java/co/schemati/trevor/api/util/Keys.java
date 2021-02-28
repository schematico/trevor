package co.schemati.trevor.api.util;

import co.schemati.trevor.api.data.User;

/**
 * A utility class used to store all database keys.
 *
 * @deprecated implementation detail
 */
@Deprecated
public enum Keys {
  CHANNEL_DATA("trevor:data"),
  CHANNEL_INSTANCE("trevor:{}"),
  CHANNEL_SERVERS("trevor:servers"),

  DATABASE_HEARTBEAT("heartbeat"),

  INSTANCE_PLAYERS("instance:{}:players"),

  PLAYER_DATA("player:{}"),

  SERVER_PLAYERS("server:{}:players");

  private final String key;

  /**
   * Constructs a new {@link Keys} reference with the provided database key.
   *
   * @param key the database key
   */
  Keys(String key) {
    this.key = key;
  }

  /**
   * Returns the database key.
   *
   * @return the database key
   */
  public String of() {
    return key;
  }

  /**
   * Replaces the database key's {@code {}} parameter with the provided text and returns it.
   *
   * @param text the text
   *
   * @return the database key
   */
  public String with(String text) {
    return key.replace("{}", text);
  }

  /**
   * Replaces the database key's {@code {}} parameter with the provided {@link User}'s UUID and
   * returns it.
   *
   * @param user the user
   *
   * @return the database key
   */
  public String with(User user) {
    return key.replace("{}", user.uuid().toString());
  }
}
