package co.schemati.trevor.velocity.platform;

import co.schemati.trevor.api.database.DatabaseProxy;
import co.schemati.trevor.common.proxy.DatabaseProxyImpl;
import co.schemati.trevor.velocity.TrevorVelocity;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class VelocityListener {

  private final TrevorVelocity plugin;
  private final DatabaseProxy proxy;

  public VelocityListener(TrevorVelocity plugin) {
    this.plugin = plugin;
    this.proxy = plugin.getCommon().getDatabaseProxy();
  }

  @Subscribe
  public void onPlayerConnect(LoginEvent event) {
    Player player = event.getPlayer();
    VelocityUser user = new VelocityUser(player);

    DatabaseProxyImpl.ConnectResult result = proxy.onPlayerConnect(user).join();

    if (!result.isAllowed()) {
      result.getMessage().ifPresent(message ->
              event.setResult(
                ResultedEvent.ComponentResult.denied(serialize(message))
              )
      );
    }
  }

  @Subscribe
  public void onPlayerDisconnect(DisconnectEvent event) {
    Player player = event.getPlayer();

    proxy.users().get(player.getUniqueId()).ifPresent(proxy::onPlayerDisconnect);
  }

  @Subscribe
  public void onPlayerServerChange(ServerConnectedEvent event) {
    Player player = event.getPlayer();
    String server = event.getServer().getServerInfo().getName();
    RegisteredServer previous = event.getPreviousServer().orElse(null);
    String previousName = previous != null ? previous.getServerInfo().getName() : "";

    proxy.users().get(player.getUniqueId()).ifPresent(user ->
      proxy.onPlayerServerChange(user, server, previousName)
    );
  }

  @Subscribe(order = PostOrder.LAST)
  public void onServerPing(ProxyPingEvent event) {
    event.setPing(
            event.getPing().asBuilder().onlinePlayers(
                    plugin.getCommon().getInstanceData().getPlayerCount()
            ).build()
    );
  }

  private Component serialize(String text) {
    return LegacyComponentSerializer.legacyAmpersand().deserialize(text);
  }
}
