package tech.tagline.trevor.api.database;

import tech.tagline.trevor.api.data.User;
import tech.tagline.trevor.api.network.event.NetworkEvent;
import tech.tagline.trevor.api.network.payload.NetworkPayload;

import java.util.Optional;

public interface DatabaseProxy {
  void post(String channel, NetworkPayload event);

  void post(String channel, DatabaseConnection connection, NetworkPayload event);

  ConnectResult onPlayerConnect(User user);

  void onPlayerDisconnect(User user);

  void onPlayerServerChange(User user, String server, String previousServer);

  void onNetworkIntercom(String channel, String message);

  class ConnectResult {

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
