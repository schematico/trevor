package tech.tagline.trevor.common.platform;

import tech.tagline.trevor.api.event.*;

import java.util.UUID;

public interface EventProcessor {

  <T extends NetworkConnectEvent> EventAction<T> onConnect(UUID uuid);

  <T extends NetworkConnectEvent> EventAction<T> onDisconnect(UUID uuid);

  <T extends NetworkConnectEvent> EventAction<T> onServerChange(UUID uuid, String server,
                                                                 String previousServer);

  <T extends NetworkConnectEvent> EventAction<T> onMessage(String channel, String message);

  interface EventAction<T extends NetworkEvent> {

    T getEvent();

    void post();
  }
}
