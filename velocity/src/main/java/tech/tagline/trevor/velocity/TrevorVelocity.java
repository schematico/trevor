package tech.tagline.trevor.velocity;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;

import java.nio.file.Path;

@Plugin(id = "trevor")
public class TrevorVelocity {

  public static final Gson GSON = new Gson();

  @Inject
  private ProxyServer proxy;

  @Inject
  @DataDirectory
  private Path dataFolder;

  public ProxyServer getProxy() {
    return proxy;
  }

  public Path getDataFolder() {
    return dataFolder;
  }
}
