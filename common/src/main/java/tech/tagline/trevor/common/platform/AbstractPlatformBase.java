package tech.tagline.trevor.common.platform;

import ninja.leaping.configurate.ConfigurationNode;
import tech.tagline.trevor.api.data.Platform;
import tech.tagline.trevor.api.database.DatabaseConfiguration;
import tech.tagline.trevor.api.instance.InstanceConfiguration;
import tech.tagline.trevor.common.api.io.Configuration;
import tech.tagline.trevor.common.database.redis.RedisConfiguration;

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

  // Implement simply for the purpose of replacing values
  @Override
  public void log(String message, Object... values) {
    for (int i = 0; i < values.length; i++) {
      Object value = values[i];
      message = message.replace("{" + i + "}", value != null ? value.toString() : "null");
    }
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
