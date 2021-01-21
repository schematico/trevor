package co.schemati.trevor.api.network.event;

import co.schemati.trevor.api.network.payload.ConnectPayload;
import co.schemati.trevor.api.network.payload.DisconnectPayload;
import co.schemati.trevor.api.network.payload.NetworkPayload;
import co.schemati.trevor.api.network.payload.ServerChangePayload;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * Represents the {@link co.schemati.trevor.api.data.Platform}'s event processor. The
 * {@link co.schemati.trevor.api.TrevorAPI} uses this to notify the platform implementation that
 * it has received a {@link NetworkPayload}.
 */
public interface EventProcessor {

  /**
   * Notify the {@link co.schemati.trevor.api.data.Platform} of a {@link ConnectPayload}.
   *
   * @param payload the payload
   * @param <T> the platform specific implementation of {@link NetworkConnectEvent}
   *
   * @return the event action
   */
  <T extends NetworkConnectEvent> EventAction<T> onConnect(ConnectPayload payload);

  /**
   * Notify the {@link co.schemati.trevor.api.data.Platform} of a {@link DisconnectPayload}.
   *
   * @param payload the payload
   * @param <T> the platform specific implementation of {@link NetworkDisconnectEvent}
   *
   * @return the event action
   */
  <T extends NetworkDisconnectEvent> EventAction<T> onDisconnect(DisconnectPayload payload);

  /**
   * Notify the {@link co.schemati.trevor.api.data.Platform} of a {@link ServerChangePayload}.
   *
   * @param payload the payload
   * @param <T> the platform specific implementation of {@link NetworkServerChangeEvent}
   *
   * @return the event action
   */
  <T extends NetworkServerChangeEvent> EventAction<T> onServerChange(ServerChangePayload payload);

  /**
   * Notify the {@link co.schemati.trevor.api.data.Platform} of a generic {@link NetworkPayload}.
   *
   * @param payload the payload
   * @param <T> the platform specific implementation of {@link NetworkIntercomEvent}
   *
   * @return the event action
   */
  <T extends NetworkIntercomEvent> EventAction<T> onMessage(NetworkPayload<?> payload);

  /**
   * Represents a wrapped instance of an event.
   *
   * @param <T> the platform specific implementation of {@link NetworkEvent}
   */
  class EventAction<T extends NetworkEvent> {

    private final T event;
    private final Function<T, CompletableFuture<T>> call;

    /**
     * Constructs a new EventAction.
     *
     * @param event the event
     * @param call the {@link CompletableFuture} callback
     */
    public EventAction(T event, Function<T, CompletableFuture<T>> call) {
      this.event = event;
      this.call = call;
    }

    /**
     * Returns the wrapped generic event.
     *
     * @return the event
     */
    public T getEvent() {
      return event;
    }

    /**
     * Calls the {@link CompletableFuture} callback using the wrapped event.
     *
     * @return the event
     */
    public CompletableFuture<T> post() {
      return call.apply(event);
    }
  }
}
