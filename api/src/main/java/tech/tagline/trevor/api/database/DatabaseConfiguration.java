package tech.tagline.trevor.common.api.database;

import tech.tagline.trevor.api.event.EventProcessor;
import tech.tagline.trevor.api.data.InstanceData;
import tech.tagline.trevor.common.proxy.DatabaseProxy;

public interface DatabaseConfiguration {

  Database create(String instance, DatabaseProxy proxy, EventProcessor processor,
                  InstanceData data);
}
