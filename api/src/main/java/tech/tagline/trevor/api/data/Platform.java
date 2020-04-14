package tech.tagline.trevor.common.platform;

import tech.tagline.trevor.api.event.EventProcessor;
import tech.tagline.trevor.common.api.config.InstanceConfiguration;
import tech.tagline.trevor.common.api.database.DatabaseConfiguration;
import tech.tagline.trevor.common.api.database.redis.RedisConfiguration;

public interface Platform {

  InstanceConfiguration getInstanceConfiguration();

  DatabaseConfiguration getDatabaseConfiguration();

  RedisConfiguration getRedisConfiguration();

  EventProcessor getEventProcessor();

  boolean isOnlineMode();

  void log(String message, Object... values);
}
