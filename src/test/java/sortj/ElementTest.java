package sortj;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Test;

public class ElementTest {
  @Test
  public void parser() {
    var text = new ArrayList<String>();
    var e = new Element(text, 0, 0);
    assertNull(e.subtext);
    assertEquals(0, e.j);

    text.add("// END");
    e = new Element(text, 0, 0);
    assertNull(e.subtext);
    assertEquals(0, e.j);

    text.clear();
    text.add(" ");
    text.add("// END");
    e = new Element(text, 0, 0);
    assertNull(e.subtext);
    assertEquals(1, e.j);

    text.clear();
    text.add(" ");
    text.add("} // closing brace");
    e = new Element(text, 1, 0);
    assertNull(e.subtext);
    assertEquals(1, e.j);

    text.clear();
    text.add(" ");
    text.add("// END");
    assertThrows(IndentException.class, () -> new Element(text, 1, 0));

    text.clear();
    text.add(" ");
    text.add(" // END");
    assertThrows(IndentException.class, () -> new Element(text, 0, 0));

    text.clear();
    text.add("a");
    text.add("// END");
    e = new Element(text, 0, 0);
    assertEquals(1, e.j);
    assertArrayEquals(new String[] {"a"}, e.subtext);

    text.clear();
    text.add("a");
    text.add("b");
    e = new Element(text, 0, 0);
    assertEquals(1, e.j);
    assertArrayEquals(new String[] {"a"}, e.subtext);

    text.clear();
    text.add("a");
    text.add(" b");
    text.add("c");
    e = new Element(text, 0, 0);
    assertEquals(2, e.j);
    assertArrayEquals(new String[] {"a", " b"}, e.subtext);

    text.clear();
    text.add("a {");
    text.add(" b");
    text.add("}");
    e = new Element(text, 0, 0);
    assertEquals(3, e.j);
    assertArrayEquals(new String[] {"a {", " b", "}"}, e.subtext);

    text.clear();
    text.add(" a");
    text.add("  b");
    text.add("}");
    e = new Element(text, 1, 0);
    assertEquals(2, e.j);
    assertArrayEquals(new String[] {" a", "  b"}, e.subtext);

    var in =
        new String[] {
          "a",
        };
    var out =
        new String[] {
          "a",
        };
    e = new Element(Arrays.asList(in), 0, 0);
    assertArrayEquals(out, e.subtext);
    assertEquals(out.length, e.j);

    in =
        new String[] {
          "@a", "a", "b",
        };
    out =
        new String[] {
          "@a", "a",
        };
    e = new Element(Arrays.asList(in), 0, 0);
    assertArrayEquals(out, e.subtext);
    assertEquals(out.length, e.j);
  }
}
