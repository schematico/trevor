package co.schemati.trevor.api.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Represents a player.
 */
public abstract class User {

  private final UUID uuid;
  private final String name;
  private final String address;

  /**
   * Construct a new user.
   *
   * @deprecated see {@link #User(UUID, String, String)}.
   *
   * @param uuid the user uuid
   * @param address the user address
   */
  @Deprecated
  protected User(UUID uuid, String address) {
    this.uuid = uuid;
    this.name = null;
    this.address = address;
  }

  /**
   * Construct a new user.
   *
   * @param uuid the user uuid
   * @param name the user name
   * @param address the user address
   */
  protected User(UUID uuid, String name, String address) {
    this.uuid = uuid;
    this.name = name;
    this.address = address;
  }

  /**
   * The user's uuid.
   *
   * @return uuid
   */
  public UUID uuid() {
    return uuid;
  }

  /**
   * The user's name.
   *
   * @return uuid
   */
  public String name() {
    return name;
  }

  /**
   * The address the user connected with.
   *
   * @return address
   */
  public String address() {
    return address;
  }

  /**
   * Serializes the user's information into a {@link Map}. Could be implementation specific.
   *
   * @param instance the instance id
   *
   * @return the serialized map
   */
  public Map<String, String> toDatabaseMap(String instance) {
    Map<String, String> data = new HashMap<>(4);

    data.put("lastOnline", "0");
    data.put("ip", address);
    data.put("instance", instance);

    return data;
  }

  @Override
  public String toString() {
    return uuid.toString();
  }
}
