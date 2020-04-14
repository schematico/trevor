package tech.tagline.trevor.common.api.database.redis;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import tech.tagline.trevor.api.event.EventProcessor;
import tech.tagline.trevor.api.data.InstanceData;
import tech.tagline.trevor.common.api.database.DatabaseConfiguration;
import tech.tagline.trevor.common.proxy.DatabaseProxy;

public class RedisConfiguration implements DatabaseConfiguration {

  private final String address;
  private final short port;
  private final String password;
  private final int maxConnections;
  private final boolean useSSL;
  private final int timeout;

  public RedisConfiguration(String address, short port, String password, int maxConnections,
                            boolean useSSL,
                            int timeout) {
    this.address = address;
    this.port = port;
    this.password = password;
    this.maxConnections = maxConnections;
    this.useSSL = useSSL;
    this.timeout = timeout;
  }

  @Override
  public RedisDatabase create(String instance, DatabaseProxy proxy, EventProcessor processor,
                              InstanceData data) {
    JedisPoolConfig config = new JedisPoolConfig();

    config.setMaxTotal(maxConnections);

    return new RedisDatabase(instance, proxy, processor, data,
            new JedisPool(config, address, port, timeout, password, useSSL));
  }
}
