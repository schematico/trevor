package co.schemati.trevor.bungee;

import co.schemati.trevor.bungee.platform.BungeeListener;
import co.schemati.trevor.bungee.platform.BungeePlatform;
import co.schemati.trevor.common.TrevorCommon;
import net.md_5.bungee.api.plugin.Plugin;

public class TrevorBungee extends Plugin {

  private BungeePlatform platform;
  private TrevorCommon common;

  @Override
  public void onLoad() {
    this.platform = new BungeePlatform(this);
    this.common = new TrevorCommon(platform);

    platform.init();

    common.load();
  }

  @Override
  public void onEnable() {
    getProxy().getPluginManager().registerListener(this, new BungeeListener(this));

    common.start();
  }

  @Override
  public void onDisable() {
    common.stop();
  }

  public BungeePlatform getPlatform() {
    return platform;
  }

  public TrevorCommon getCommon() {
    return common;
  }
}
