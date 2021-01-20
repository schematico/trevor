package co.schemati.trevor.common.platform;

import co.schemati.trevor.common.database.redis.RedisConfiguration;
import ninja.leaping.configurate.ConfigurationNode;
import co.schemati.trevor.api.data.Platform;
import co.schemati.trevor.api.database.DatabaseConfiguration;
import co.schemati.trevor.api.instance.InstanceConfiguration;
import co.schemati.trevor.common.api.io.Configuration;

import java.io.File;

public abstract class AbstractPlatformBase implements Platform {

  private final File parent;

  protected Configuration config;
  private InstanceConfiguration instanceConfiguration;
  private DatabaseConfiguration databaseConfiguration;

  protected AbstractPlatformBase(File parent) {
    this.parent = parent;
  }

  @Override
  public boolean init() {
    this.config = Configuration.of(parent, "config.yml").build();

    createInstanceConfig(config.getNode("instance"));
    createDatabaseConfig(config.getNode("redis"));

    return true;
  }

  private void createInstanceConfig(ConfigurationNode node) {
    String id = node.getNode("id").getString();

    this.instanceConfiguration = new InstanceConfiguration(id);
  }

  private void createDatabaseConfig(ConfigurationNode node) {
    String address = node.getNode("address").getString();
    short port = (short) node.getNode("port").getInt();
    String password = node.getNode("password").getString();
    int maxConnections = node.getNode("max-connections").getInt();
    int timeout = node.getNode("timeout").getInt();
    boolean useSSL = node.getNode("use-ssl").getBoolean();

    this.databaseConfiguration =
            new RedisConfiguration(address, port, password, maxConnections, useSSL, timeout);
  }

  @Override
  public InstanceConfiguration getInstanceConfiguration() {
    return instanceConfiguration;
  }

  @Override
  public DatabaseConfiguration getDatabaseConfiguration() {
    return databaseConfiguration;
  }
}
