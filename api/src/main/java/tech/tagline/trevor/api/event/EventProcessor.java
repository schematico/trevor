package tech.tagline.trevor.api.event;

import tech.tagline.trevor.api.data.payload.*;
import tech.tagline.trevor.api.event.*;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

public interface EventProcessor {

  <T extends NetworkConnectEvent> EventAction<T> onConnect(ConnectPayload payload);

  <T extends NetworkDisconnectEvent> EventAction<T> onDisconnect(DisconnectPayload payload);

  <T extends NetworkServerChangeEvent> EventAction<T> onServerChange(ServerChangePayload payload);

  <T extends NetworkIntercomEvent> EventAction<T> onMessage(String channel, String message);

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
