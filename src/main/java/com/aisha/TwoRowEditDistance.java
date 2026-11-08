package com.aisha;

public class TwoRowEditDistance implements EditDistance {

    private long operations;


    @Override
    public int distance(String a, String b) {
        resetOperations();

        if (a.length() < b.length()) {
            String temp = a;
            a = b;
            b = temp;
        }

        int n = a.length();
        int m = b.length();

        int[] prev = new int[m + 1];
        int[] curr = new int[m + 1];

        for (int j = 0; j <= m; j++) {
            prev[j] = j;
            operations++;
        }

        for (int i = 1; i <= n; i++) {

            curr[0] = i;
            operations++;

            for (int j = 1; j <= m; j++) {

                operations++;

                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    curr[j] = prev[j - 1];
                } else {
                    int delete = prev[j];
                    int insert = curr[j - 1];
                    int replace = prev[j - 1];

                    curr[j] = 1 + Math.min(delete, Math.min(insert, replace));
                }
            }

            int[] temp = prev;
            prev = curr;
            curr = temp;
        }

        return prev[m];
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