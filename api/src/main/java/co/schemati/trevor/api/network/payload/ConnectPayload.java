package co.schemati.trevor.api.network.payload;

import co.schemati.trevor.api.network.event.EventProcessor;
import co.schemati.trevor.api.network.event.NetworkConnectEvent;

import java.util.UUID;

/**
 * Represents the payload for a {@link co.schemati.trevor.api.data.User} connecting to the network.
 */
public class ConnectPayload extends OwnedPayload {

  private String name;
  private String address;

  /**
   * Constructs a new ConnectPayload.
   *
   * @deprecated see {@link #ConnectPayload(String, UUID, String, String)}.
   *
   * @param source the instance source
   * @param uuid the user uuid
   * @param address the user address
   */
  @Deprecated
  protected ConnectPayload(String source, UUID uuid, String address) {
    super(source, uuid);

    this.name = null;
    this.address = address;
  }

  /**
   * Constructs a new ConnectPayload.
   *
   * @param source the instance source
   * @param uuid the user uuid
   * @param name the user name
   * @param address the user address
   */
  protected ConnectPayload(String source, UUID uuid, String name, String address) {
    super(source, uuid);

    this.name = name;
    this.address = address;
  }

  /**
   * The {@link co.schemati.trevor.api.data.User}'s name.
   *
   * @return the name
   */
  public String name() {
    return name;
  }

  /**
   * The {@link co.schemati.trevor.api.data.User}'s address.
   *
   * @return the address
   */
  public String address() {
    return address;
  }

  @Override
  public EventProcessor.EventAction<NetworkConnectEvent> process(EventProcessor processor) {
    return processor.onConnect(this);
  }

  /**
   * Wraps the {@link ConnectPayload} constructor.
   *
   * @see ConnectPayload#ConnectPayload(String, UUID, String, String)
   *
   * @deprecated see {@link ConnectPayload#of(String, UUID, String, String)}
   *
   * @param source the instance source
   * @param uuid the user uuid
   * @param address the user address
   *
   * @return the new {@link ConnectPayload}
   */
  @Deprecated
  public static ConnectPayload of(String source, UUID uuid, String address) {
    return new ConnectPayload(source, uuid, null, address);
  }

  /**
   * Wraps the {@link ConnectPayload} constructor.
   *
   * @see ConnectPayload#ConnectPayload(String, UUID, String, String)
   *
   * @param source the instance source
   * @param uuid the user uuid
   * @param name the user name
   * @param address the user address
   *
   * @return the new {@link ConnectPayload}
   */
  public static ConnectPayload of(String source, UUID uuid, String name, String address) {
    return new ConnectPayload(source, uuid, name, address);
  }
}
