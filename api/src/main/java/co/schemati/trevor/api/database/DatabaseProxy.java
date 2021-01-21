package co.schemati.trevor.api.database;

import co.schemati.trevor.api.data.User;
import co.schemati.trevor.api.network.payload.NetworkPayload;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Represents a proxy between the {@link Database} and the
 * {@link co.schemati.trevor.api.data.Platform} implementation.
 */
public interface DatabaseProxy {

  /**
   * Announces a {@link NetworkPayload} on the network using the {@link DatabaseIntercom}.
   *
   * @param channel the channel
   * @param event the payload
   */
  void post(String channel, NetworkPayload<?> event);

  /**
   * Announces a {@link NetworkPayload} on the network using the {@link DatabaseIntercom}.
   *
   * <br><br>
   *
   * This method differs from {@link DatabaseProxy#post(String channel, NetworkPayload event)} as
   * it requires an open {@link DatabaseConnection} to be provided. This method is used to
   * prevent opening another connection when the action needs to be performed inside an already
   * open connection state.
   *
   * @param channel the channel
   * @param connection the connection
   * @param event the payload
   */
  void post(String channel, DatabaseConnection connection, NetworkPayload<?> event);

  /**
   * Notifies the {@link co.schemati.trevor.api.TrevorAPI} implementation that a {@link User} has
   * connected to the proxy instance.
   *
   * @param user the user
   *
   * @return future wrapped {@link ConnectResult}
   */
  CompletableFuture<ConnectResult> onPlayerConnect(User user);

  /**
   * Notifies the {@link co.schemati.trevor.api.TrevorAPI} implementation that a {@link User} has
   * disconnected from the proxy instance.
   *
   * @param user the user
   */
  void onPlayerDisconnect(User user);

  /**
   * Notifies the {@link co.schemati.trevor.api.TrevorAPI} implementation that a {@link User} has
   * changed servers.
   *
   * @param user the user
   * @param server the server
   * @param previousServer the previous server
   */
  void onPlayerServerChange(User user, String server, String previousServer);

  /**
   * Notifies the {@link co.schemati.trevor.api.TrevorAPI} implementation that a network
   * announcement has been received.
   *
   * @param channel the channel
   * @param message the message
   */
  void onNetworkIntercom(String channel, String message);

  /**
   * Represents the result of a {@link User} connection.
   */
  class ConnectResult {

    private boolean allowed;
    private String message;

    /**
     * Whether the connection is allowed.
     *
     * @return allowed
     */
    public boolean isAllowed() {
      return allowed;
    }

    /**
     * Returns the message provided when denying the connection. It's not safe to assume the
     * message will actually be present, even if the connection is denied.
     *
     * @return the message
     */
    public Optional<String> getMessage() {
      return Optional.ofNullable(message);
    }

    /**
     * Returns a constructed {@link ConnectResult} that allows the {@link User} connection.
     *
     * @return the result
     */
    public static ConnectResult allow() {
      ConnectResult result = new ConnectResult();

      result.allowed = true;

      return result;
    }

    /**
     * Returns a constructed {@link ConnectResult} that denies the {@link User} connection with
     * the provided message.
     *
     * @param message the message
     *
     * @return the result
     */
    public static ConnectResult deny(String message) {
      ConnectResult result = new ConnectResult();

      result.allowed = false;
      result.message = message;

      return result;
    }
  }
}
