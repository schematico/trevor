package tech.tagline.trevor.bungee;

import com.google.gson.Gson;
import tech.tagline.trevor.bungee.platform.BungeePlatform;
import tech.tagline.trevor.common.TrevorCommon;
import net.md_5.bungee.api.plugin.Plugin;

public class TrevorBungee extends Plugin {

  public static final Gson GSON = new Gson();

  private BungeePlatform platform;
  private TrevorCommon trevor;

  @Override
  public void onLoad() {
    this.platform = new BungeePlatform(this);
    this.trevor = new TrevorCommon(platform);

    platform.init();

    trevor.load();
  }

  @Override
  public void onEnable() {
    trevor.start();
  }

  @Override
  public void onDisable() {
    trevor.stop();
  }
}
