package co.schemati.trevor.velocity.platform;

import co.schemati.trevor.api.network.event.EventProcessor;
import co.schemati.trevor.api.util.Strings;
import co.schemati.trevor.common.platform.AbstractPlatformBase;
import co.schemati.trevor.velocity.TrevorVelocity;

public class VelocityPlatform extends AbstractPlatformBase {

  private final TrevorVelocity plugin;

  private VelocityEventProcessor eventProcessor;

  public VelocityPlatform(TrevorVelocity plugin) {
    super(plugin.getDataFolder().toFile());

    this.plugin = plugin;
  }

  public boolean init() {
    if (!super.init()) {
      return false;
    }

    this.eventProcessor = new VelocityEventProcessor(plugin);

    return true;
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
    plugin.getLogger().info(Strings.format(message, values));
  }
}
