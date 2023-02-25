package sortj;

import java.util.List;

public final class Element {
  final List<String> text;
  int end;
  String[] subtext;
  String key;

  private int skipBlanks(int i) {
    while (i < text.size() && text.get(i).isBlank()) i++;
    return i;
  }

  public Element(List<String> text, int dent, int i) {
    this.text = text;

    // skip leading blank lines
    i = skipBlanks(i);

    // end of file
    if (i == text.size()) {
      end = i;
      return;
    }

    // end of section
    if (text.get(i).strip().equals("// END")) {
      if (Etc.indent(text, i) != dent) throw new IndentException(i);
      end = i;
      return;
    }
    if (Etc.indent(text, i) < dent) {
      if (!text.get(i).strip().startsWith("}")) throw new IndentException(i);
      end = i;
      return;
    }

    // there is still at least one element to read in this section
    var j = skipBlanks(i);

    end = j;
  }
}
