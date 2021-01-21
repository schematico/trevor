package co.schemati.trevor.api.database;

/**
 * Represents the intercom used to communicate across the network.
 */
public interface DatabaseIntercom extends Runnable {

  /**
   * Subscribes the {@link DatabaseIntercom} to the provided channel.
   *
   * @param channel the channel
   */
  void add(String... channel);

  /**
   * Unsubscribes the {@link DatabaseIntercom} to the provided channel.
   *
   * @param channel the channel
   */
  void remove(String... channel);

  /**
   * Closes the intercom connection. Exactly how the intercom references are destroyed, or
   * for how long they are still available, is implementation specific. It's best to assume all
   * {@link DatabaseIntercom} operations are unsafe after executing kill().
   */
  void kill();
}
