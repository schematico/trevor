package tech.tagline.trevor.api;

import tech.tagline.trevor.api.data.Platform;
import tech.tagline.trevor.api.database.Database;
import tech.tagline.trevor.api.database.DatabaseProxy;
import tech.tagline.trevor.api.instance.InstanceData;

public interface TrevorAPI {

  InstanceData getInstanceData();

  Platform getPlatform();

  Database getDatabase();

  DatabaseProxy getDatabaseProxy();
}
