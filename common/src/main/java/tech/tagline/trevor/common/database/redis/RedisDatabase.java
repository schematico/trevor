package tech.tagline.trevor.common.database.redis;

import com.google.gson.Gson;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;
import tech.tagline.trevor.api.data.Platform;
import tech.tagline.trevor.api.database.Database;
import tech.tagline.trevor.api.database.DatabaseConnection;
import tech.tagline.trevor.api.database.DatabaseIntercom;
import tech.tagline.trevor.api.database.DatabaseProxy;
import tech.tagline.trevor.api.instance.InstanceData;
import tech.tagline.trevor.common.TrevorCommon;

import java.util.concurrent.*;

public class RedisDatabase implements Database {

  private final Platform platform;
  private final String instance;
  private final InstanceData data;
  private final JedisPool pool;
  private final Gson gson;
  private final ScheduledExecutorService executor;

  private RedisIntercom intercom;
  private Future<?> intercomTask;
  private Future<?> heartbeatTask;

  public RedisDatabase(Platform platform, InstanceData data, JedisPool pool, Gson gson) {
    this.platform = platform;
    this.instance = platform.getInstanceConfiguration().getID();
    this.data = data;
    this.pool = pool;
    this.gson = gson;
    this.executor = Executors.newScheduledThreadPool(8);
  }

  @Override
  public boolean init(DatabaseProxy proxy) {
    DatabaseConnection connection = open().join();
    if (connection.isInstanceAlive()) {
      platform.log("Duplicate instance detected with instance id: {0}", instance);
      return false;
    }

    this.intercom = new RedisIntercom(platform, this, proxy, gson);

    this.intercomTask = executor.submit(intercom);

    this.heartbeatTask =
            executor.scheduleAtFixedRate(this::beat, 5,5, TimeUnit.SECONDS);

    return true;
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
      try (Jedis resource = getResource()) {
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
    if (heartbeatTask != null) {
      heartbeatTask.cancel(true);
      intercomTask.cancel(true);
    }
  }

  protected Jedis getResource() {
    return pool.getResource();
  }
}
