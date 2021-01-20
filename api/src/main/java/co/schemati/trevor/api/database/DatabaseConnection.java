package co.schemati.trevor.api.database;

import co.schemati.trevor.api.data.User;
import co.schemati.trevor.api.instance.InstanceData;
import co.schemati.trevor.api.network.payload.DisconnectPayload;

import java.io.Closeable;
import java.util.Set;
import java.util.UUID;

public interface DatabaseConnection extends Closeable {

  void beat();

  void update(InstanceData data);

  boolean create(User user);

  DisconnectPayload destroy(UUID uuid);

  void setServer(User user, String server);

  void publish(String channel, String message);

  boolean isOnline(User user);

  boolean isInstanceAlive();

  Set<String> getNetworkPlayers();

  long getNetworkPlayerCount();

  void deleteHeartbeat();
}
