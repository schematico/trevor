package tech.tagline.trevor.velocity.platform;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import tech.tagline.trevor.common.config.InstanceConfiguration;
import tech.tagline.trevor.common.config.RedisConfiguration;
import tech.tagline.trevor.common.platform.EventProcessor;
import tech.tagline.trevor.common.platform.Platform;
import tech.tagline.trevor.common.util.FileIO;
import tech.tagline.trevor.velocity.TrevorVelocity;

import java.io.File;
import java.io.IOException;

public class VelocityPlatform implements Platform {

  private final TrevorVelocity plugin;

  private File folder;
  private File file;
  private ConfigurationNode config;

  private VelocityEventProcessor eventProcessor;
  private InstanceConfiguration instanceConfiguration;
  private RedisConfiguration redisConfiguration;

  public VelocityPlatform(TrevorVelocity plugin) {
    this.plugin = plugin;
  }

  public boolean init() {
    this.folder = plugin.getDataFolder().toFile();
    this.file = new File(folder, "config.yml");

    FileIO.create(folder, true);
    FileIO.create(file, false);

    try {
      this.config = YAMLConfigurationLoader.builder().setFile(file).build().load();
    } catch (IOException exception) {
      exception.printStackTrace();
      return false;
    }

    this.eventProcessor = new VelocityEventProcessor(plugin);

    instance : {
      ConfigurationNode section = config.getNode("instance");

      String instanceID = section.getString("id");

      this.instanceConfiguration = new InstanceConfiguration(instanceID);
    }

    redis : {
      ConfigurationNode section = config.getNode("redis");

      String address = section.getNode("address").getString();
      short port = (short) section.getNode("port").getInt();
      String password = section.getNode("password").getString();
      int maxConnections = section.getNode("max-connections").getInt();
      int timeout = section.getNode("timeout").getInt();
      boolean useSSL = section.getNode("use-ssl").getBoolean();

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

  @Override
  public <T> T fromJson(String json, Class<T> clazz) {
    return TrevorVelocity.GSON.fromJson(json, clazz);
  }

  @Override
  public <T> String toJson(T value) {
    return TrevorVelocity.GSON.toJson(value);
  }

  @Override
  public EventProcessor getEventProcessor() {
    return eventProcessor;
  }
}
