package co.schemati.trevor.api;

import co.schemati.trevor.api.data.Platform;
import co.schemati.trevor.api.database.Database;
import co.schemati.trevor.api.database.DatabaseProxy;
import co.schemati.trevor.api.instance.InstanceData;

/**
 * Represents an implementation of the api.
 */
public interface TrevorAPI {

  /**
   * Returns the api's {@link InstanceData}.
   *
   * @return the instance data
   */
  InstanceData getInstanceData();

  /**
   * Returns the api's {@link Platform}.
   *
   * @return the platform
   */
  Platform getPlatform();

  /**
   * Returns the api's {@link Database}.
   *
   * @return the database
   */
  Database getDatabase();

  /**
   * Returns the api's {@link DatabaseProxy}.
   *
   * @return the database proxy
   */
  DatabaseProxy getDatabaseProxy();
}
