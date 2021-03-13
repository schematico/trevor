package co.schemati.trevor.api.database;

import co.schemati.trevor.api.TrevorAPI;
import co.schemati.trevor.api.data.User;
import co.schemati.trevor.api.instance.InstanceData;
import co.schemati.trevor.api.network.payload.DisconnectPayload;

import javax.annotation.Nullable;
import java.io.Closeable;
import java.util.Set;
import java.util.UUID;

/**
 * Represents a connection to a remote database, created using a {@link Database} reference.
 */
public interface DatabaseConnection extends Closeable {

  /**
   * Performs a database 'heartbeat'.
   *
   * The database heartbeat updates the last alive timestamp in the database along with updating
   * all values associated with the {@link co.schemati.trevor.api.instance.InstanceData}.
   */
  void beat();

  /**
   * Updates a {@link InstanceData} instance using values from the remote database.
   *
   * @deprecated Should be implemented by {@link DatabaseConnection#beat()}.
   * @see DatabaseConnection#beat()
   *
   * @param data the instance data
   */
  @Deprecated
  void update(InstanceData data);

  /**
   * Registers the provided {@link User} in the remote database while also checking if they are
   * already logged into another instance.
   *
   * <br>
   *
   * A false result typically means the user should be denied access as they're already logged
   * into another instance.
   *
   * @param user the user
   *
   * @return creation success
   */
  boolean create(User user);

  /**
   * Removes the {@link User} registration in the remote database.
   *
   * <br>
   *
   * Returns a {@link DisconnectPayload}. This method does not announce the payload itself.
   *
   * @param uuid the user uuid
   *
   * @return the disconnect payload
   */
  DisconnectPayload destroy(UUID uuid);

  /**
   * Sets the server the {@link User} with the provided UUID is connected to.
   * Providing a null value for the server removes the server entry from the database.
   *
   * @see DatabaseConnection#setServer(User, String)
   *
   * @param uuid the uuid
   * @param server the server
   */
  void setServer(UUID uuid, @Nullable String server);
  
  /**
   * Sets the server the {@link User} is connected to.
   * Providing a null value for the server removes the server entry from the database.
   *
   * @param user the user
   * @param server the server
   */
  void setServer(User user, @Nullable String server);
  
  /**
   * Announces a message across the remote database network using the {@link DatabaseIntercom}.
   *
   * @param channel the channel
   * @param message the message
   */
  void publish(String channel, String message);

  /**
   * Checks if the {@link User} is online.
   *
   * @param user the user
   *
   * @return true if online
   */
  boolean isOnline(User user);

  /**
   * Checks if the instance is alive.
   *
   * @return true if alive
   */
  boolean isInstanceAlive();

  /**
   * Checks the remote database for all known connected players.
   *
   * @return the network players' uuids
   */
  Set<String> getNetworkPlayers();

  /**
   * Checks the remote database for the network player count.
   *
   * @deprecated Replace with {@link InstanceData#getPlayerCount()}. Instance data can be accessed
   * with {@link TrevorAPI#getInstanceData()}.
   *
   * @return the network player count
   */
  @Deprecated
  long getNetworkPlayerCount();

  /**
   * Checks the remote database for all known connected players on the given backend server.
   *
   * @param server the backend server
   *
   * @return the server players' uuids
   */
  Set<String> getServerPlayers(String server);

  /**
   * Checks the remote database for the player count on the given backend server.
   *
   * @param server the backend server
   *
   * @return the server player count
   */
  long getServerPlayerCount(String server);

  /**
   * Removes the instance's heartbeat entry in the remote database.
   *
   * @deprecated Use shutdown() instead
   */
  @Deprecated
  void deleteHeartbeat();

  /**
   * Uses this connected to clear the remote database of unnecessary instance information.
   * Calling this method outside of a platform implementation is not recommended as it can cause
   * issues with all plugins relying on the API.
   */
  void shutdown();
}
