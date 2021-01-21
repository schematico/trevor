package co.schemati.trevor.api.instance;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents the dynamic data for the instance.
 */
public class InstanceData {

  private List<String> instances = new ArrayList<>();
  private final AtomicInteger playerCount = new AtomicInteger();

  /**
   * Returns the network's player count.
   *
   * @return the player count
   */
  public int getPlayerCount() {
    return playerCount.get();
  }

  /**
   * Returns the network's instances.
   *
   * @return the instances
   */
  public List<String> getInstances() {
    return instances;
  }

  /**
   * Updates the {@link InstanceData} variables.
   *
   * @param instances the network instances
   * @param playerCount the network player count
   */
  public void update(List<String> instances, int playerCount) {
    this.instances = instances;
    this.playerCount.set(playerCount);
  }
}