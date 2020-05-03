package tech.tagline.trevor.bungee.platform;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import tech.tagline.trevor.api.data.User;

import java.util.UUID;

public class BungeeUser extends User {

  public BungeeUser(ProxiedPlayer player) {
    super(player.getUniqueId(),
            player.getPendingConnection().getSocketAddress().toString());
  }

  public BungeeUser(UUID uuid, String address) {
    super(uuid, address);
  }
}
