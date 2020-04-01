package tech.tagline.trevor.bungee.platform;

import tech.tagline.trevor.bungee.TrevorBungee;
import tech.tagline.trevor.common.config.InstanceConfiguration;
import tech.tagline.trevor.common.platform.Platform;
import tech.tagline.trevor.common.config.RedisConfiguration;
import tech.tagline.trevor.common.util.FileIO;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class BungeePlatform implements Platform {

  private final TrevorBungee plugin;

  private File folder;
  private File file;
  private Configuration config;

  private InstanceConfiguration instanceConfiguration;
  private RedisConfiguration redisConfiguration;
  private IOException exception;

  public BungeePlatform(TrevorBungee plugin) {
    this.plugin = plugin;
  }

  public boolean init() {
    this.folder = plugin.getDataFolder();
    this.file = new File(folder, "config.yml");

    FileIO.create(folder, true);
    FileIO.create(file, false);

    try {
      this.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
    } catch (IOException exception) {
      exception.printStackTrace();
      return false;
    }

    instance : {
      Configuration section = config.getSection("instance");

      int instanceID = section.getInt("id");

      this.instanceConfiguration = new InstanceConfiguration(instanceID);
    }

    redis : {
      Configuration section = config.getSection("redis");

      String address = section.getString("address");
      short port = section.getShort("port");
      String password = section.getString("password");
      int maxConnections = section.getInt("max-connections");
      int timeout = section.getInt("timeout");
      boolean useSSL = section.getBoolean("use-ssl");

      this.redisConfiguration = new RedisConfiguration(address, port, password, maxConnections,
              useSSL,
              timeout);
    }
    return true;
  }

  @Override
  public InstanceConfiguration getInstanceConfiguration() {
    return instanceConfiguration;
  }

  @Override
  public RedisConfiguration getRedisConfiguration() {
    return redisConfiguration;
  }
}
