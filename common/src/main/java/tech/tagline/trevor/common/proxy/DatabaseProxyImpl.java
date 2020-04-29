package tech.tagline.trevor.common.proxy;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import tech.tagline.trevor.api.data.Platform;
import tech.tagline.trevor.api.data.User;
import tech.tagline.trevor.api.database.Database;
import tech.tagline.trevor.api.database.DatabaseConnection;
import tech.tagline.trevor.api.network.payload.ConnectPayload;
import tech.tagline.trevor.api.network.payload.DisconnectPayload;
import tech.tagline.trevor.api.network.payload.NetworkPayload;
import tech.tagline.trevor.api.network.payload.ServerChangePayload;
import tech.tagline.trevor.api.database.DatabaseProxy;

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
  public ConnectResult onPlayerConnect(User user) {
    try {
      DatabaseConnection connection = database.open().join();
      if (!connection.isOnline(user)) {
        ConnectPayload payload = ConnectPayload.of(instance, user.getUUID(), user.getAddress());

        connection.create(user);
        connection.publish(gson.toJson(payload));

        return ConnectResult.allow();
      }
      return ConnectResult.deny("&cYou are already logged in.");
    } catch (CompletionException exception) {
      exception.printStackTrace();
    }
    return ConnectResult.deny("&cAn error occurred, please try again.");
  }

  @Override
  public void onPlayerDisconnect(User user) {
    long timestamp = System.currentTimeMillis();
    database.open().thenAccept(connection -> {
      DisconnectPayload payload = DisconnectPayload.of(instance, user.getUUID(), timestamp);

      connection.destroy(user.getUUID());
      connection.publish(gson.toJson(payload));
    });
  }

  @Override
  public void onPlayerServerChange(User user, String server, String previousServer) {
   database.open().thenAccept(connection -> {
     ServerChangePayload payload =
             ServerChangePayload.of(instance, user.getUUID(), server, previousServer);

     connection.setServer(user, server);
     connection.publish(gson.toJson(payload));
   });
  }

  @Override
  public void onNetworkIntercom(String channel, String message) {
    // TODO: Figure out why JsonParser#parseString() doesn't work since this method is deprecated
    JsonObject json = new JsonParser().parse(message).getAsJsonObject();

    String contentRaw = json.get("content").getAsString();
    try {
      NetworkPayload.Content content = NetworkPayload.Content.valueOf(contentRaw);

      NetworkPayload payload = gson.fromJson(message, content.getContentClass());
      if (!instance.equals(payload.getSource())) {
        payload.process(platform.getEventProcessor()).post();
      }
    } catch (IllegalArgumentException exception) {
      exception.printStackTrace();
    }
  }
}
