package tech.tagline.trevor.common.impl;

import redis.clients.jedis.Jedis;
import tech.tagline.trevor.api.Keys;
import tech.tagline.trevor.api.TrevorApi;
import tech.tagline.trevor.common.TrevorCommon;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class TrevorApiImpl implements TrevorApi {

  private final TrevorCommon common;

  public TrevorApiImpl(TrevorCommon common) {
    this.common = common;
  }

  @Override
  public CompletableFuture<Optional<String>> getAddress(UUID uuid) {
    return wrap(resource ->
            resource.hget(Keys.PLAYER_DATA.with(uuid), "address"));
  }

  @Override
  public CompletableFuture<Optional<String>> getCurrentInstance(UUID uuid) {
    return wrap(resource ->
            resource.hget(Keys.PLAYER_DATA.with(uuid), "instance"));
  }

  @Override
  public CompletableFuture<Optional<String>> getCurrentServer(UUID uuid) {
    return wrap(resource ->
            resource.hget(Keys.PLAYER_DATA.with(uuid), "server"));
  }

  @Override
  public CompletableFuture<Boolean> isOnline(UUID uuid) {
    return getLastOnline(uuid).thenApply(v -> v.orElse(-1L) == 0);
  }

  @Override
  public CompletableFuture<Optional<Long>> getLastOnline(UUID uuid) {
    return wrap(resource ->
            Long.parseLong(resource.hget(Keys.PLAYER_DATA.with(uuid), "lastOnline")));
  }

  private <T> CompletableFuture<Optional<T>> wrap(final Function<Jedis, T> supplier) {
    CompletableFuture<Optional<T>> future = new CompletableFuture<>();

    common.getExecutor().submit(() -> {
      try (Jedis resource = common.getPool().getResource()) {
        future.complete(Optional.ofNullable(supplier.apply(resource)));
      }
    });

    return future;
  }
}
