package tech.tagline.trevor.common.util;

import java.io.File;

public class FileIO {

  public static boolean create(File file, boolean directory) {
    try {
      if (file == null) {
        throw new IllegalArgumentException("file is null");
      }

      if (!file.exists()) {
        if (directory) {
          file.mkdirs();
        } else {
          file.createNewFile();
        }
      }
      return true;
    } catch(Exception exception) {
      exception.printStackTrace();
      return false;
    }
  }
}
