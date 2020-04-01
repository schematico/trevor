package tech.tagline.trevor.api.event;

import java.util.UUID;

public class NetworkServerChangeEvent implements NetworkEvent {

  private final UUID uuid;
  private final String serverName;
  private final String previousServerName;

  public NetworkServerChangeEvent(UUID uuid, String serverName, String previousServerName) {
    this.uuid = uuid;
    this.serverName= serverName;
    this.previousServerName = previousServerName;
  }

  public UUID getUUID() {
    return uuid;
  }

  public String getServerName() {
    return serverName;
  }

  public String getPreviousServerName() {
    return previousServerName;
  }
}
