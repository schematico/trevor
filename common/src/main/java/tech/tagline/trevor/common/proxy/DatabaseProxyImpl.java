package tech.tagline.trevor.common.proxy;

import com.google.gson.Gson;
import tech.tagline.trevor.api.data.Platform;
import tech.tagline.trevor.api.data.User;
import tech.tagline.trevor.api.database.Database;
import tech.tagline.trevor.api.database.DatabaseConnection;
import tech.tagline.trevor.api.network.payload.ConnectPayload;
import tech.tagline.trevor.api.network.payload.DisconnectPayload;
import tech.tagline.trevor.api.network.payload.NetworkPayload;
import tech.tagline.trevor.api.network.payload.ServerChangePayload;
import tech.tagline.trevor.api.database.DatabaseProxy;
import tech.tagline.trevor.api.util.Keys;
import tech.tagline.trevor.common.util.Protocol;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class DatabaseProxyImpl implements DatabaseProxy {

  private final Platform platform;
  private final Database database;
  private final Gson gson;

  private final String instance;

  public DatabaseProxyImpl(Platform platform, Database database, Gson gson) {
    this.platform = platform;
    this.database = database;
    this.gson = gson;

    this.instance = platform.getInstanceConfiguration().getID();
  }

  @Override
  public CompletableFuture<ConnectResult> onPlayerConnect(User user) {
    return database.open().thenApply(connection -> {
      try {
        if (!connection.isOnline(user)) {
          ConnectPayload payload = ConnectPayload.of(instance, user.uuid(), user.address());

          connection.create(user);
          post(Keys.CHANNEL_DATA.of(), connection, payload);

          return ConnectResult.allow();
        }
        return ConnectResult.deny("&cYou are already logged in.");
      } catch (CompletionException exception) {
        return ConnectResult.deny("&cAn error occurred, please try again.");
      }
    });
  }

  @Override
  public void onPlayerDisconnect(User user) {
    long timestamp = System.currentTimeMillis();
    database.open().thenAccept(connection -> {
      DisconnectPayload payload = DisconnectPayload.of(instance, user.uuid(), timestamp);

      connection.destroy(user.uuid());
      post(Keys.CHANNEL_DATA.of(), connection, payload);
    });
  }

  @Override
  public void onPlayerServerChange(User user, String server, String previousServer) {
   database.open().thenAccept(connection -> {
     ServerChangePayload payload =
             ServerChangePayload.of(instance, user.uuid(), server, previousServer);

     connection.setServer(user, server);
     post(Keys.CHANNEL_DATA.of(), connection, payload);
   });
  }

  @Override
  public void onNetworkIntercom(String channel, String message) {
    NetworkPayload<?> payload = Protocol.deserialize(message, gson);
    if (payload != null) {
      payload.process(platform.getEventProcessor()).post();
    }
  }

  @Override
  public void post(String channel, NetworkPayload<?> payload) {
    database.open().thenAccept(connection -> post(channel, connection, payload));
  }

  @Override
  public void post(String channel, DatabaseConnection connection, NetworkPayload<?> payload) {
    connection.publish(channel, Protocol.serialize(payload, gson));
  }
}
