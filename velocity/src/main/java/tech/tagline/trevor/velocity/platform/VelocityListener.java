package tech.tagline.trevor.velocity.platform;

import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import net.kyori.text.Component;
import net.kyori.text.serializer.legacy.LegacyComponentSerializer;
import tech.tagline.trevor.common.proxy.DatabaseProxyImpl;
import tech.tagline.trevor.velocity.TrevorVelocity;

public class VelocityListener {

  private final TrevorVelocity plugin;

  public VelocityListener(TrevorVelocity plugin) {
    this.plugin = plugin;
  }

  @Subscribe
  public void onPlayerConnect(LoginEvent event) {
    Player player = event.getPlayer();

    // TODO: Maybe keep a map of platform users
    VelocityUser user = new VelocityUser(player);

    DatabaseProxyImpl.ConnectResult result =
            plugin.getTrevor().getDatabaseProxy().onPlayerConnect(user);

    if (!result.isAllowed()) {
      event.setResult(
              ResultedEvent.ComponentResult.denied(serialize(result.getMessage().toString())));
    }
  }

  @Subscribe
  public void onPlayerDisconnect(DisconnectEvent event) {
    Player player = event.getPlayer();
    // TODO: Maybe keep a map of platform users
    VelocityUser user = new VelocityUser(player);

    plugin.getTrevor().getDatabaseProxy().onPlayerDisconnect(user);
  }

  @Subscribe
  public void onPlayerServerChange(ServerConnectedEvent event) {
    String server = event.getServer().getServerInfo().getName();
    String previousServer = "";
    if (event.getPreviousServer().isPresent()) {
      previousServer = event.getPreviousServer().get().getServerInfo().getName();
    }

    Player player = event.getPlayer();
    // TODO: Maybe keep a map of platform users
    VelocityUser user = new VelocityUser(player);

    plugin.getTrevor().getDatabaseProxy().onPlayerServerChange(user, server, previousServer);
  }

  private Component serialize(String text) {
    return LegacyComponentSerializer.legacy().deserialize(text, '&');
  }
}
