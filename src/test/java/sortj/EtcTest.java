package sortj;

import static org.junit.Assert.*;

import java.util.List;
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
    assertEquals(Etc.indent(List.of("a"), 0), 0);
    assertEquals(Etc.indent(List.of("  a   \t   b"), 0), 2);
    assertThrows(TabException.class, () -> Etc.indent(List.of("\t"), 0));
    assertThrows(TabException.class, () -> Etc.indent(List.of("   \tx"), 0));
  }
}
