package tech.tagline.trevor.api.database;

import tech.tagline.trevor.api.data.Platform;
import tech.tagline.trevor.api.instance.InstanceData;

public interface DatabaseConfiguration {

  Database create(Platform platform, DatabaseProxy proxy, InstanceData data);
}
