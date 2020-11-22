package tech.tagline.trevor.common.api.concurrent;

import tech.tagline.trevor.api.database.Database;
import tech.tagline.trevor.api.database.DatabaseConnection;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;

public class ConnectionQueue {

  private final Database database;

  private final BlockingQueue<CompletableFuture<DatabaseConnection>> queue;

  private boolean running = true;

  public ConnectionQueue(Database database) {
    this.database = database;
    this.queue = new LinkedBlockingQueue<>();

    database.getExecutor().submit(this::work);
  }

  private void work() {
    DatabaseConnection connection = database.connect();
    while (running) {
      try {
        CompletableFuture<DatabaseConnection> future = queue.take();
        if (connection == null || !connection.isRunning()) {
          connection = database.connect();
        }

        future.complete(connection);
      } catch (InterruptedException exception) {
        exception.printStackTrace();
      }
    }
  }

  public CompletableFuture<DatabaseConnection> open() {
    CompletableFuture<DatabaseConnection> future = new CompletableFuture<>();

    queue.offer(future);

    return future;
  }

  public void shutdown() {
    this.running = false;
  }
}
