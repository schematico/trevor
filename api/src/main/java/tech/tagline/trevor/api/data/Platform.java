package tech.tagline.trevor.api.data;

import tech.tagline.trevor.api.instance.InstanceConfiguration;
import tech.tagline.trevor.api.database.DatabaseConfiguration;
import tech.tagline.trevor.api.network.event.EventProcessor;

public interface Platform {

  InstanceConfiguration getInstanceConfiguration();

  DatabaseConfiguration getDatabaseConfiguration();

  EventProcessor getEventProcessor();

  boolean isOnlineMode();

  void log(String message, Object... values);
}
