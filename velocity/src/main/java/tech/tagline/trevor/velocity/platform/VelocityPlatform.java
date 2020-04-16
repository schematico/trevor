package tech.tagline.trevor.velocity.platform;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import tech.tagline.trevor.api.instance.InstanceConfiguration;
import tech.tagline.trevor.api.database.DatabaseConfiguration;
import tech.tagline.trevor.common.database.redis.RedisConfiguration;
import tech.tagline.trevor.api.network.event.EventProcessor;
import tech.tagline.trevor.api.data.Platform;
import tech.tagline.trevor.api.util.FileIO;
import tech.tagline.trevor.velocity.TrevorVelocity;

import javax.security.auth.login.Configuration;
import java.io.File;
import java.io.IOException;

public class VelocityPlatform implements Platform {

  private final TrevorVelocity plugin;

  private ConfigurationNode fileConfiguration;
  private InstanceConfiguration instanceConfiguration;
  private DatabaseConfiguration databaseConfiguration;

  private VelocityEventProcessor eventProcessor;

  public VelocityPlatform(TrevorVelocity plugin) {
    this.plugin = plugin;
  }

  public boolean init() {
    createFileConfiguration();
    if (fileConfiguration == null) {
      return false;
    }

    createInstanceConfig(fileConfiguration.getNode("instance"));
    createDatabaseConfig(fileConfiguration.getNode("redis"));

    this.eventProcessor = new VelocityEventProcessor(plugin);

    return true;
  }

  @Override
  public InstanceConfiguration getInstanceConfiguration() {
    return instanceConfiguration;
  }

  @Override
  public DatabaseConfiguration getDatabaseConfiguration() {
    return databaseConfiguration;
  }

  @Override
  public EventProcessor getEventProcessor() {
    return eventProcessor;
  }

  @Override
  public boolean isOnlineMode() {
    return plugin.getProxy().getConfiguration().isOnlineMode();
  }

  @Override
  public void log(String message, Object... values) {
    for (int i = 0; i < values.length; i++) {
      Object value = values[i];
      message = message.replace("{" + i + "}", value != null ? value.toString() : "null");
    }

    plugin.getLogger().info(message);
  }

  private void createFileConfiguration() {
    File folder = plugin.getDataFolder().toFile();
    File file = new File(folder, "config.yml");

    FileIO.create(folder, true);
    FileIO.create(file, false);

    try {
      this.fileConfiguration = YAMLConfigurationLoader.builder().setFile(file).build().load();
    } catch (IOException exception) {
      exception.printStackTrace();
    }
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
}
