package com.aisha;

import java.util.Random;

public class InputGenerator {

    private final Random random;

    private static final String ALPHABET =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public InputGenerator(long seed) {
        this.random = new Random(seed);
    }

    public String randomString(int length) {

        StringBuilder builder =
                new StringBuilder(length);

        for (int i = 0; i < length; i++) {

            int index =
                    random.nextInt(
                            ALPHABET.length()
                    );

            builder.append(
                    ALPHABET.charAt(index)
            );
        }

        return builder.toString();
    }

    public String repeatedString(
            char character,
            int length
    ) {

        StringBuilder builder =
                new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            builder.append(character);
        }

        return builder.toString();
    }

    public String alternatingString(
            int length
    ) {

        StringBuilder builder =
                new StringBuilder(length);

        for (int i = 0; i < length; i++) {

            if (i % 2 == 0) {
                builder.append('A');
            } else {
                builder.append('B');
            }
        }

        return builder.toString();
    }

    public String repeatedWorstCaseText(
            int length
    ) {

        return repeatedString(
                'A',
                length
        );
    }

    public String repeatedWorstCasePattern(
            int length
    ) {

        if (length <= 1) {
            return "B";
        }

        StringBuilder builder =
                new StringBuilder(length);

        for (int i = 0; i < length - 1; i++) {
            builder.append('A');
        }

        builder.append('B');

        return builder.toString();
    }
}