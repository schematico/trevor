package tech.tagline.trevor.common.data;

import java.util.concurrent.atomic.AtomicInteger;

public class InstanceData {

  private final AtomicInteger playerCount = new AtomicInteger();

  public int getPlayerCount() {
    return playerCount.get();
  }

  public void setPlayerCount(int value) {
    playerCount.set(value);
  }
}
