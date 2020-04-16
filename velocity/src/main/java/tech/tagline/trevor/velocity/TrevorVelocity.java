package tech.tagline.trevor.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;
import tech.tagline.trevor.common.TrevorCommon;
import tech.tagline.trevor.velocity.platform.VelocityListener;
import tech.tagline.trevor.velocity.platform.VelocityPlatform;

import java.nio.file.Path;

@Plugin(id = "trevor")
public class TrevorVelocity {

  private TrevorCommon trevor;

  private VelocityPlatform platform;

  @Inject
  private ProxyServer proxy;

  @Inject
  private Logger logger;

  @Inject
  @DataDirectory
  private Path dataFolder;

  @Subscribe
  public void onProxyStart(ProxyInitializeEvent event) {
    this.platform = new VelocityPlatform(this);

    this.trevor = new TrevorCommon(platform);

    platform.init();

    trevor.load();

    proxy.getEventManager().register(this, new VelocityListener(this));

    trevor.start();
  }

  @Subscribe
  public void onProxyShutdown(ProxyShutdownEvent event) {
    trevor.stop();
  }

  public ProxyServer getProxy() {
    return proxy;
  }

  public Path getDataFolder() {
    return dataFolder;
  }

  public TrevorCommon getTrevor() {
    return trevor;
  }

  public Logger getLogger() {
    return logger;
  }
}
