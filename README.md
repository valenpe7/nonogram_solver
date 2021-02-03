# Nonogram solver

This program is part of the assessment work of the course "B4B36ZUI - Introduction to Artificial Intelligence" lectured at FEE CTU in Prague.

Nonograms (http://en.wikipedia.org/wiki/Nonogram) are brain teasers, defined by a legend linked to a grid used to draw a picture. Each number in the legend sets the number of the following boxes filled with color which belongs to the given number. The following rules hold:

- there is always at least one empty box between two blocks of the boxes filled with the same color
- there does not have to be an empty box between the boxes filled with different color
- the order of the numbers in the legend corresponds to the order of the blocks of the boxes (from left to right, from top to bottom)

### Input:

The program reads the file from the standard input that has the following format:

```
NUMBER_OF_ROWS,NUMBER_OF_COLUMNS
CONSTRAINTS_FOR_ROW_1
CONSTRAINTS_FOR_ROW_2
...
CONSTRAINTS_FOR_ROW_M
CONSTRAINTS_FOR_COLUMN_1
CONSTRAINTS_FOR_COLUMN_2
...
CONSTRAINTS_FOR_COLUMN_N
```

while each constraint has the format:

```
COLOR_1,NUMBER_1,COLOR_2,NUMBER_2,...,NUMBER_K
```

where the field "color" applies to the following number and is in a format of the character that represents the color you will use for drawing (for example "#"). The field "number" sets the size of the given block.

The input examples can be found in `/nonogram_solver/input` directory.

### Output:

- The solution is drawn line-by-line to the standard output
- The colored box is marked by the sign of the given color, the empty box is marked as "_"
- If multiple pictures satisfy the constraints, all of them are drawn and separated with an empty line
- If the solution does not exist, the output is "null"

<img src="https://github.com/valenpe7/nonogram_solver/blob/master/nonogram_solver.gif" width=250>

### Implementation details:

The problem is formalized as follows:

* Variables - individual rows and columns of given task
* Domains - each variable has its own domain determined by all possible combinations of blocks placing (independently of other variables)
* Constraints - j-th value of i-th row must be equal to i-th value of j-th column

The following CSP algorithms/techniques are used to speed up the computation:

* Recursive backtracking - searching for all solutions of given task
* Forward checking - reducing variable domains after each assignment and checking whether no domain is empty
* Arc consistency (AC-3) - runs after each assignment to identify further relationships between updated variable domains and potentially reduce their size even more
* Heuristics for variables - most constrained variable heuristics (the least number of values remaining in its domain)
