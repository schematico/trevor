package co.schemati.trevor.bungee.platform;

import co.schemati.trevor.bungee.TrevorBungee;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import co.schemati.trevor.common.proxy.DatabaseProxyImpl;

public class BungeeListener implements Listener {

  private final TrevorBungee plugin;

  public BungeeListener(TrevorBungee plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void onPlayerConnect(LoginEvent event) {
    PendingConnection connection = event.getConnection();

    // TODO: Maybe keep a map of platform users
    BungeeUser user = new BungeeUser(connection.getUniqueId(),
            connection.getSocketAddress().toString());

    DatabaseProxyImpl.ConnectResult result = plugin.getCommon()
            .getDatabaseProxy().onPlayerConnect(user);

    if (!result.isAllowed()) {
      event.setCancelled(true);

      result.getMessage().ifPresent(message -> event.setCancelReason(serialize(message)));
    }
  }

  @EventHandler
  public void onPlayerDisconnect(PlayerDisconnectEvent event) {
    ProxiedPlayer player = event.getPlayer();
    // TODO: Maybe keep a map of platform users
    BungeeUser user = new BungeeUser(player);

    plugin.getCommon().getDatabaseProxy().onPlayerDisconnect(user);
  }

  @EventHandler
  public void onPlayerServerChange(ServerConnectedEvent event) {
    String server = event.getServer().getInfo().getName();
    String previousServer = "";
    ProxiedPlayer player = event.getPlayer();
    if (player.getServer() != null) {
      previousServer = player.getServer().getInfo().getName();
    }

    // TODO: Maybe keep a map of platform users
    BungeeUser user = new BungeeUser(player);

    plugin.getCommon().getDatabaseProxy().onPlayerServerChange(user, server, previousServer);
  }

  @EventHandler
  public void onServerPing(ProxyPingEvent event) {
    event.getResponse().getPlayers().setOnline(
            plugin.getCommon().getInstanceData().getPlayerCount()
    );
  }

  private BaseComponent[] serialize(String text) {
   return TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', text));
  }
}
