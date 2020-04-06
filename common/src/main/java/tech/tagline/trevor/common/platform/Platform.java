package tech.tagline.trevor.common.platform;

import tech.tagline.trevor.api.event.EventProcessor;
import tech.tagline.trevor.common.config.InstanceConfiguration;
import tech.tagline.trevor.common.config.RedisConfiguration;

public interface Platform {

  InstanceConfiguration getInstanceConfiguration();

  RedisConfiguration getRedisConfiguration();

  EventProcessor getEventProcessor();

  boolean isOnlineMode();

  void log(String message, Object... values);
}
