# Assignment 4 — Dynamic Programming and String Algorithms

**Student:** Aisha Kazbekova  
**Group:** SE-2518

## Project Description

This project implements algorithms from Dynamic Programming and String Matching.

The project contains:

- Recursive Edit Distance
- Table Edit Distance
- Two-Row Edit Distance
- Naive String Search
- KMP String Search
- LPS preprocessing
- Internal operation counters
- Reproducible input generator
- JUnit 5 tests
- Experimental measurements
- CSV results
- PNG plots
- Experiment report

---

## Technologies

- Java 17
- Maven
- JUnit 5
- IntelliJ IDEA

---

## Project Structure

```text
daaass4
├── pom.xml
├── README.md
├── REPORT.md
├── EXPERIMENT_PLAN.md
├── results
│   ├── results.csv
│   └── plots
│       ├── 01_recursive_operations.png
│       ├── 02_dp_runtime.png
│       ├── 03_search_operations.png
│       └── 04_search_ratio.png
│
└── src
    ├── main
    │   └── java
    │       └── com.aisha
    │           ├── Main.java
    │           ├── EditDistance.java
    │           ├── RecursiveEditDistance.java
    │           ├── TableEditDistance.java
    │           ├── TwoRowEditDistance.java
    │           ├── StringSearch.java
    │           ├── NaiveSearch.java
    │           ├── KmpSearch.java
    │           ├── InputGenerator.java
    │           ├── Experiment.java
    │           └── PlotGenerator.java
    │
    └── test
        └── java
            └── com.aisha
                └── AlgorithmsTest.java
```

---

# Build

The project uses Maven.

To build the project, open the terminal in the project directory and run:

```bash
mvn clean test
```

The project should compile successfully and all JUnit 5 tests should pass.

---

# Run Tests

Tests are located in:

```text
src/test/java/com.aisha/AlgorithmsTest.java
```

They can be executed from IntelliJ IDEA by opening `AlgorithmsTest.java` and selecting:

```text
Run 'AlgorithmsTest'
```

They can also be executed from the terminal:

```bash
mvn test
```

The tests check:

- Edit Distance reference values
- empty strings
- agreement between all three Edit Distance versions
- 100 random Edit Distance cases
- reference LPS arrays
- reference String Search results
- overlapping matches
- empty pattern exception
- pattern longer than text
- no-match case
- agreement between Naive Search and KMP on 100 random texts
- algorithm counters

---

# Run Demo

The program supports a `demo` command.

In IntelliJ IDEA create an Application configuration with:

```text
Main class:
com.aisha.Main

Program arguments:
demo
```

Run the configuration.

The demo shows reference results for Edit Distance, Naive Search, KMP and LPS.

---

# Run Experiment

Before running the experiment, the experiment design and predictions are recorded in:

```text
EXPERIMENT_PLAN.md
```

Create an IntelliJ Application configuration with:

```text
Main class:
com.aisha.Main

Program arguments:
experiment
```

Run the configuration.

The experiment performs five measured runs for every experimental case and saves the results to:

```text
results/results.csv
```

The CSV contains:

```text
family
algorithm
input
n
m
run
time_ms
operations
```

A fixed random seed is used so that the generated input data is reproducible.

---

# Generate Plots

Create another IntelliJ Application configuration with:

```text
Main class:
com.aisha.Main

Program arguments:
plots
```

Run the configuration after the experiment has created `results/results.csv`.

The generated charts are saved in:

```text
results/plots/
```

The project creates four plots:

```text
01_recursive_operations.png
02_dp_runtime.png
03_search_operations.png
04_search_ratio.png
```

The plots compare:

- recursive Edit Distance growth;
- dynamic programming runtime;
- Naive and KMP character comparisons;
- the Naive-to-KMP work ratio.

The recursive plot uses a logarithmic scale, and the search ratio plot explicitly compares the measured work of Naive Search and KMP.

---

# Edit Distance

The project contains three Edit Distance implementations.

## Recursive

`RecursiveEditDistance`

This implementation uses direct recursion without memoization.

It demonstrates how repeated subproblems make the recursive solution expensive.

## Table

`TableEditDistance`

This implementation uses a complete dynamic programming table.

Its time complexity is approximately:

```text
O(n * m)
```

and its additional memory usage is:

```text
O(n * m)
```

## Two Rows

`TwoRowEditDistance`

This version computes the same Edit Distance value but stores only two rows of the dynamic programming table.

Its additional memory usage is reduced to approximately:

```text
O(m)
```

where `m` is the length of the shorter input string.

---

# String Search

The project contains two search algorithms.

## Naive Search

`NaiveSearch`

The algorithm checks every possible starting position in the text and compares the pattern character by character.

## KMP

`KmpSearch`

KMP first builds an LPS array and then uses it during the search.

The public method:

```java
public int[] lps(String pattern)
```

allows the LPS preprocessing to be tested separately.

Both search algorithms return all matching start positions, including overlapping matches.

---

# Counters

Execution time can vary depending on the computer and JVM state.

For this reason, the project also records operation counters inside the algorithms.

For Edit Distance:

- Recursive counts recursive calls.
- Dynamic programming versions count processed states.

For String Search:

- Naive and KMP count character comparisons performed during their work.

The counters are saved together with execution time in `results/results.csv`.

---

# Experiment Inputs

Edit Distance uses:

- equal strings;
- completely different strings;
- random strings.

String Search uses:

- random text;
- repeated worst-case-like input;
- alternating input.

The repeated search input is intentionally difficult for Naive Search because many characters match before a mismatch occurs near the end of the pattern.

---

# Report

The final experiment explanation is available in:

```text
REPORT.md
```

It contains:

- Experiment Design Card
- plots
- results
- prediction vs result
- answers to the assignment questions
- explanation of timing measurements
- conclusions

---

# GitHub

Repository:

```text
https://github.com/JustMel1508/daa4.git
```

Final work is kept on the:

```text
main
```

branch.

The final version will be tagged:

```text
v1.0
```

---

