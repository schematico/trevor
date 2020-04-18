package tech.tagline.trevor.api.network.payload;

import tech.tagline.trevor.api.network.event.EventProcessor;
import tech.tagline.trevor.api.network.event.NetworkServerChangeEvent;

import java.util.UUID;

public class ServerChangePayload extends OwnedPayload {

  private final String server;
  private final String previousServer;

  protected ServerChangePayload(String source, UUID uuid, String server, String previousServer) {
    super(Content.SERVERCHANGE, source, uuid);

    this.server = server;
    this.previousServer = previousServer;
  }

  public String getServer() {
    return server;
  }

  public String getPreviousServer() {
    return previousServer;
  }

  @Override
  public EventProcessor.EventAction<NetworkServerChangeEvent> process(EventProcessor processor) {
    return processor.onServerChange(this);
  }

  public static ServerChangePayload of(String source, UUID uuid, String server,
                                       String previousServer) {
    return new ServerChangePayload(source, uuid, server, previousServer);
  }
}
