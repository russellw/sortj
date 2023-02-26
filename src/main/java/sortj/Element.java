package sortj;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class Element {
  private static final Pattern[] PATTERNS =
      new Pattern[] {
        Pattern.compile(".*\\W+(\\w+)\\s*=.*"),
        Pattern.compile(".*\\W+(\\w+)\\(.*"),
        Pattern.compile(".*\\W+(\\w+).*"),
      };

  // the location of this element in the text
  final int start;

  // the index just after the last line of this element.
  // it is filled in even if there wasn't actually an element to read
  final int end;

  // the portion of the text that comprises this element.
  // if subtext is null, there wasn't another element to read
  final List<String> subtext;

  // cache
  private String key;

  String key() {
    if (key != null) return key;
    for (var i = 0; ; i++) {
      var s = subtext.get(i);
      s = s.strip();
      if (s.startsWith("//") || s.startsWith("@")) continue;
      for (var p : PATTERNS) {
        var m = p.matcher(s);
        if (m.matches()) {
          key = m.group(1) + '\t' + s;
          return key;
        }
      }
      throw new SyntaxException(start + i);
    }
  }

  public Element(List<String> text, int dent, int i) {
    // skip leading blank lines
    i = Etc.skipBlanks(text, i);
    start = i;

    // dedent means end of block
    if (Etc.indent(text, i) < dent) {
      if (i < text.size() && !Etc.reallyStartsWith(text, i, "}")) throw new IndentException(i);
      end = i;
      subtext = null;
      return;
    }

    // so does explicit end marker
    if (text.get(i).strip().equals("// END")) {
      if (Etc.indent(text, i) != dent) throw new IndentException(i);
      end = i;
      subtext = null;
      return;
    }

    // leading comments and annotations are part of the element
    var j = i;
    while (Etc.reallyStartsWith(text, j, "//") || Etc.reallyStartsWith(text, j, "@")) {
      if (Etc.indent(text, j) != dent) throw new IndentException(j);
      j++;
    }

    // the element body
    if (Etc.indent(text, j) != dent) throw new IndentException(j);
    var start = j;
    do j++;
    while (Etc.indent(text, j) > dent);

    // a closing brace at the same column is part of the element
    if (Etc.indent(text, j) == dent && Etc.reallyStartsWith(text, j, "}")) {
      if (!text.get(start).contains("{")) throw new IndentException(i);
      j++;
    }

    // trailing blank lines are part of the text, but not this element
    end = j;
    while (text.get(j - 1).isBlank()) j--;

    // element text
    subtext = text.subList(i, j);
  }

  static List<String> cat(List<Element> elements) {
    var v = new ArrayList<String>();
    for (var e : elements) v.addAll(e.subtext);
    return v;
  }
}
