package tech.tagline.trevor.common.platform;

import tech.tagline.trevor.api.event.*;

import java.util.UUID;
import java.util.function.Consumer;

public interface EventProcessor {

  <T extends NetworkConnectEvent> EventAction<T> onConnect(UUID uuid);

  <T extends NetworkConnectEvent> EventAction<T> onDisconnect(UUID uuid);

  <T extends NetworkConnectEvent> EventAction<T> onServerChange(UUID uuid, String server,
                                                                 String previousServer);

  <T extends NetworkConnectEvent> EventAction<T> onMessage(String channel, String message);

  class EventAction<T extends NetworkEvent> {

    private final T event;
    private final Consumer<T> postCall;

    public EventAction(T event, Consumer<T> postCall) {
      this.event = event;
      this.postCall = postCall;
    }

    public T getEvent() {
      return null;
    }

    public void post() {
      postCall.accept(event);
    }
  }
}
