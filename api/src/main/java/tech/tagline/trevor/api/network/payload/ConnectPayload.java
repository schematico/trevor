package tech.tagline.trevor.api.network.payload;

import tech.tagline.trevor.api.network.event.EventProcessor;
import tech.tagline.trevor.api.network.event.NetworkConnectEvent;
import tech.tagline.trevor.api.network.event.NetworkEvent;

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
