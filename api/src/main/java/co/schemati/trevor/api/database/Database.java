package co.schemati.trevor.api.database;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public interface Database {

  boolean init(DatabaseProxy proxy);

  void beat();

  CompletableFuture<DatabaseConnection> open();

  DatabaseIntercom getIntercom();

  ExecutorService getExecutor();

  void kill();
}
