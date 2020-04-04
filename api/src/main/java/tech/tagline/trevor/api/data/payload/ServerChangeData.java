package tech.tagline.trevor.api.data.payload;

import tech.tagline.trevor.api.data.User;

public class ServerChangeData {

  private final String server;
  private final String previousServer;

  public ServerChangeData(String server, String previousServer) {
    this.server = server;
    this.previousServer = previousServer;
  }

  public String getServer() {
    return server;
  }

  public String getPreviousServer() {
    return previousServer;
  }

  public static IntercomPayload craft(String instanceID, User user, String server,
                                      String previousServer) {
    return new IntercomPayload(IntercomPayload.Type.SERVERCHANGE, user.getUUID(), instanceID,
            new ServerChangeData(server, previousServer));
  }
}
