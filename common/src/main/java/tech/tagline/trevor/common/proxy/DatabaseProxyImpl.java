package tech.tagline.trevor.common.proxy;

import tech.tagline.trevor.api.data.User;
import tech.tagline.trevor.api.data.payload.ConnectPayload;
import tech.tagline.trevor.api.data.payload.DisconnectPayload;
import tech.tagline.trevor.api.data.payload.ServerChangePayload;
import tech.tagline.trevor.api.database.Database;
import tech.tagline.trevor.common.TrevorCommon;

import java.util.Optional;

public class DatabaseProxy {

  private final TrevorCommon common;

  public DatabaseProxy(TrevorCommon common) {
    this.common = common;
  }

  public ConnectResult onPlayerConnect(User user) {
    String instance = common.getPlatform().getInstanceConfiguration().getInstanceID();
    // TODO: Do something with this error
    Database.Connection connection = common.getDatabase().open().exceptionally(ex -> null).join();
    if (connection == null) {
      return ConnectResult.deny("&cAn error occurred, please try again.");
    }

    if (connection.isOnline(user)) {
      return ConnectResult.deny("&cYou are already logged in.");
    }

    ConnectPayload payload = ConnectPayload.of(instance, user.getUUID(), user.getAddress());

    connection.create(user);
    connection.publish(common.getGson().toJson(payload));

    return ConnectResult.allow();
  }

  public void onPlayerDisconnect(User user) {
    String instance = common.getPlatform().getInstanceConfiguration().getInstanceID();
    // TODO: Do something with this error
    long timestamp = System.currentTimeMillis();
    common.getDatabase().open().exceptionally(ex -> null).thenAcceptAsync(connection -> {
      if (connection != null) {
        DisconnectPayload payload = DisconnectPayload.of(instance, user.getUUID(), timestamp);

        connection.destroy(user.getUUID());
        connection.publish(common.getGson().toJson(payload));
      }
    });
  }

  public void onPlayerServerChange(User user, String server, String previousServer) {
    String instance = common.getPlatform().getInstanceConfiguration().getInstanceID();
    // TODO: Do something with this error
   common.getDatabase().open().exceptionally(ex -> null).thenAcceptAsync(connection -> {
     if (connection != null) {
       ServerChangePayload payload =
               ServerChangePayload.of(instance, user.getUUID(), server, previousServer);

       connection.setServer(user, server);
       connection.publish(common.getGson().toJson(payload));
     }
   });
  }

  public void onNetworkIntercom(String channel, String message) {

  }

  public static class ConnectResult {

    private boolean allowed;
    private String message;

    public boolean isAllowed() {
      return allowed;
    }

    public Optional<String> getMessage() {
      return Optional.ofNullable(message);
    }

    public static ConnectResult allow() {
      ConnectResult result = new ConnectResult();

      result.allowed = true;

      return result;
    }

    public static ConnectResult deny(String message) {
      ConnectResult result = new ConnectResult();

      result.allowed = false;
      result.message = message;

      return result;
    }
  }
}
