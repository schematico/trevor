package tech.tagline.trevor.common.api.database.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;
import tech.tagline.trevor.api.event.EventProcessor;
import tech.tagline.trevor.api.data.InstanceData;
import tech.tagline.trevor.common.api.database.Database;
import tech.tagline.trevor.common.proxy.DatabaseProxy;
import tech.tagline.trevor.common.task.HeartbeatTask;

import java.util.concurrent.*;

public class RedisDatabase implements Database {

  private final String instance;
  private final DatabaseProxy proxy;
  private final EventProcessor processor;
  private final InstanceData data;
  private final JedisPool pool;
  private final ScheduledExecutorService executor;

  private Intercom intercom;
  private Future<?> intercomTask;
  private Future<?> heartbeatTask;

  public RedisDatabase(String instance, DatabaseProxy proxy, EventProcessor processor,
                       InstanceData data, JedisPool pool) {
    this.instance = instance;
    this.proxy = proxy;
    this.processor = processor;
    this.data = data;
    this.pool = pool;
    this.executor = Executors.newScheduledThreadPool(8);
  }

  @Override
  public void init() {
    this.intercom = new RedisIntercom(this, proxy, processor);

    this.intercomTask = executor.submit(intercom);

    this.heartbeatTask = executor.submit(new HeartbeatTask(this, data));
  }

  @Override
  public CompletableFuture<Connection> open() {
    CompletableFuture<Connection> future = new CompletableFuture<>();

    executor.submit(() -> {
      try (Jedis resource = pool.getResource()) {
        future.complete(new RedisConnection(instance, resource));
      } catch (JedisConnectionException exception) {
        future.completeExceptionally(exception);
      }
    });

    return future;
  }

  @Override
  public Database.Intercom getIntercom() {
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
