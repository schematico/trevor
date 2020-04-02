package tech.tagline.trevor.common.platform;

import tech.tagline.trevor.api.event.NetworkEvent;
import tech.tagline.trevor.common.config.InstanceConfiguration;
import tech.tagline.trevor.common.config.RedisConfiguration;

public interface Platform {

  InstanceConfiguration getInstanceConfiguration();

  RedisConfiguration getRedisConfiguration();

  <T> T fromJson(String json, Class<T> clazz);

  <T> String toJson(T value);

  EventProcessor getEventProcessor();
}
