package sortj;

import static org.junit.Assert.*;

import java.util.ArrayList;
import org.junit.Test;

public class ElementTest {
  @Test
  public void parser() {
    var text = new ArrayList<String>();
    var e = new Element(text, 0, 0);
    assert e.subtext == null;
    assert e.end == 0;

    text.add("// END");
    e = new Element(text, 0, 0);
    assert e.subtext == null;
    assert e.end == 0;

    text.clear();
    text.add(" ");
    text.add("// END");
    e = new Element(text, 0, 0);
    assert e.subtext == null;
    assert e.end == 1;

    text.clear();
    text.add(" ");
    text.add("} // closing brace");
    e = new Element(text, 1, 0);
    assert e.subtext == null;
    assert e.end == 1;

    text.clear();
    text.add(" ");
    text.add("// END");
    assertThrows(IndentException.class, () -> new Element(text, 1, 0));

    text.clear();
    text.add(" ");
    text.add(" // END");
    assertThrows(IndentException.class, () -> new Element(text, 0, 0));
  }
}
