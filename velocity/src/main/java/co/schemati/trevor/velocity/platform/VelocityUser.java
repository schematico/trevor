package co.schemati.trevor.velocity.platform;

import com.velocitypowered.api.proxy.Player;
import co.schemati.trevor.api.data.User;

import java.util.UUID;

public class VelocityUser extends User {

  public VelocityUser(Player player) {
    super(player.getUniqueId(), player.getRemoteAddress().getAddress().toString());
  }

  public VelocityUser(UUID uuid, String address) {
    super(uuid, address);
  }
}
