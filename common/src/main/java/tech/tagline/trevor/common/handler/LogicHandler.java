package tech.tagline.trevor.common.handler;

import redis.clients.jedis.Jedis;
import tech.tagline.trevor.api.Keys;
import tech.tagline.trevor.api.data.User;
import tech.tagline.trevor.common.TrevorCommon;

import java.util.Optional;

public class LogicHandler {

  private final TrevorCommon common;

  public LogicHandler(TrevorCommon common) {
    this.common = common;
  }

  public ConnectResult onPlayerConnect(User user) {
    String instanceID = common.getPlatform().getInstanceConfiguration().getInstanceID();
    try (Jedis resource = common.getPool().getResource()) {
      for (String instance : common.getDataHandler().getInstanceData().getInstances()) {
        if (resource.sismember(Keys.INSTANCE_PLAYERS.with(instanceID), user.getUUID().toString())) {
          // Deny join since player is online somewhere else
          // TODO: Allow customization of this message
          return ConnectResult.deny("&cYou are already logged in.");
        }

        common.getDataHandler().create(user, true);
        return ConnectResult.allow();
      }
    }
    // TODO: Allow customization of this message
    return ConnectResult.deny("&cAn error occured, please try again.");
  }

  public void onPlayerDisconnect(User user) {
    common.getDataHandler().destroy(user.getUUID().toString(), true);
  }

  public void onPlayerServerChange(User user, String server, String previousServer) {
    common.getDataHandler().setServer(user, server, previousServer, true);
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
