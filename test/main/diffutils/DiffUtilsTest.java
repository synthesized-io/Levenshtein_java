package main.diffutils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DiffUtilsTest {

    @Test
    void testEmptyString() {
        assertEquals(0, DiffUtils.levEditDistance("", "", 0));
        assertEquals(0, DiffUtils.levEditDistance("", "", 1));
        assertEquals(0, DiffUtils.levEditDistance("", "", 2));
    }

    @Test
    void getRatioFuzzyTest() {
        String s1 = "abxcd";
        String s2 = "abcd";

        assertEquals(0.88888, DiffUtils.getRatio(s1,s2), 0.01);
        assertEquals (0.61, DiffUtils.getRatio( "fuzzy bearrrrrlll", "fuzzy was a bear"), 0.01);

    }

    @Test
    void levEditDistanceFuzzyTest() {
        assertEquals(11, DiffUtils.levEditDistance("sf&t co., ltd.","sft", 0));
        assertEquals(3, DiffUtils.levEditDistance("kitten", "sitting", 1));
        assertEquals(3, DiffUtils.levEditDistance("saturday", "sunday", 1));
    }
}