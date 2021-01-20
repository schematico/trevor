package co.schemati.trevor.common;

import com.google.gson.Gson;
import co.schemati.trevor.api.TrevorAPI;
import co.schemati.trevor.api.TrevorService;
import co.schemati.trevor.api.database.DatabaseConnection;
import co.schemati.trevor.api.network.payload.DisconnectPayload;
import co.schemati.trevor.api.instance.InstanceData;
import co.schemati.trevor.api.database.Database;
import co.schemati.trevor.api.util.Keys;
import co.schemati.trevor.common.proxy.DatabaseProxyImpl;
import co.schemati.trevor.api.data.Platform;
import co.schemati.trevor.common.util.Protocol;

import java.util.UUID;

public class TrevorCommon implements TrevorAPI {

  private final Platform platform;

  private Gson gson;

  private Database database;
  private DatabaseProxyImpl proxy;

  private InstanceData data;

  public TrevorCommon(Platform platform) {
    this.platform = platform;
  }

  public boolean load() {
    TrevorService.setAPI(this);

    // TODO: Verify instance configuration values before pool creation
    this.gson = new Gson();

    this.data = new InstanceData();

    this.database = platform.getDatabaseConfiguration().create(platform, data, gson);

    this.proxy = new DatabaseProxyImpl(platform, database, gson);

    return true;
  }

  public boolean start() {
    if (!database.init(proxy)) {
      return false;
    }

    return true;
  }

  public boolean stop() {
    if (database != null) {
      DatabaseConnection connection = database.open().join();

      connection.deleteHeartbeat();

      if (connection.getNetworkPlayerCount() > 0) {
        connection.getNetworkPlayers().forEach(uuid -> {
          DisconnectPayload payload = connection.destroy(UUID.fromString(uuid));

          connection.publish(Keys.CHANNEL_DATA.of(), Protocol.serialize(payload, gson));
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
