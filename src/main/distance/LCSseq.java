package main.distance;

import java.awt.*;
import java.math.BigInteger;
import java.util.HashMap;

public class LCSseq  {
    /**
     * Calculates the value of the main.distance.
     *
     * @return returns an integer representing the main.distance.
     */
    public static int similarity(String s1, String s2) {
        if (s1.length() == 0) return 0;
        BigInteger S = (BigInteger.ONE.shiftLeft(s1.length()));
        S = S.subtract(BigInteger.ONE);

        HashMap<Character, BigInteger> block = new HashMap<>();

        BigInteger x = BigInteger.ONE;
        for (int i = 0; i < s1.length(); i++) {
            char ch1 = s1.charAt(i);
            BigInteger valueAtKey = (block.get(ch1) != null) ? block.get(ch1) : BigInteger.ZERO;
            block.put(ch1, (valueAtKey.or(x)));
            x = x.shiftLeft(1);
        }

        for (int i = 0; i < s2.length(); i++) {
            char ch2 = s2.charAt(i);
            BigInteger matches = (block.get(ch2) != null) ? block.get(ch2) : BigInteger.ZERO;
            BigInteger u = S.and(matches);
            S = (S.add(u)).or(S.subtract(u));
        }
        return (int)S.toString(2).chars().filter(ch -> ch == '0').count();
    }

    public static int similarity(String s1, String s2, int scoreCutoff) {
        int score = similarity(s1, s2);
        return (score >= scoreCutoff) ? score : 0;
    }

    public static int similarity(String s1, String s2, Preprocessor preprocessor) {
        String a = preprocessor.process(s1);
        String b = preprocessor.process(s2);
        return similarity(a, b);
    }

    public static int similarity(String s1, String s2, Preprocessor preprocessor, int scoreCutoff) {
        String a = preprocessor.process(s1);
        String b = preprocessor.process(s2);
        return similarity(a, b, scoreCutoff);
    }

    /**
     * Calculates the length of the longest common subsequence.
     *
     * @return an integer representing similarity.
     */
    public static int distance(String s1, String s2) {
        return 0;
    }

    /**
     * Calculates the normalized main.distance.
     *
     * @return a float representing normalized main.distance.
     */
    public static float normalized_distance(String s1, String s2) {
        return 0;
    }

    /**
     * Returns the normalized similarity value of the main.distance.
     *
     * @return a float representing similarity.
     */
    public static float normalized_similarity(String s1, String s2) {
        return 0;
    }
}
