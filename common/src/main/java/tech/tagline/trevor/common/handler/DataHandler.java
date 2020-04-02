package tech.tagline.trevor.common.handler;

import redis.clients.jedis.Jedis;
import tech.tagline.trevor.api.Keys;
import tech.tagline.trevor.api.data.User;
import tech.tagline.trevor.api.data.payload.ConnectData;
import tech.tagline.trevor.api.data.payload.DisconnectData;
import tech.tagline.trevor.api.data.payload.IntercomPayload;
import tech.tagline.trevor.common.TrevorCommon;
import tech.tagline.trevor.common.platform.Platform;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DataHandler {

  private final TrevorCommon common;
  private final Platform platform;
  private final String instanceID;

  public DataHandler(TrevorCommon common) {
    this.common = common;
    this.platform = common.getPlatform();
    this.instanceID = platform.getInstanceConfiguration().getInstanceID();
  }

  public void create(User user, boolean post) {
    Jedis pipeline = null;

    Map<String, String> data = user.toDatabaseMap(instanceID);

    pipeline.hmset(Keys.PLAYER_DATA.with(user.getUUID().toString()), data);
    pipeline.sadd(Keys.INSTANCE_PLAYERS.with(instanceID), user.getUUID().toString());

    if (post) {
      IntercomPayload payload = ConnectData.craft(instanceID, user.getUUID(), user.getAddress());
      pipeline.publish(Keys.CHANNEL_DATA.of(), platform.toJson(payload));
    }
  }

  public void destroy(String uuid) {
    Jedis resource = null;

    long timestamp = System.currentTimeMillis();
    resource.srem(Keys.INSTANCE_PLAYERS.with(instanceID), uuid);
    resource.hdel(Keys.PLAYER_DATA.with(uuid), "server", "ip", "instance");
    resource.hset(Keys.PLAYER_DATA.with(uuid), "lastOnline", String.valueOf(timestamp));

    IntercomPayload payload = DisconnectData.craft(instanceID, UUID.fromString(uuid), timestamp);
    resource.publish(Keys.CHANNEL_DATA.of(), platform.toJson(payload));
  }
}
