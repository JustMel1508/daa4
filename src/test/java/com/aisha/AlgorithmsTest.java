package com.aisha;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class AlgorithmsTest {



    @Test
    void recursiveReferenceValues() {

        EditDistance algorithm = new RecursiveEditDistance();

        assertEquals(
                3,
                algorithm.distance("kitten", "sitting")
        );

        assertEquals(
                2,
                algorithm.distance("flaw", "lawn")
        );

        assertEquals(
                5,
                algorithm.distance("intention", "execution")
        );

        assertEquals(
                3,
                algorithm.distance("", "abc")
        );

        assertEquals(
                0,
                algorithm.distance("abc", "abc")
        );
    }

    @Test
    void tableReferenceValues() {

        EditDistance algorithm = new TableEditDistance();

        assertEquals(
                3,
                algorithm.distance("kitten", "sitting")
        );

        assertEquals(
                2,
                algorithm.distance("flaw", "lawn")
        );

        assertEquals(
                5,
                algorithm.distance("intention", "execution")
        );

        assertEquals(
                3,
                algorithm.distance("", "abc")
        );

        assertEquals(
                0,
                algorithm.distance("abc", "abc")
        );
    }

    @Test
    void twoRowsReferenceValues() {

        EditDistance algorithm = new TwoRowEditDistance();

        assertEquals(
                3,
                algorithm.distance("kitten", "sitting")
        );

        assertEquals(
                2,
                algorithm.distance("flaw", "lawn")
        );

        assertEquals(
                5,
                algorithm.distance("intention", "execution")
        );

        assertEquals(
                3,
                algorithm.distance("", "abc")
        );

        assertEquals(
                0,
                algorithm.distance("abc", "abc")
        );
    }



    @Test
    void emptyStringsWorkForAllVersions() {

        EditDistance[] algorithms = {
                new RecursiveEditDistance(),
                new TableEditDistance(),
                new TwoRowEditDistance()
        };

        for (EditDistance algorithm : algorithms) {

            assertEquals(
                    0,
                    algorithm.distance("", "")
            );

            assertEquals(
                    3,
                    algorithm.distance("", "abc")
            );

            assertEquals(
                    3,
                    algorithm.distance("abc", "")
            );
        }
    }



    @Test
    void allEditDistanceVersionsAgreeOn100RandomStrings() {

        Random random = new Random(2518);

        RecursiveEditDistance recursive =
                new RecursiveEditDistance();

        TableEditDistance table =
                new TableEditDistance();

        TwoRowEditDistance twoRows =
                new TwoRowEditDistance();

        for (int test = 0; test < 100; test++) {

            String a = randomStringAllowEmpty(random, 5);
            String b = randomStringAllowEmpty(random, 5);

            int recursiveResult =
                    recursive.distance(a, b);

            int tableResult =
                    table.distance(a, b);

            int twoRowsResult =
                    twoRows.distance(a, b);

            assertEquals(
                    tableResult,
                    recursiveResult,
                    "Recursive result differs for a="
                            + a
                            + ", b="
                            + b
            );

            assertEquals(
                    tableResult,
                    twoRowsResult,
                    "TwoRows result differs for a="
                            + a
                            + ", b="
                            + b
            );
        }
    }



    @Test
    void firstLpsReferenceValue() {

        KmpSearch kmp = new KmpSearch();

        int[] expected = {
                0, 1, 2, 0, 1, 2, 3, 3
        };

        int[] actual =
                kmp.lps("AAACAAAA");

        assertArrayEquals(
                expected,
                actual
        );
    }

    @Test
    void secondLpsReferenceValue() {

        KmpSearch kmp = new KmpSearch();

        int[] expected = {
                0, 0, 1, 2, 0, 1, 2, 3, 4
        };

        int[] actual =
                kmp.lps("ABABCABAB");

        assertArrayEquals(
                expected,
                actual
        );
    }


    @Test
    void searchReferenceValue() {

        NaiveSearch naive =
                new NaiveSearch();

        KmpSearch kmp =
                new KmpSearch();

        String text =
                "ABABDABACDABABCABAB";

        String pattern =
                "ABABCABAB";

        List<Integer> expected =
                List.of(10);

        assertEquals(
                expected,
                naive.search(text, pattern)
        );

        assertEquals(
                expected,
                kmp.search(text, pattern)
        );
    }

    @Test
    void overlappingACharactersAreFound() {

        NaiveSearch naive =
                new NaiveSearch();

        KmpSearch kmp =
                new KmpSearch();

        List<Integer> expected =
                List.of(0, 1, 2, 3);

        assertEquals(
                expected,
                naive.search("AAAAA", "AA")
        );

        assertEquals(
                expected,
                kmp.search("AAAAA", "AA")
        );
    }

    @Test
    void overlappingAbaPatternsAreFound() {

        NaiveSearch naive =
                new NaiveSearch();

        KmpSearch kmp =
                new KmpSearch();

        List<Integer> expected =
                List.of(0, 2, 4);

        assertEquals(
                expected,
                naive.search("ABABABA", "ABA")
        );

        assertEquals(
                expected,
                kmp.search("ABABABA", "ABA")
        );
    }

    @Test
    void emptyPatternThrowsExceptionInNaive() {

        NaiveSearch naive =
                new NaiveSearch();

        assertThrows(
                IllegalArgumentException.class,
                () -> naive.search(
                        "ABC",
                        ""
                )
        );
    }

    @Test
    void emptyPatternThrowsExceptionInKmp() {

        KmpSearch kmp =
                new KmpSearch();

        assertThrows(
                IllegalArgumentException.class,
                () -> kmp.search(
                        "ABC",
                        ""
                )
        );
    }

    @Test
    void longerPatternReturnsEmptyList() {

        NaiveSearch naive =
                new NaiveSearch();

        KmpSearch kmp =
                new KmpSearch();

        assertTrue(
                naive.search(
                        "ABC",
                        "ABCDEFG"
                ).isEmpty()
        );

        assertTrue(
                kmp.search(
                        "ABC",
                        "ABCDEFG"
                ).isEmpty()
        );
    }

    @Test
    void noMatchReturnsEmptyList() {

        NaiveSearch naive =
                new NaiveSearch();

        KmpSearch kmp =
                new KmpSearch();

        assertTrue(
                naive.search(
                        "ABCDEFG",
                        "XYZ"
                ).isEmpty()
        );

        assertTrue(
                kmp.search(
                        "ABCDEFG",
                        "XYZ"
                ).isEmpty()
        );
    }



    @Test
    void naiveAndKmpAgreeOn100RandomTexts() {

        Random random =
                new Random(2518);

        NaiveSearch naive =
                new NaiveSearch();

        KmpSearch kmp =
                new KmpSearch();

        for (int test = 0; test < 100; test++) {


            String text =
                    randomStringNonEmpty(
                            random,
                            10,
                            30
                    );


            String pattern =
                    randomStringNonEmpty(
                            random,
                            1,
                            6
                    );

            List<Integer> naiveResult =
                    naive.search(
                            text,
                            pattern
                    );

            List<Integer> kmpResult =
                    kmp.search(
                            text,
                            pattern
                    );

            assertEquals(
                    naiveResult,
                    kmpResult,
                    "Naive and KMP differ."
                            + " Text="
                            + text
                            + ", Pattern="
                            + pattern
            );
        }
    }



    @Test
    void editDistanceCounterProducesMeasurements() {

        EditDistance algorithm =
                new TableEditDistance();

        algorithm.distance(
                "kitten",
                "sitting"
        );

        assertTrue(
                algorithm.getOperations() > 0
        );
    }

    @Test
    void searchCounterProducesMeasurements() {

        StringSearch algorithm =
                new NaiveSearch();

        algorithm.search(
                "ABABABABAB",
                "ABA"
        );

        assertTrue(
                algorithm.getOperations() > 0
        );
    }


    private String randomStringAllowEmpty(
            Random random,
            int maxLength
    ) {

        int length =
                random.nextInt(
                        maxLength + 1
                );

        String alphabet =
                "ABC";

        StringBuilder builder =
                new StringBuilder();

        for (int i = 0; i < length; i++) {

            int index =
                    random.nextInt(
                            alphabet.length()
                    );

            builder.append(
                    alphabet.charAt(index)
            );
        }

        return builder.toString();
    }


    private String randomStringNonEmpty(
            Random random,
            int minLength,
            int maxLength
    ) {

        int length =
                minLength
                        + random.nextInt(
                        maxLength
                                - minLength
                                + 1
                );

        String alphabet =
                "ABC";

        StringBuilder builder =
                new StringBuilder();

        for (int i = 0; i < length; i++) {

            int index =
                    random.nextInt(
                            alphabet.length()
                    );

            builder.append(
                    alphabet.charAt(index)
            );
        }

        return builder.toString();
    }
}