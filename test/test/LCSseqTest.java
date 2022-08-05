package test;

import main.distance.LCSseq;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LCSseqTest {

    @Test
    void testEmptyStrings() {
        assertEquals(LCSseq.similarity("", ""), 0);
    }

    @Test
    void testSimilarStrings() {
        assertEquals(4, LCSseq.similarity("test", "test"));
    }
    @Test
    void testDifferentStrings(){
        assertEquals(0, LCSseq.similarity("aaaa", "bbbb"));
        long startTime = System.nanoTime();
        LCSseq.similarity("aaaa", "bbbb");
        long endTime = System.nanoTime();
        System.out.println((endTime - startTime));
    }

}