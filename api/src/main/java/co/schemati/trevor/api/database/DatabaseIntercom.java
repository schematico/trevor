package co.schemati.trevor.api.database;

public interface DatabaseIntercom extends Runnable {

  void add(String... channel);

  void remove(String... channel);

  void destroy();
}
