package co.schemati.trevor.bungee.platform;

import co.schemati.trevor.bungee.TrevorBungee;
import co.schemati.trevor.api.util.Strings;
import co.schemati.trevor.api.network.event.EventProcessor;
import co.schemati.trevor.common.platform.AbstractPlatformBase;

public class BungeePlatform extends AbstractPlatformBase {

  private final TrevorBungee plugin;

  private BungeeEventProcessor eventProcessor;

  public BungeePlatform(TrevorBungee plugin) {
    super(plugin.getDataFolder());

    this.plugin = plugin;
  }

  public boolean init() {
    if (!super.init()) {
      return false;
    }

    this.eventProcessor = new BungeeEventProcessor(plugin);

    return true;
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
    plugin.getLogger().info(Strings.format(message, values));
  }
}
