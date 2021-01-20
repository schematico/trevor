package co.schemati.trevor.api.util;

public class Strings {

  public static String format(String text, Object... values) {
    for (int i = 0; i < values.length; i++) {
      text = text.replace("{" + i + "}", toSafeString(values[i]));
    }
    return text;
  }

  public static String toSafeString(Object value) {
    return value != null ? value.toString() : "null";
  }
}
