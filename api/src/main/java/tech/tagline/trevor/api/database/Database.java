package tech.tagline.trevor.common.api.database;

import tech.tagline.trevor.api.data.User;
import tech.tagline.trevor.api.data.payload.DisconnectPayload;
import tech.tagline.trevor.api.data.InstanceData;

import java.io.Closeable;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public interface Database {

  void init();

  CompletableFuture<Connection> open();

  Database.Intercom getIntercom();

  ExecutorService getExecutor();

  void kill();

  interface Connection extends Closeable {

    void beat(long timestamp);

    void update(long timestamp, InstanceData data);

    boolean create(User user);

    DisconnectPayload destroy(UUID uuid);

    void setServer(User user, String server);

    void publish(String message);

    boolean isOnline(User user);

    boolean isInstanceAlive(long timestamp);

    Set<String> getNetworkPlayers();

    long getNetworkPlayerCount();

    void deleteHeartbeat();
  }

  interface Intercom extends Runnable {

  }
}
