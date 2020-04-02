package tech.tagline.trevor.velocity.platform;

import tech.tagline.trevor.common.platform.EventProcessor;
import tech.tagline.trevor.velocity.TrevorVelocity;
import tech.tagline.trevor.velocity.platform.event.*;

import java.util.UUID;
import java.util.function.Consumer;

public class VelocityEventProcessor implements EventProcessor {

  private final TrevorVelocity plugin;

  public VelocityEventProcessor(TrevorVelocity plugin) {
    this.plugin = plugin;
  }

  @Override
  public EventAction<VelocityNetworkConnectEvent> onConnect(UUID uuid) {
    return wrap(new VelocityNetworkConnectEvent(uuid));
  }

  @Override
  public EventAction<VelocityNetworkDisconnectEvent> onDisconnect(UUID uuid) {
    return wrap(new VelocityNetworkDisconnectEvent(uuid));
  }

  @Override
  public EventAction<VelocityNetworkServerChangeEvent> onServerChange(UUID uuid, String server,
                                                                     String previousServer) {
    return wrap(new VelocityNetworkServerChangeEvent(uuid, server, previousServer));
  }

  @Override
  public EventAction<VelocityNetworkMessageEvent> onMessage(String channel, String message) {
    return wrap(new VelocityNetworkMessageEvent(channel, message));
  }

  private <T extends VelocityNetworkEvent> EventAction<T> wrap(T event) {
    return new VelocityEventAction<T>(event, plugin.getProxy().getEventManager()::fireAndForget);
  }

  public class VelocityEventAction<T extends VelocityNetworkEvent> implements EventAction<T> {

    private final T event;
    private final Consumer<T> postCall;

    public VelocityEventAction(T event, Consumer<T> postCall) {
      this.event = event;
      this.postCall = postCall;
    }

    @Override
    public T getEvent() {
      return null;
    }

    @Override
    public void post() {
      postCall.accept(event);
    }
  }
}
