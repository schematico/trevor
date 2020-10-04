package tech.tagline.trevor.api.network.event;

import tech.tagline.trevor.api.network.payload.*;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public interface EventProcessor {

  <T extends NetworkConnectEvent> EventAction<T> onConnect(ConnectPayload payload);

  <T extends NetworkDisconnectEvent> EventAction<T> onDisconnect(DisconnectPayload payload);

  <T extends NetworkServerChangeEvent> EventAction<T> onServerChange(ServerChangePayload payload);

  <T extends NetworkIntercomEvent> EventAction<T> onMessage(NetworkPayload<?> payload);

  class EventAction<T extends NetworkEvent> {

    private final T event;
    private final Function<T, CompletableFuture<T>> call;

    public EventAction(T event, Function<T, CompletableFuture<T>> call) {
      this.event = event;
      this.call = call;
    }

    public T getEvent() {
      return null;
    }

    public CompletableFuture<T> post() {
      return call.apply(event);
    }
  }
}
