package sortj;

import org.junit.Test;

import static org.junit.Assert.*;

public class EtcTest {
    @Test
    public void ext() {
        assertEquals(Etc.ext("foo.txt"),"txt");
        assertEquals(Etc.ext("foo.c"),"c");
        assertEquals(Etc.ext("foo"),"");
    }
}
