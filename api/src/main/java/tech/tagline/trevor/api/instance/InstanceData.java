package tech.tagline.trevor.api.instance;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class InstanceData {

    private List<String> instances = new ArrayList<>();
    private final AtomicInteger playerCount = new AtomicInteger();

    public int getPlayerCount() {
      return playerCount.get();
    }

    public List<String> getInstances() {
      return instances;
    }

    public void update(List<String> instances, int playerCount) {
      this.instances = instances;
      this.playerCount.set(playerCount);
    }
  }