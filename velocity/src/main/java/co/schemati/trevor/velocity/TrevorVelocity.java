package co.schemati.trevor.velocity;

import co.schemati.trevor.velocity.platform.VelocityPlatform;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;
import co.schemati.trevor.common.TrevorCommon;
import co.schemati.trevor.velocity.platform.VelocityListener;

import java.nio.file.Path;

@Plugin(id = "trevor")
public class TrevorVelocity {

  private TrevorCommon common;

  private VelocityPlatform platform;

  @Inject
  private ProxyServer proxy;

  @Inject
  private Logger logger;

  @Inject
  @DataDirectory
  private Path dataFolder;

  public TrevorVelocity() {
    System.out.println("Trevor construct");
  }

  @Subscribe
  public void onProxyStart(ProxyInitializeEvent event) {
    this.platform = new VelocityPlatform(this);

    this.common = new TrevorCommon(platform);

    if (!platform.init()) {
      platform.log("Trevor failed to load platform... Shutting down.");
      return;
    }

    if (!common.load()) {
      platform.log("Trevor failed to load... Shutting down.");
      return;
    }

    if (!common.start()) {
      platform.log("Trevor failed to start... Shutting down.");
      return;
    }

    proxy.getEventManager().register(this, new VelocityListener(this));
  }

  @Subscribe
  public void onProxyShutdown(ProxyShutdownEvent event) {
    common.stop();
  }

  public ProxyServer getProxy() {
    return proxy;
  }

  public Path getDataFolder() {
    return dataFolder;
  }

  public TrevorCommon getCommon() {
    return common;
  }

  public Logger getLogger() {
    return logger;
  }
}
