package tech.tagline.trevor.api;


import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface TrevorApi {

  CompletableFuture<Optional<String>> getAddress(UUID uuid);

  CompletableFuture<Optional<String>> getCurrentInstance(UUID uuid);

  CompletableFuture<Optional<String>> getCurrentServer(UUID uuid);

  CompletableFuture<Boolean> isOnline(UUID uuid);

  CompletableFuture<Optional<Long>> getLastOnline(UUID uuid);
}
