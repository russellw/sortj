package sortj;

import java.util.List;

public final class Element {
  final int end;
  final String[] subtext;
  final String key;

  public Element(List<String> text, int dent, int i) {
    // skip leading blank lines
    while (i < text.size() && text.get(i).isBlank()) i++;

    // end of file
    if (i == text.size()) {
      end = i;
      subtext = null;
      key = null;
      return;
    }

    // end of section
    if (text.get(i).strip().equals("// END")) {
      if (Etc.indent(i, text.get(i)) != dent) throw new IndentException(i);
      end = i;
      subtext = null;
      key = null;
      return;
    }
    if (Etc.indent(i, text.get(i)) < dent) {
      if (!text.get(i).strip().startsWith("}")) throw new IndentException(i);
      end = i;
      subtext = null;
      key = null;
      return;
    }

    // there is still at least one element to read in this section
    var j = i;

    end = j;
    subtext = null;
    key = null;
  }
}
