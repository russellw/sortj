package sortj;

import java.util.List;

public final class Etc {
  @SuppressWarnings("unused")
  static void dbg(Object a) {
    System.out.printf("%s: %s\n", Thread.currentThread().getStackTrace()[2], a);
  }

  static int indent(List<String> text, int i) {
    var s = text.get(i);
    var j = 0;
    while (s.charAt(j) == ' ') j++;
    if (s.charAt(j) == '\t') throw new TabException(i);
    return j;
  }

  static String ext(String file) {
    var i = file.lastIndexOf('.');
    if (i < 0) return "";
    return file.substring(i + 1);
  }
}
