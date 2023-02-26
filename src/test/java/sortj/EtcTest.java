package sortj;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class EtcTest {
  @Test
  public void ext() {
    assertEquals("txt", Etc.ext("foo.txt"));
    assertEquals("c", Etc.ext("foo.c"));
    assertEquals("", Etc.ext("foo"));
  }

  @Test
  public void indent() {
    assertEquals(Integer.MAX_VALUE, Etc.indent(List.of(" "), 0));
    assertEquals(0, Etc.indent(List.of("a"), 0));
    assertEquals(2, Etc.indent(List.of("  a   \t   b"), 0));
    assertThrows(TabException.class, () -> Etc.indent(List.of("   \tx"), 0));
    assertTrue(Etc.indent(List.of(""), 1) < 0);
  }

  @Test
  public void skipBlanks() {
    var text = new ArrayList<String>();
    assertEquals(0, Etc.skipBlanks(text, 0));

    text.add("");
    assertEquals(1, Etc.skipBlanks(text, 0));

    text.clear();
    text.add(" ");
    assertEquals(1, Etc.skipBlanks(text, 0));

    text.clear();
    text.add(" ");
    text.add(" x");
    assertEquals(1, Etc.skipBlanks(text, 0));
  }

  @Test
  public void replace() {
    var in = List.of("0", "1", "2", "3", "4");
    var out = List.of("a", "b");
    assertEquals(List.of("a", "b"), Etc.replace(in, -1, 10, out));
    assertEquals(List.of("0", "a", "b"), Etc.replace(in, 1, 10, out));
    assertEquals(List.of("0", "a", "b", "4"), Etc.replace(in, 1, 4, out));
  }
}
