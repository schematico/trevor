package co.schemati.trevor.common.database.redis;

import co.schemati.trevor.api.data.Platform;
import co.schemati.trevor.api.database.Database;
import co.schemati.trevor.api.database.DatabaseConnection;
import co.schemati.trevor.api.database.DatabaseIntercom;
import co.schemati.trevor.api.database.DatabaseProxy;
import co.schemati.trevor.api.instance.InstanceData;
import com.google.gson.Gson;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RedisDatabase implements Database {

  public static final String HEARTBEAT = "heartbeat";
  public static final String INSTANCE_PLAYERS = "instance:{}:players";
  public static final String SERVER_PLAYERS = "server:{}:players";
  public static final String PLAYER_DATA = "player:{}";

  private final Platform platform;
  private final String instance;
  private final InstanceData data;
  private final JedisPool pool;
  private final Gson gson;
  private final ScheduledExecutorService executor;

  private RedisIntercom intercom;
  private Future<?> heartbeat;

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
    boolean duplicate = open().thenApply(DatabaseConnection::isInstanceAlive).join();
    if (duplicate) {
      platform.log("Duplicate instance detected with instance id: {0}", instance);
      return false;
    }

    this.heartbeat = executor.scheduleAtFixedRate(this::beat, 5,5, TimeUnit.SECONDS);

    this.intercom = new RedisIntercom(platform, this, proxy, gson);

    intercom.init();

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
    if (heartbeat != null) {
      heartbeat.cancel(true);
    }

    intercom.kill();
  }

  protected Jedis getResource() {
    return pool.getResource();
  }
}
