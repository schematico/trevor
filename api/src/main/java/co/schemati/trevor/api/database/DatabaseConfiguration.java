package co.schemati.trevor.api.database;

import co.schemati.trevor.api.data.Platform;
import com.google.gson.Gson;
import co.schemati.trevor.api.instance.InstanceData;

public interface DatabaseConfiguration {

  Database create(Platform platform, InstanceData data, Gson gson);
}
