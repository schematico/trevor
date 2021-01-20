package co.schemati.trevor.api.data;

import co.schemati.trevor.api.instance.InstanceConfiguration;
import co.schemati.trevor.api.database.DatabaseConfiguration;
import co.schemati.trevor.api.network.event.EventProcessor;
import co.schemati.trevor.api.util.Strings;

/**
 * Each individual software implementation must implement a {@link Platform} in order for the
 * common implementation to access the necessary methods to allow the common package to function
 */
public interface Platform {

  /**
   * The platform initializes all platform specific utilities
   *
   * @return init success
   */
  boolean init();

  /**
   * The platform's {@link InstanceConfiguration}
   * 
   * @return the instance configuration
   */
  InstanceConfiguration getInstanceConfiguration();

  /**
   * The platform's {@link DatabaseConfiguration}
   *
   * @return the database configuration
   */
  DatabaseConfiguration getDatabaseConfiguration();

  /**
   * The platform's {@link EventProcessor}
   *
   * @return the event processor
   */
  EventProcessor getEventProcessor();

  /**
   * If the platform is in online mode
   *
   * @return online mode
   */
  boolean isOnlineMode();

  /**
   * Log information to the console with variable replacements.
   *
   * @param message the message
   * @param values the values to be replaced
   *
   * @see Strings#format
   */
  void log(String message, Object... values);
}
