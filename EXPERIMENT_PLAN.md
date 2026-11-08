# Experiment Design Card

## 1. Edit Distance

### Question

How does the running cost of the three Edit Distance implementations change as the input size increases?

I also want to compare how much extra memory is required by the full DP table and the two-row implementation.

### Sizes

For Recursive Edit Distance:

n = 2, 4, 6, 8, 10

The recursive version grows very quickly, so small sizes are used to avoid extremely long execution times.

For Table and Two Rows:

n = 128, 256, 512, 1024

These sizes are large enough to observe the O(n * m) behaviour.

### Inputs

Input type 1: Equal strings.

Example:

AAAA
AAAA

This input shows a simple case where corresponding characters are equal.

Input type 2: Completely different strings.

Example:

AAAA
BBBB

This input is intended to make the algorithm perform more work because characters do not match.

Input type 3: Random strings.

Random strings are generated with a fixed seed.

### Metrics

I will measure:

- execution time in milliseconds;
- number of operations performed inside the algorithm.

For Recursive Edit Distance, the counter represents recursive calls.

For the DP versions, the counter represents processed DP states.

### Prediction

I expect the recursive implementation to become much slower as n grows because it repeatedly solves the same subproblems.

I expect both DP versions to have approximately quadratic time growth.

I expect the two-row version to use much less memory than the full table.

---

## 2. String Search

### Question

How do Naive Search and KMP behave on different text types as the text length increases?

### Sizes

Text sizes:

n = 2048, 4096, 8192, 16384

Pattern sizes will also increase with the text size.

### Inputs

Input type 1: Random text.

This represents an average-like case.

Input type 2: Repeated characters.

Example:

Text:
AAAAAAAAAAAAAAAA

Pattern:
AAAAAAAB

This is intended to be difficult for Naive Search because many characters match before the final mismatch.

Input type 3: Alternating characters.

Example:

ABABABABABABABAB

This is useful for showing repeated prefixes and suffixes that KMP can reuse.

### Metrics

I will measure:

- execution time in milliseconds;
- number of character comparisons.

The comparison counter is increased inside both algorithms.

### Prediction

I expect Naive Search to perform many repeated comparisons on difficult repeated-character inputs.

I expect KMP to perform fewer comparisons because the LPS array lets it reuse information from previous matches.

---

## Planned Plots

### Plot 1

Recursive Edit Distance operation count vs input size.

Purpose:
Show how quickly the recursive approach grows.

### Plot 2

Table vs Two Rows execution time vs input size.

Purpose:
Compare the runtime behaviour of the two dynamic programming versions.

### Plot 3

Naive vs KMP character comparisons vs text size.

Purpose:
Show how KMP avoids repeated comparisons.

### Plot 4

Naive vs KMP execution time vs text size.

Purpose:
Compare real measured running time.

At least one plot will use a logarithmic scale or a ratio so that growth differences are easier to see.