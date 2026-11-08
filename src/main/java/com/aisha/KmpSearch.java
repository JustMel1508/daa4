package com.aisha;

import java.util.ArrayList;
import java.util.List;

public class KmpSearch implements StringSearch {

    private long operations;

    public int[] lps(String pattern) {

        if (pattern.isEmpty()) {
            throw new IllegalArgumentException("Pattern must not be empty");
        }

        int[] lps = new int[pattern.length()];

        int length = 0;
        int i = 1;

        while (i < pattern.length()) {

            operations++;

            if (pattern.charAt(i) == pattern.charAt(length)) {

                length++;
                lps[i] = length;
                i++;

            } else {

                if (length != 0) {
                    length = lps[length - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }

        return lps;
    }

    @Override
    public List<Integer> search(String text, String pattern) {

        resetOperations();

        if (pattern.isEmpty()) {
            throw new IllegalArgumentException("Pattern must not be empty");
        }

        List<Integer> result = new ArrayList<>();

        if (pattern.length() > text.length()) {
            return result;
        }

        int[] lps = lps(pattern);

        int i = 0;
        int j = 0;

        while (i < text.length()) {

            operations++;

            if (text.charAt(i) == pattern.charAt(j)) {

                i++;
                j++;

                if (j == pattern.length()) {

                    result.add(i - j);

                    j = lps[j - 1];
                }

            } else {

                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }

        return result;
    }

    @Override
    public long getOperations() {
        return operations;
    }

    @Override
    public void resetOperations() {
        operations = 0;
    }
}