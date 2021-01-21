package co.schemati.trevor.api.util;

/**
 * A utility class for mutating a {@link String}.
 */
public class Strings {

  /**
   * Formats the provided text by replacing all instances of {@code { index }} with the values
   * returned by {@link Strings#toSafeString(Object)}. The text replacements are based on their
   * order in the values overload.
   *
   * @param text the text
   * @param values the replacement values
   *
   * @return the formatted string
   */
  public static String format(String text, Object... values) {
    for (int i = 0; i < values.length; i++) {
      text = text.replace("{" + i + "}", toSafeString(values[i]));
    }
    return text;
  }

  /**
   * Returns the value's {@link Object#toString()} value if present, otherwise {@code null}.
   *
   * @param value the value
   *
   * @return the value as a string
   */
  public static String toSafeString(Object value) {
    return value != null ? value.toString() : "null";
  }
}
