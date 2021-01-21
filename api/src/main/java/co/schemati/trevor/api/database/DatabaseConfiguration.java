package co.schemati.trevor.api.database;

import co.schemati.trevor.api.data.Platform;
import com.google.gson.Gson;
import co.schemati.trevor.api.instance.InstanceData;

/**
 * Represents the configuration used to construct the database reference.
 */
public interface DatabaseConfiguration {

  /**
   * Creates a {@link Database} reference using this {@link DatabaseConfiguration}.
   *
   * @param platform the trevor platform
   * @param data the trevor instance configuration
   * @param gson an instance of {@link Gson} to perform serialization
   *
   * @return the database reference
   */
  Database create(Platform platform, InstanceData data, Gson gson);
}
