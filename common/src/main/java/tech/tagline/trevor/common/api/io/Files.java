package tech.tagline.trevor.common.api.io;

import tech.tagline.trevor.common.TrevorCommon;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Files {

  public static File getOrCreate(File folder, String name) {
    File directory = getOrCreateDirectory(folder.getParentFile(), folder.getName());
    File file = new File(directory, name);
    if (!file.exists()) {
      try {
        try (InputStream input = TrevorCommon.class.getResourceAsStream("/" + name)) {
          if (input != null) {
            java.nio.file.Files.copy(input, file.toPath());
          } else {
            file.createNewFile();
          }
        }
      } catch (IOException exception) {
        exception.printStackTrace();
      }
    }
    return file;
  }

  public static File getOrCreateDirectory(File folder, String name) {
    File file = new File(folder, name);
    if (file.exists()) {
      if (!file.isDirectory()) {
        file.delete();
        file.mkdirs();
      }
    } else {
      file.mkdirs();
    }
    return file;
  }

  public static String getStrippedName(File file) {
    return file.getName().replaceFirst("[.][^.]+$", "");
  }
}
