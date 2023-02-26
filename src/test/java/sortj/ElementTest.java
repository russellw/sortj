package sortj;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;
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

  @Test
  public void key() {
    var p = Pattern.compile("a");
    var m = p.matcher("a");
    assertTrue(m.matches());

    m = p.matcher("b");
    assertFalse(m.matches());

    m = p.matcher(" a");
    assertFalse(m.matches());

    p = Pattern.compile(" (\\w+) ");
    m = p.matcher(" abc ");
    assertTrue(m.matches());
    assertEquals(m.group(0), " abc ");
    assertEquals(m.group(1), "abc");

    p = Pattern.compile(" (\\w+)");
    m = p.matcher(" abc ");
    assertFalse(m.matches());

    p = Pattern.compile(".*\\W+(\\w+).*");
    m = p.matcher(" abc def ghi;");
    assertTrue(m.matches());
    assertEquals(m.group(1), "ghi");

    m = p.matcher("ghi;");
    assertFalse(m.matches());

    var in =
        new String[] {
          "int abc;",
        };
    var e = new Element(Arrays.asList(in), 0, 0);
    var s = e.key();
    var v = s.split("\t");
    assertEquals("abc", v[0]);
  }
}
