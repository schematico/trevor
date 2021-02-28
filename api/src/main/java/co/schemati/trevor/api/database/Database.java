package co.schemati.trevor.api.database;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

/**
 * Represents a remote database connection.
 */
public interface Database {

  /**
   * The channel to post payload data to.
   */
  String CHANNEL_DATA = "trevor:data";
  /**
   * The channel to post information to a specific instance.
   *
   * NOTE: Not yet implemented.
   */
  String CHANNEL_INSTANCE = "trevor:{}";

  /**
   * Initializes the {@link Database} using the provided {@link DatabaseProxy}.
   *
   * @param proxy the database proxy
   * @return init success
   */
  boolean init(DatabaseProxy proxy);

  /**
   * @see DatabaseConnection#beat()
   */
  void beat();

  /**
   * Open a connection to the remote database. Exactly how the connection is opened/returned is
   * implementation specific.
   *
   * @return future wrapper database connection
   */
  CompletableFuture<DatabaseConnection> open();

  /**
   * Returns the {@link DatabaseIntercom}.
   *
   * @return the database intercom
   */
  DatabaseIntercom getIntercom();

  /**
   * Returns the {@link ExecutorService} the database uses to perform connection operations.
   *
   * @return the executor service
   */
  ExecutorService getExecutor();

  /**
   * Closes the remote database connection. Exactly how the database references are destroyed, or
   * for how long they are still available, is implementation specific. It's best to assume all
   * {@link Database} operations are unsafe after executing kill().
   */
  void kill();
}
