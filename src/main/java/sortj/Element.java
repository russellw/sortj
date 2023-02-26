package sortj;

import java.util.List;
import java.util.regex.Pattern;

public final class Element {
  private static final Pattern[] PATTERNS =
      new Pattern[] {
        Pattern.compile(".*\\W+(\\w+)\\s*=.*"),
        Pattern.compile(".*\\W+(\\w+)\\(.*"),
        Pattern.compile(".*\\W+(\\w+).*"),
      };

  // the index just after the last line of this element.
  // it is filled in even if there wasn't actually an element to read
  int end;

  // the portion of the text that comprises this element.
  // an array rather than a list, to make sure we don't end up
  // with just a view on the original list, which would be invalid
  // as soon as the text starts being rearranged.
  // if subtext is null, there wasn't another element to read
  String[] subtext;

  // cache
  private String key;

  String key() {
    if (key != null) return key;
    for (var i = 0; ; i++) {
      var s = subtext[i];
      s = s.strip();
      if (s.startsWith("//") || s.startsWith("@")) continue;
      for (var p : PATTERNS) {
        var m = p.matcher(s);
        if (m.matches()) {
          key = m.group(1) + '\t' + s;
          return key;
        }
      }
      throw new SyntaxException(end - subtext.length + i);
    }
  }

  public Element(List<String> text, int dent, int i) {
    // skip leading blank lines
    i = Etc.skipBlanks(text, i);
    end = i;

    // dedent means end of block
    if (Etc.indent(text, i) < dent) {
      if (i < text.size() && !Etc.reallyStartsWith(text, i, "}")) throw new IndentException(i);
      return;
    }

    // so does explicit end marker
    if (text.get(i).strip().equals("// END")) {
      if (Etc.indent(text, i) != dent) throw new IndentException(i);
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
    this.end = j;
    while (text.get(j - 1).isBlank()) j--;

    // element text
    subtext = new String[j - i];
    for (var k = 0; k < subtext.length; k++) subtext[k] = text.get(i + k);
  }
}
