package co.schemati.trevor.api;

/**
 * Represents a place to retrieve the provided {@link TrevorAPI}.
 */
public class TrevorService {

  private static TrevorAPI api;
  private static final Object lock = new Object();

  /**
   * Returns the provided {@link TrevorAPI}.
   *
   * @return the trevor api
   */
  public static TrevorAPI getAPI() {
    return api;
  }

  /**
   * Provides an implementation of {@link TrevorAPI}.
   *
   * <br>
   *
   * Only 1 implementation can be registered; otherwise, an {@link IllegalStateException} will be
   * thrown.
   *
   * @param api the api
   */
  public static void setAPI(TrevorAPI api) {
    if (TrevorService.api == null) {
      synchronized (lock) {
        if (TrevorService.api == null) {
          TrevorService.api = api;
          return;
        }
      }
    }
    throw new IllegalStateException("Singleton TrevorAPI cannot be redefined.");
  }
}
