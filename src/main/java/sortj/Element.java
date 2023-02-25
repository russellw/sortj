package sortj;

import java.util.List;

public final class Element {
  int j;
  String[] subtext;

  public Element(List<String> text, int dent, int i) {
    // skip leading blank lines
    i = Etc.skipBlanks(text, i);
    j = i;

    // end of block
    if (Etc.indent(text, i) < dent) {
      if (i < text.size() && !text.get(i).strip().startsWith("}")) throw new IndentException(i);
      return;
    }
    if (text.get(i).strip().equals("// END")) {
      if (Etc.indent(text, i) != dent) throw new IndentException(i);
      return;
    }

    // there is still at least one element to read in this block
    j = Etc.skipBlanks(text, i + 1);

    // it's just one line
    if (Etc.indent(text, j) <= dent) {
      // omit trailing blank lines
      j = i + 1;

      // element text
      subtext = new String[] {text.get(i)};
      return;
    }

    // multiple lines
    while (Etc.indent(text, j) > dent) j++;

    // a closing brace might be part of the element
    if (Etc.indent(text, j) == dent && text.get(j).strip().startsWith("}")) {
      if (!text.get(i).contains("{")) throw new IndentException(i);
      j++;
    }

    // omit trailing blank lines
    while (text.get(j - 1).isBlank()) j--;

    // element text
    subtext = new String[j - i];
    for (var k = 0; k < subtext.length; k++) subtext[k] = text.get(i + k);
  }
}
