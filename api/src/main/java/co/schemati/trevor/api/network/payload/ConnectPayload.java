package co.schemati.trevor.api.network.payload;

import co.schemati.trevor.api.network.event.EventProcessor;
import co.schemati.trevor.api.network.event.NetworkConnectEvent;

import java.util.UUID;

public class ConnectPayload extends OwnedPayload {

  private String address;

  protected ConnectPayload(String source, UUID uuid, String address) {
    super(source, uuid);

    this.address = address;
  }

  public String address() {
    return address;
  }

  @Override
  public EventProcessor.EventAction<NetworkConnectEvent> process(EventProcessor processor) {
    return processor.onConnect(this);
  }

  public static ConnectPayload of(String source, UUID uuid, String address) {
    return new ConnectPayload(source, uuid, address);
  }
}
