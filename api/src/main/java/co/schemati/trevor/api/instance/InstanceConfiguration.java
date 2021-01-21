package co.schemati.trevor.api.instance;

/**
 * Represents the instance configuration values.
 */
public class InstanceConfiguration {

  private final String id;

  /**
   * Construct a new InstanceConfiguration.
   *
   * @param id the instance id
   */
  public InstanceConfiguration(String id) {
    this.id = id;
  }

  /**
   * Returns the instance's id.
   *
   * <br>
   *
   * The id is used to register the instance in the remote database, so it must be unique.
   *
   * @return the id
   */
  public String getID() {
    return id;
  }
}
