package tech.tagline.trevor.common.database.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;
import tech.tagline.trevor.api.data.Platform;
import tech.tagline.trevor.api.database.Database;
import tech.tagline.trevor.api.database.DatabaseConnection;
import tech.tagline.trevor.api.database.DatabaseIntercom;
import tech.tagline.trevor.api.database.DatabaseProxy;
import tech.tagline.trevor.api.network.event.EventProcessor;
import tech.tagline.trevor.api.instance.InstanceData;

import java.util.concurrent.*;

public class RedisDatabase implements Database {

  private final Platform platform;
  private final DatabaseProxy proxy;
  private final InstanceData data;
  private final JedisPool pool;
  private final ScheduledExecutorService executor;

  private RedisIntercom intercom;
  private Future<?> intercomTask;
  private Future<?> heartbeatTask;

  public RedisDatabase(Platform platform, DatabaseProxy proxy, InstanceData data, JedisPool pool) {
    this.platform = platform;
    this.proxy = proxy;
    this.data = data;
    this.pool = pool;
    this.executor = Executors.newScheduledThreadPool(8);
  }

  @Override
  public void init() {
    this.intercom = new RedisIntercom(platform, this,  proxy);

    this.intercomTask = executor.submit(intercom);

    this.heartbeatTask =
            executor.scheduleAtFixedRate(this::beat, 0,5, TimeUnit.SECONDS);
  }

  @Override
  public void beat() {
    open().thenAccept(connection -> {
      connection.beat();

      connection.update(data);
    });
  }

  @Override
  public CompletableFuture<DatabaseConnection> open() {
    CompletableFuture<DatabaseConnection> future = new CompletableFuture<>();

    executor.submit(() -> {
      try (Jedis resource = pool.getResource()) {
        future.complete(new RedisConnection(platform.getInstanceConfiguration().getID(), resource));
      } catch (JedisConnectionException exception) {
        future.completeExceptionally(exception);
      }
    });

    return future;
  }

  @Override
  public DatabaseIntercom getIntercom() {
    return intercom;
  }

  @Override
  public ExecutorService getExecutor() {
    return executor;
  }

  @Override
  public void kill() {
    heartbeatTask.cancel(true);
    intercomTask.cancel(true);
  }
}
