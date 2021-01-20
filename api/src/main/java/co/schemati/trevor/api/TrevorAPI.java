package co.schemati.trevor.api;

import co.schemati.trevor.api.data.Platform;
import co.schemati.trevor.api.database.Database;
import co.schemati.trevor.api.database.DatabaseProxy;
import co.schemati.trevor.api.instance.InstanceData;

public interface TrevorAPI {

  InstanceData getInstanceData();

  Platform getPlatform();

  Database getDatabase();

  DatabaseProxy getDatabaseProxy();
}
