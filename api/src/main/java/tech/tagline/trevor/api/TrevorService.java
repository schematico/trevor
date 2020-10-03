package tech.tagline.trevor.api;

public class TrevorService {

  private static TrevorAPI api;
  private static final Object lock = new Object();

  public static TrevorAPI getAPI() {
    return api;
  }

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
