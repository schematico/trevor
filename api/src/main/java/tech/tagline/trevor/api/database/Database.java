package tech.tagline.trevor.api.database;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public interface Database {

  void init();

  void beat();

  CompletableFuture<DatabaseConnection> open();

  DatabaseIntercom getIntercom();

  ExecutorService getExecutor();

  void kill();
}
