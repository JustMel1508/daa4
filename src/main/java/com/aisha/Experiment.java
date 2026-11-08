package com.aisha;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

public class Experiment {

    private static final int RUNS = 5;

    public void runAll() throws IOException {

        File resultsDirectory =
                new File("results");

        if (!resultsDirectory.exists()) {
            resultsDirectory.mkdirs();
        }

        File outputFile =
                new File(
                        resultsDirectory,
                        "results.csv"
                );

        try (
                BufferedWriter writer =
                        new BufferedWriter(
                                new FileWriter(outputFile)
                        )
        ) {

            writer.write(
                    "family,algorithm,input,n,m,run,time_ms,operations"
            );

            writer.newLine();

            System.out.println(
                    "Starting Edit Distance experiments..."
            );

            runRecursiveEditDistance(writer);

            runDynamicProgrammingEditDistance(writer);

            System.out.println(
                    "Starting String Search experiments..."
            );

            runStringSearchExperiments(writer);
        }

        System.out.println();
        System.out.println(
                "Experiment finished successfully."
        );

        System.out.println(
                "Results saved to:"
        );

        System.out.println(
                "results/results.csv"
        );
    }



    private void runRecursiveEditDistance(
            BufferedWriter writer
    ) throws IOException {

        int[] sizes = {
                2,
                4,
                6,
                8,
                10
        };

        for (int n : sizes) {

            String equalA =
                    repeated('A', n);

            String equalB =
                    repeated('A', n);

            runEditCase(
                    writer,
                    new RecursiveEditDistance(),
                    "Recursive",
                    "equal",
                    equalA,
                    equalB,
                    n
            );

            String differentA =
                    repeated('A', n);

            String differentB =
                    repeated('B', n);

            runEditCase(
                    writer,
                    new RecursiveEditDistance(),
                    "Recursive",
                    "different",
                    differentA,
                    differentB,
                    n
            );

            InputGenerator generator =
                    new InputGenerator(
                            2518L + n
                    );

            String randomA =
                    generator.randomString(n);

            String randomB =
                    generator.randomString(n);

            runEditCase(
                    writer,
                    new RecursiveEditDistance(),
                    "Recursive",
                    "random",
                    randomA,
                    randomB,
                    n
            );
        }
    }



    private void runDynamicProgrammingEditDistance(
            BufferedWriter writer
    ) throws IOException {

        int[] sizes = {
                128,
                256,
                512,
                1024
        };

        for (int n : sizes) {

            String equalA =
                    repeated('A', n);

            String equalB =
                    repeated('A', n);

            String differentA =
                    repeated('A', n);

            String differentB =
                    repeated('B', n);

            InputGenerator generator =
                    new InputGenerator(
                            5000L + n
                    );

            String randomA =
                    generator.randomString(n);

            String randomB =
                    generator.randomString(n);


            runEditCase(
                    writer,
                    new TableEditDistance(),
                    "Table",
                    "equal",
                    equalA,
                    equalB,
                    n
            );

            runEditCase(
                    writer,
                    new TwoRowEditDistance(),
                    "TwoRows",
                    "equal",
                    equalA,
                    equalB,
                    n
            );

            runEditCase(
                    writer,
                    new TableEditDistance(),
                    "Table",
                    "different",
                    differentA,
                    differentB,
                    n
            );

            runEditCase(
                    writer,
                    new TwoRowEditDistance(),
                    "TwoRows",
                    "different",
                    differentA,
                    differentB,
                    n
            );



            runEditCase(
                    writer,
                    new TableEditDistance(),
                    "Table",
                    "random",
                    randomA,
                    randomB,
                    n
            );

            runEditCase(
                    writer,
                    new TwoRowEditDistance(),
                    "TwoRows",
                    "random",
                    randomA,
                    randomB,
                    n
            );
        }
    }



    private void runStringSearchExperiments(
            BufferedWriter writer
    ) throws IOException {

        int[] sizes = {
                2048,
                4096,
                8192,
                16384
        };

        for (int n : sizes) {

            int patternLength =
                    Math.max(
                            8,
                            n / 64
                    );



            InputGenerator randomGenerator =
                    new InputGenerator(
                            10000L + n
                    );

            String randomText =
                    randomGenerator.randomString(n);

            String randomPattern =
                    randomGenerator.randomString(
                            patternLength
                    );

            runSearchCase(
                    writer,
                    new NaiveSearch(),
                    "Naive",
                    "random",
                    randomText,
                    randomPattern,
                    n,
                    patternLength
            );

            runSearchCase(
                    writer,
                    new KmpSearch(),
                    "KMP",
                    "random",
                    randomText,
                    randomPattern,
                    n,
                    patternLength
            );



            InputGenerator worstGenerator =
                    new InputGenerator(
                            20000L + n
                    );

            String worstText =
                    worstGenerator
                            .repeatedWorstCaseText(n);

            String worstPattern =
                    worstGenerator
                            .repeatedWorstCasePattern(
                                    patternLength
                            );

            runSearchCase(
                    writer,
                    new NaiveSearch(),
                    "Naive",
                    "repeated_worst",
                    worstText,
                    worstPattern,
                    n,
                    patternLength
            );

            runSearchCase(
                    writer,
                    new KmpSearch(),
                    "KMP",
                    "repeated_worst",
                    worstText,
                    worstPattern,
                    n,
                    patternLength
            );



            InputGenerator alternatingGenerator =
                    new InputGenerator(
                            30000L + n
                    );

            String alternatingText =
                    alternatingGenerator
                            .alternatingString(n);

            String alternatingPattern =
                    alternatingGenerator
                            .alternatingString(
                                    patternLength
                            );

            runSearchCase(
                    writer,
                    new NaiveSearch(),
                    "Naive",
                    "alternating",
                    alternatingText,
                    alternatingPattern,
                    n,
                    patternLength
            );

            runSearchCase(
                    writer,
                    new KmpSearch(),
                    "KMP",
                    "alternating",
                    alternatingText,
                    alternatingPattern,
                    n,
                    patternLength
            );
        }
    }


    private void runEditCase(
            BufferedWriter writer,
            EditDistance algorithm,
            String algorithmName,
            String inputType,
            String a,
            String b,
            int n
    ) throws IOException {


        algorithm.distance(
                a,
                b
        );

        for (int run = 1; run <= RUNS; run++) {

            long start =
                    System.nanoTime();

            algorithm.distance(
                    a,
                    b
            );

            long end =
                    System.nanoTime();

            double timeMs =
                    (end - start)
                            / 1_000_000.0;

            long operations =
                    algorithm.getOperations();

            writeRow(
                    writer,
                    "EditDistance",
                    algorithmName,
                    inputType,
                    n,
                    b.length(),
                    run,
                    timeMs,
                    operations
            );
        }
    }



    private void runSearchCase(
            BufferedWriter writer,
            StringSearch algorithm,
            String algorithmName,
            String inputType,
            String text,
            String pattern,
            int n,
            int m
    ) throws IOException {


        algorithm.search(
                text,
                pattern
        );

        for (int run = 1; run <= RUNS; run++) {

            long start =
                    System.nanoTime();

            algorithm.search(
                    text,
                    pattern
            );

            long end =
                    System.nanoTime();

            double timeMs =
                    (end - start)
                            / 1_000_000.0;

            long operations =
                    algorithm.getOperations();

            writeRow(
                    writer,
                    "StringSearch",
                    algorithmName,
                    inputType,
                    n,
                    m,
                    run,
                    timeMs,
                    operations
            );
        }
    }



    private void writeRow(
            BufferedWriter writer,
            String family,
            String algorithm,
            String input,
            int n,
            int m,
            int run,
            double timeMs,
            long operations
    ) throws IOException {

        writer.write(
                family
                        + ","
                        + algorithm
                        + ","
                        + input
                        + ","
                        + n
                        + ","
                        + m
                        + ","
                        + run
                        + ","
                        + String.format(
                        Locale.US,
                        "%.6f",
                        timeMs
                )
                        + ","
                        + operations
        );

        writer.newLine();
    }



    private String repeated(
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
}