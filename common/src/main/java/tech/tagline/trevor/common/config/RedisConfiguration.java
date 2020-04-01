package tech.tagline.trevor.common.config;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisConfiguration {

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

  public String getAddress() {
    return address;
  }

  public short getPort() {
    return port;
  }

  public int getMaxConnections() {
    return maxConnections;
  }

  public boolean isUseSSL() {
    return useSSL;
  }

  public int getTimeout() {
    return timeout;
  }

  public JedisPool create() {
    JedisPoolConfig config = new JedisPoolConfig();

    config.setMaxTotal(maxConnections);

    return new JedisPool(config, address, port, timeout, password);
  }
}
