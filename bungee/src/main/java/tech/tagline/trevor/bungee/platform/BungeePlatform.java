package tech.tagline.trevor.bungee.platform;

import tech.tagline.trevor.api.database.DatabaseConfiguration;
import tech.tagline.trevor.bungee.TrevorBungee;
import tech.tagline.trevor.api.instance.InstanceConfiguration;
import tech.tagline.trevor.api.network.event.EventProcessor;
import tech.tagline.trevor.api.data.Platform;
import tech.tagline.trevor.common.database.redis.RedisConfiguration;
import tech.tagline.trevor.api.util.FileIO;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class BungeePlatform implements Platform {

  private final TrevorBungee plugin;

  private Configuration fileConfiguration;
  private InstanceConfiguration instanceConfiguration;
  private RedisConfiguration databaseConfiguration;

  private BungeeEventProcessor eventProcessor;

  public BungeePlatform(TrevorBungee plugin) {
    this.plugin = plugin;
  }

  public boolean init() {
    createFileConfiguration();
    if (fileConfiguration == null) {
      return false;
    }

    createInstanceConfig(fileConfiguration.getSection("instance"));
    createDatabaseConfig(fileConfiguration.getSection("redis"));

    this.eventProcessor = new BungeeEventProcessor(plugin);

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
    return plugin.getProxy().getConfig().isOnlineMode();
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
    File folder = plugin.getDataFolder();
    File file = new File(folder, "config.yml");

    FileIO.create(folder, true);
    FileIO.create(file, false);

    try {
      this.fileConfiguration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
    } catch (IOException exception) {
      exception.printStackTrace();
    }
  }

  private void createInstanceConfig(Configuration config) {
    String id = config.getString("id");

    this.instanceConfiguration = new InstanceConfiguration(id);
  }

  private void createDatabaseConfig(Configuration config) {
    String address = config.getString("address");
    short port = config.getShort("port");
    String password = config.getString("password");
    int maxConnections = config.getInt("max-connections");
    int timeout = config.getInt("timeout");
    boolean useSSL = config.getBoolean("use-ssl");

    this.databaseConfiguration =
            new RedisConfiguration(address, port, password, maxConnections, useSSL, timeout);
  }
}
