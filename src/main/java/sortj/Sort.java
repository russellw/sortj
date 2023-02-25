package sortj;

import java.util.List;

public final class Sort {
  List<String> text;

  Sort(List<String> text, int i) {
    assert Etc.reallyStartsWith(text, i, "// SORT");
    this.text = text;

    var dent = Etc.indent(text, i);
  }
}
