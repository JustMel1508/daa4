package com.aisha;

import java.util.ArrayList;
import java.util.List;

public class NaiveSearch implements StringSearch {

    private long operations;

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

        int n = text.length();
        int m = pattern.length();

        for (int i = 0; i <= n - m; i++) {

            boolean match = true;

            for (int j = 0; j < m; j++) {
                operations++;

                if (text.charAt(i + j) != pattern.charAt(j)) {
                    match = false;
                    break;
                }
            }

            if (match) {
                result.add(i);
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