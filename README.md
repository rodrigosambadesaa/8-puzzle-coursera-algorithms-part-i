# 8-puzzle Coursera Algorithms Part I

Solution for the Princeton Algorithms Part I Slider Puzzle assignment.

## Files

- `Board.java`: immutable board data type.
- `Solver.java`: A* solver using Manhattan priority and `MinPQ`.
- `collinear.zip`: submission archive containing the two required source files.

## Compile and run

```text
javac -cp algs4.jar Board.java Solver.java
java -cp .;algs4.jar Solver puzzle04.txt
```

The assignment supplies `algs4.jar` separately.
