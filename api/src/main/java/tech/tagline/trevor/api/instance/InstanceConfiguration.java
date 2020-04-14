package tech.tagline.trevor.api.config;

public class InstanceConfiguration {

  private final String instanceID;

  public InstanceConfiguration(String instanceID) {
    this.instanceID = instanceID;
  }

  public String getInstanceID() {
    return instanceID;
  }
}
