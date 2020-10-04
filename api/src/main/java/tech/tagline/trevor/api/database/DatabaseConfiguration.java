package tech.tagline.trevor.api.database;

import com.google.gson.Gson;
import tech.tagline.trevor.api.data.Platform;
import tech.tagline.trevor.api.instance.InstanceData;

public interface DatabaseConfiguration {

  Database create(Platform platform, InstanceData data, Gson gson);
}
