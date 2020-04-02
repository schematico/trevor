package tech.tagline.trevor.common.util;

import java.util.List;

public class RedisIO {

  public static long getTime(List<String> list) {
    return Long.parseLong(list.get(0));
  }
}
