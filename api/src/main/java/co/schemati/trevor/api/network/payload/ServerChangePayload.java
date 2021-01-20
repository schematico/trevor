package co.schemati.trevor.api.network.payload;

import co.schemati.trevor.api.network.event.EventProcessor;
import co.schemati.trevor.api.network.event.NetworkServerChangeEvent;

import java.util.UUID;

public class ServerChangePayload extends OwnedPayload {

  private final String server;
  private final String previousServer;

  protected ServerChangePayload(String source, UUID uuid, String server, String previousServer) {
    super(source, uuid);

    this.server = server;
    this.previousServer = previousServer;
  }

  public String server() {
    return server;
  }

  public String previousServer() {
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
