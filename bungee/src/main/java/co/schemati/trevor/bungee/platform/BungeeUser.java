package co.schemati.trevor.bungee.platform;

import co.schemati.trevor.api.data.User;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class BungeeUser extends User {

  public BungeeUser(ProxiedPlayer player) {
    super(player.getUniqueId(),
            player.getName(),
            player.getPendingConnection().getSocketAddress().toString());
  }

  public BungeeUser(UUID uuid, String name, String address) {
    super(uuid, name, address);
  }
}
