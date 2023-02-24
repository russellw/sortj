package sortj;

import static org.junit.Assert.*;

import org.junit.Test;

public class EtcTest {
  @Test
  public void ext() {
    assertEquals(Etc.ext("foo.txt"), "txt");
    assertEquals(Etc.ext("foo.c"), "c");
    assertEquals(Etc.ext("foo"), "");
  }

  @Test
  public void indent() {
    assertEquals(Etc.indent(0, "a"), 0);
    assertEquals(Etc.indent(0, "  a   \t   b"), 2);
    assertThrows(TabException.class, () -> Etc.indent(0, "\t"));
    assertThrows(TabException.class, () -> Etc.indent(0, "   \tx"));
  }
}
