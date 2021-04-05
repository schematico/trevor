package co.schemati.trevor.bungee.platform;

import co.schemati.trevor.api.database.DatabaseProxy;
import co.schemati.trevor.bungee.TrevorBungee;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class BungeeListener implements Listener {

  private final TrevorBungee plugin;
  private final DatabaseProxy proxy;

  public BungeeListener(TrevorBungee plugin) {
    this.plugin = plugin;
    this.proxy = plugin.getCommon().getDatabaseProxy();
  }

  @EventHandler
  public void onPlayerConnect(LoginEvent event) {
    PendingConnection connection = event.getConnection();

    BungeeUser user = new BungeeUser(connection.getUniqueId(),
            connection.getName(),
            connection.getSocketAddress().toString());

    event.registerIntent(plugin);

    proxy.onPlayerConnect(user).thenAccept(result -> {
      if (!result.isAllowed()) {
        event.setCancelled(true);
        result.getMessage().ifPresent(message -> event.setCancelReason(serialize(message)));
      }

      event.completeIntent(plugin);
    });
  }

  @EventHandler
  public void onPlayerDisconnect(PlayerDisconnectEvent event) {
    ProxiedPlayer player = event.getPlayer();

    proxy.users().get(player.getUniqueId()).ifPresent(proxy::onPlayerDisconnect);
  }

  @EventHandler
  public void onPlayerServerChange(ServerConnectedEvent event) {
    ProxiedPlayer player = event.getPlayer();
    String server = event.getServer().getInfo().getName();
    Server previous = player.getServer();
    String previousName = previous != null ? previous.getInfo().getName() : "";

    proxy.users().get(player.getUniqueId()).ifPresent(user ->
            proxy.onPlayerServerChange(user, server, previousName)
    );
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
