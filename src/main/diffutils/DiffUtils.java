package main.diffutils;

import java.math.BigInteger;
import java.util.HashMap;

public class DiffUtils {
    public static double getRatio(String s1, String s2) {
        int len1 = s1.length();
        int len2 = s2.length();
        int lensum = len1 + len2;

        int editDistance = levEditDistance(s1, s2, 2);

        return (lensum - editDistance) / (double) lensum;
    }

    public static int levEditDistance(String s1, String s2, int replacementCosts) {
        return switch (replacementCosts) {
            case 1 -> uniformDistance(s1, s2);
            case 2 -> indelDistance(s1, s2);
            default -> uniformGeneric(s1, s2, replacementCosts);
        };
    }

    private static int uniformGeneric(String s1, String s2, int replacementCosts) {
        int length1 = s1.length();
        int[] cache = new int[length1 + 1];
        for (int i = 0; i < length1 + 1; i++) {
            cache[i] = i;
        }

        for (char ch : s2.toCharArray()) {
            int temp = cache[0];
            cache[0] = cache[0] + 1;

            for (int i = 0; i < length1; i++) {
                int x = temp;
                if (s1.charAt(i) != ch)
                    x = Math.min(cache[i] + 1,
                            Math.min(cache[i + 1] + 1, temp + replacementCosts));

                temp = cache[i + 1];
                cache[i + 1] = x;
            }
        }
        return cache[cache.length - 1];
    }

    private static int indelDistance(String s1, String s2) {
        int maximum = s1.length() + s2.length();
        int lcsSimilarity = (s1.length() < 64) ? quickLcsSimilarity(s1, s2) : bigLcsSimilarity(s1, s2);
        return maximum - 2 * lcsSimilarity;
    }

    private static int uniformDistance(String s1, String s2) {
        if (s1.length() == 0) return 0;
        BigInteger VP = BigInteger.ONE.shiftLeft(s1.length())
                                      .subtract(BigInteger.ONE);
        BigInteger VN = BigInteger.ZERO;
        final BigInteger mask = BigInteger.ONE.shiftLeft(s1.length() - 1);

        HashMap<Character, BigInteger> block = new HashMap<>();

        BigInteger x = BigInteger.ONE;
        for (char ch : s1.toCharArray()) {
            BigInteger elementAtKey = (block.get(ch) != null) ? block.get(ch) : BigInteger.ZERO;
            block.put(ch, elementAtKey.or(x));
            x = x.shiftLeft(1);
        }

        int currDist = s1.length();
        for (char ch : s2.toCharArray()) {
            final BigInteger X = (block.get(ch) != null) ? block.get(ch) : BigInteger.ZERO;
            final BigInteger D0 = X.and(VP)
                                   .add(VP)
                                   .xor(VP)
                                   .or(X)
                                   .or(VN);

            BigInteger HP = VN.or(D0.or(VP).not());
            BigInteger HN = D0.and(VP);
            currDist += (HP.and(mask).equals(BigInteger.ZERO)) ? 0 : 1;
            currDist -= (HN.and(mask).equals(BigInteger.ZERO)) ? 0 : 1;

            HP = HP.shiftLeft(1).or(BigInteger.ONE);
            HN = HN.shiftLeft(1);
            VP = HN.or(D0.or(HP).not());
            VN = HP.and(D0);
        }
        return currDist;
    }


    private static int bigLcsSimilarity(String s1, String s2) {
        BigInteger S = (BigInteger.ONE.shiftLeft(s1.length()));
        S = S.subtract(BigInteger.ONE);

        HashMap<Character, BigInteger> block = new HashMap<>();

        BigInteger x = BigInteger.ONE;
        for (char ch : s1.toCharArray()) {
            BigInteger valueAtKey = (block.get(ch) != null) ? block.get(ch) : BigInteger.ZERO;
            block.put(ch, (valueAtKey.or(x)));
            x = x.shiftLeft(1);
        }

        for (char ch : s2.toCharArray()) {
            BigInteger matches = (block.get(ch) != null) ? block.get(ch) : BigInteger.ZERO;
            BigInteger u = S.and(matches);
            S = (S.add(u)).or(S.subtract(u));
        }
        return (int) S.toString(2).chars().filter(ch -> ch == '0').count();
    }

    private static int quickLcsSimilarity(String s1, String s2) {
        if (s1.length() == 0) return 0;
        long S = (1L << s1.length()) - 1;

        HashMap<Character, Long> block = new HashMap<>();

        long x = 1;
        for (char ch : s1.toCharArray()) {
            long valueAtKey = (block.get(ch) != null) ? block.get(ch) : 0;
            block.put(ch, (valueAtKey | x));
            x = x << 1;
        }

        for (char ch : s2.toCharArray()) {
            long matches = (block.get(ch) != null) ? block.get(ch) : 0;
            long u = S & matches;
            S = (S + u) | (S - u);
        }

        return (int) Long.toBinaryString(S).chars().filter(ch -> ch == '0').count();
    }

}
