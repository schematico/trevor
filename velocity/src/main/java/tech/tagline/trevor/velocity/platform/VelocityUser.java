package tech.tagline.trevor.velocity.platform;

import com.velocitypowered.api.proxy.Player;
import tech.tagline.trevor.api.data.User;

import java.net.InetAddress;
import java.util.UUID;

public class VelocityUser extends User {

  public VelocityUser(Player player) {
    super(player.getUniqueId(), player.getRemoteAddress().getAddress().toString());
  }

  public VelocityUser(UUID uuid, String address) {
    super(uuid, address);
  }
}
