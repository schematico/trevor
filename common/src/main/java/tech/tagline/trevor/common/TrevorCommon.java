package tech.tagline.trevor.common;

import com.google.gson.Gson;
import tech.tagline.trevor.api.database.DatabaseConnection;
import tech.tagline.trevor.api.network.payload.DisconnectPayload;
import tech.tagline.trevor.api.instance.InstanceData;
import tech.tagline.trevor.api.database.Database;
import tech.tagline.trevor.common.proxy.DatabaseProxyImpl;
import tech.tagline.trevor.api.data.Platform;

import java.util.UUID;
import java.util.concurrent.*;

public class TrevorCommon {

  private final Platform platform;

  private ScheduledExecutorService executor;
  private Gson gson;

  private Database database;
  private DatabaseProxyImpl proxy;

  private InstanceData data;

  public TrevorCommon(Platform platform) {
    this.platform = platform;
  }

  public boolean load() {
    // TODO: Verify instance configuration values before pool creation

    this.executor = Executors.newScheduledThreadPool(8);
    this.gson = new Gson();

    this.data = new InstanceData();

    this.database = platform.getDatabaseConfiguration().create(platform, proxy, data);

    this.proxy = new DatabaseProxyImpl(platform, database, gson);

    return true;
  }

  public boolean start() {
    database.init();

    // Test connection and perform heartbeat
    DatabaseConnection connection = database.open().join();
    if (connection.isInstanceAlive()) {
      // TODO: Shutdown and inform console
      return false;
    }

    database.init();

    return true;
  }

  public boolean stop() {
    if (database != null) {
      DatabaseConnection connection = database.open().join();

      connection.deleteHeartbeat();

      if (connection.getNetworkPlayerCount() > 0) {
        connection.getNetworkPlayers().forEach(uuid -> {
          DisconnectPayload payload = connection.destroy(UUID.fromString(uuid));

          connection.publish(gson.toJson(payload));
        });
      }

      database.kill();
    }
    return true;
  }

  public InstanceData getInstanceData() {
    return data;
  }

  public Platform getPlatform() {
    return platform;
  }

  public Database getDatabase() {
    return database;
  }

  public DatabaseProxyImpl getDatabaseProxy() {
    return proxy;
  }
}
