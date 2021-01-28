package co.schemati.trevor.api.network.payload;

import co.schemati.trevor.api.network.event.EventProcessor;
import co.schemati.trevor.api.network.event.NetworkServerChangeEvent;

import java.util.UUID;

/**
 * Represents the payload for a {@link co.schemati.trevor.api.data.User} changing servers on the
 * network.
 */
public class ServerChangePayload extends OwnedPayload {

  private final String server;
  private final String previousServer;

  /**
   * Constructs a new ServerChangePayload.
   *
   * @param source the source
   * @param uuid the user uuid
   * @param server the new server
   * @param previousServer the previous server
   */
  protected ServerChangePayload(String source, UUID uuid, String server, String previousServer) {
    super(source, uuid);

    this.server = server;
    this.previousServer = previousServer;
  }

  /**
   * Returns the new server.
   *
   * @return the new server
   */
  public String server() {
    return server;
  }

  /**
   * Returns the previous server.
   *
   * @return the previous server
   */
  public String previousServer() {
    return previousServer;
  }

  @Override
  public EventProcessor.EventAction<NetworkServerChangeEvent> process(EventProcessor processor) {
    return processor.onServerChange(this);
  }

  /**
   * Wraps the {@link ServerChangePayload} constructor.
   *
   * @see ServerChangePayload#ServerChangePayload(String, UUID, String, String)
   *
   * @param source the source
   * @param uuid the user uuid
   * @param server the new server
   * @param previousServer the previous server
   *
   * @return the new {@link ServerChangePayload}
   */
  public static ServerChangePayload of(String source, UUID uuid, String server,
                                       String previousServer) {
    return new ServerChangePayload(source, uuid, server, previousServer);
  }
}
