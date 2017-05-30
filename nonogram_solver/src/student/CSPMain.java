package student;

/**
 * Created by valen_000 on 29. 3. 2017.
 */

public class CSPMain {

    /**
     * Formalization:
     *
     * Variables - individual rows and columns of given task
     * Domains - each variable has its own domain determined by all possible combinations of blocks placing (independently of other variables)
     * Constraints - j-th value of i-th row must be equal to i-th value of j-th column
     *
     * CSP algorithms/techniques implemented:
     *
     * Recursive backtracking - searching for all solutions of given task
     * Forward checking - reducing variable domains after each assignment and checking whether no domain is empty
     * Arc consistency (AC-3) - runs after each assignment to identify further relationships between updated variable domains and potentially reduce their size even more
     * Heuristics for variables - most constrained variable heuristics (the least number of values remaining in its domain)
     * Heuristics for values - there is no heuristics for values in domain implemented
     *
     */

    public static void main(String[] args) {
        CSPSolver solver = new CSPSolver();
        solver.solve_task();
    }

}
