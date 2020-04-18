package tech.tagline.trevor.common.database.redis;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import tech.tagline.trevor.api.data.Platform;
import tech.tagline.trevor.api.database.DatabaseConfiguration;
import tech.tagline.trevor.api.database.DatabaseProxy;
import tech.tagline.trevor.api.network.event.EventProcessor;
import tech.tagline.trevor.api.instance.InstanceData;

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
  public RedisDatabase create(Platform platform, DatabaseProxy proxy, InstanceData data) {
    JedisPoolConfig config = new JedisPoolConfig();

    config.setMaxTotal(maxConnections);

    JedisPool pool = new JedisPool(config, address, port, timeout, password, useSSL);

    return new RedisDatabase(platform, proxy, data, pool);
  }
}
