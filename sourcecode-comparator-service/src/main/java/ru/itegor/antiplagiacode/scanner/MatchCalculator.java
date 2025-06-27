package ru.itegor.antiplagiacode.scanner;

import org.springframework.stereotype.Component;

import java.util.List;

// https://www.geeksforgeeks.org/dsa/edit-distance-dp-5/

@Component
public class MatchCalculator {
    public int editDistance(String s1, String s2) {
        int m = s1.length();
        int n = s2.length();

        int prev;
        int[] curr = new int[n + 1];

        for (int j = 0; j <= n; j++)
            curr[j] = j;

        for (int i = 1; i <= m; i++) {
            prev = curr[0];
            curr[0] = i;
            for (int j = 1; j <= n; j++) {
                int temp = curr[j];
                if (s1.charAt(i - 1) == s2.charAt(j - 1))
                    curr[j] = prev;
                else
                    curr[j] = 1 + Math.min(Math.min(curr[j - 1], prev), curr[j]);
                prev = temp;
            }
        }

        return curr[n];
    }

    public double getStringPercentage(String s1, String s2) {
        try {
            return getFullStringPercentage(s1, s2);
        } catch (Throwable e) {
            try {
                if (e instanceof OutOfMemoryError) {
                    return getPerLineStringPercentage(s1, s2);
                } else {
                    return 0;
                }
            } catch (Throwable perLinesError) {
                return 0;
            }
        }
    }

    private double getFullStringPercentage(String s1, String s2) {
        int length = Math.max(s1.length(), s2.length());
        return ((double) (length - editDistance(s1, s2)) / length) * 100;
    }

    private double getPerLineStringPercentage(String s1, String s2) {
        List<String> lines1 = List.of(s1.split("\n"));
        List<String> lines2 = List.of(s2.split("\n"));

        int maxDistance = 0;
        for (int i = 0; i < lines1.size(); i++) {
            maxDistance += Math.max(lines1.get(i).length(), lines2.get(i).length());
        }

        int fileEditDistance = 0;

        for (int i = 0; i < lines1.size(); i++) {
            fileEditDistance += editDistance(lines1.get(i), lines2.get(i));
        }

        return ((double) (maxDistance - fileEditDistance) / maxDistance) * 100;
    }
}
