package sortj;

import java.util.List;

public final class Etc {
  @SuppressWarnings("unused")
  static void dbg(Object a) {
    System.out.printf("%s: %s\n", Thread.currentThread().getStackTrace()[2], a);
  }

  static boolean reallyStartsWith(List<String> text, int i, String prefix) {
    if (i == text.size()) return false;
    return text.get(i).stripLeading().startsWith(prefix);
  }

  static int indent(List<String> text, int i) {
    // End of file is always the end of a block
    if (i == text.size()) return -1;
    var s = text.get(i);

    // in Java, blank line is never the end of a block
    if (s.isBlank()) return Integer.MAX_VALUE;

    var j = 0;
    while (s.charAt(j) == ' ') j++;

    // don't know what indentation a tab character would correspond to
    if (s.charAt(j) == '\t') throw new TabException(i);
    return j;
  }

  static String ext(String file) {
    var i = file.lastIndexOf('.');
    if (i < 0) return "";
    return file.substring(i + 1);
  }

  static int skipBlanks(List<String> text, int i) {
    while (i < text.size() && text.get(i).isBlank()) i++;
    return i;
  }
}
