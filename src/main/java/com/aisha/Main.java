package com.aisha;

public class Main {

    public static void main(String[] args) {

        if (args.length == 0) {
            printHelp();
            return;
        }

        String command = args[0].toLowerCase();

        try {

            switch (command) {

                case "demo":
                    runDemo();
                    break;

                case "experiment":
                    new Experiment().runAll();
                    break;

                case "plots":
                    new PlotGenerator().generateAll();
                    break;

                default:
                    printHelp();
                    break;
            }

        } catch (Exception e) {

            System.err.println(
                    "Error: " + e.getMessage()
            );

            e.printStackTrace();
        }
    }

    private static void runDemo() {

        System.out.println(
                "=============================="
        );

        System.out.println(
                "EDIT DISTANCE"
        );

        System.out.println(
                "=============================="
        );

        EditDistance recursive =
                new RecursiveEditDistance();

        EditDistance table =
                new TableEditDistance();

        EditDistance twoRows =
                new TwoRowEditDistance();

        System.out.println(
                "kitten -> sitting"
        );

        System.out.println(
                "Recursive = "
                        + recursive.distance(
                        "kitten",
                        "sitting"
                )
        );

        System.out.println(
                "Table = "
                        + table.distance(
                        "kitten",
                        "sitting"
                )
        );

        System.out.println(
                "Two Rows = "
                        + twoRows.distance(
                        "kitten",
                        "sitting"
                )
        );

        System.out.println();

        System.out.println(
                "flaw -> lawn"
        );

        System.out.println(
                "Distance = "
                        + table.distance(
                        "flaw",
                        "lawn"
                )
        );

        System.out.println();

        System.out.println(
                "intention -> execution"
        );

        System.out.println(
                "Distance = "
                        + table.distance(
                        "intention",
                        "execution"
                )
        );

        System.out.println();

        System.out.println(
                "=============================="
        );

        System.out.println(
                "STRING SEARCH"
        );

        System.out.println(
                "=============================="
        );

        NaiveSearch naive =
                new NaiveSearch();

        KmpSearch kmp =
                new KmpSearch();

        System.out.println(
                "Text: ABABABA"
        );

        System.out.println(
                "Pattern: ABA"
        );

        System.out.println(
                "Naive = "
                        + naive.search(
                        "ABABABA",
                        "ABA"
                )
        );

        System.out.println(
                "KMP = "
                        + kmp.search(
                        "ABABABA",
                        "ABA"
                )
        );

        System.out.println();

        System.out.println(
                "KMP LPS:"
        );

        printArray(
                kmp.lps(
                        "ABABCABAB"
                )
        );
    }

    private static void printArray(
            int[] array
    ) {

        System.out.print("[");

        for (int i = 0; i < array.length; i++) {

            System.out.print(
                    array[i]
            );

            if (i < array.length - 1) {
                System.out.print(", ");
            }
        }

        System.out.println("]");
    }

    private static void printHelp() {

        System.out.println(
                "Available commands:"
        );

        System.out.println(
                "demo"
        );

        System.out.println(
                "experiment"
        );

        System.out.println(
                "plots"
        );
    }
}