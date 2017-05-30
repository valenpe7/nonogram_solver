package student;

import java.io.*;
import java.util.*;

/**
 * Created by valen_000 on 29. 3. 2017.
 */

public class CSPSolver {

    private int num_rows, num_cols;
    private Variable[] rows, cols;
    private Queue<Variable> variables;
    private List<String> solutions;

    CSPSolver() {
        this.variables = new PriorityQueue<>(new variable_comparator());
        this.solutions = new ArrayList<>();
    }

    public void solve_task() {
        try {
            this.load_task();
        } catch (IOException exception) {
            System.out.println("IOException: " + exception.toString());
            System.exit(-1);
        }
        this.backtracking_search();
        this.print_solutions();
    }

    private void load_task() throws IOException {
        int line_number = 0;
        BufferedReader buf_read = new BufferedReader(new InputStreamReader(System.in));
        cycle: while(true) {
            String[] line = buf_read.readLine().split(",");
            if(line_number == 0) {
                this.num_rows = Integer.parseInt(line[0]);
                this.num_cols = Integer.parseInt(line[1]);
                this.rows = new Variable[this.num_rows];
                this.cols = new Variable[this.num_cols];
            }
            if(line_number > 0 && line_number <= this.num_rows) {
                int id = line_number - 1;
                this.rows[id] = new Variable(id, this.num_cols, true, false);
                for(int i = 0; i < line.length; i = i + 2) {
                    char color = line[i].charAt(0);
                    int length = Integer.parseInt(line[i + 1]);
                    this.rows[id].set_constraint(new Constraint(length, color));
                }
                this.rows[id].set_domain();
            }
            if(line_number > this.num_rows && line_number <= this.num_rows + this.num_cols) {
                int id = line_number - this.num_rows - 1;
                this.cols[id] = new Variable(id, this.num_rows, false, true);
                for(int i = 0; i < line.length; i = i + 2) {
                    char color = line[i].charAt(0);
                    int length = Integer.parseInt(line[i + 1]);
                    this.cols[id].set_constraint(new Constraint(length, color));
                }
                this.cols[id].set_domain();
            }
            if(line_number++ >= this.num_rows + this.num_cols) {
                break cycle;
            }
        }
    }

    private void backtracking_search() {
        this.recursive_backtracking(0);
    }

    private void recursive_backtracking(int step) {
        this.sort_variables();
        if(this.variables.isEmpty()) {
            this.add_solution();
            return;
        }
        Variable var = this.variables.poll();
        for(Value val : var.get_domain()) {
            this.assign_value(var, val);
            if(this.forward_checking(var, step)) {
                this.arc_consistency(step);
                this.recursive_backtracking(step + 1);
            }
            this.step_back(step);
            this.assign_value(var, null);
        }
    }

    private void sort_variables() {
        this.variables.clear();
        for(Variable row : this.rows) {
            if(row.get_value() == null) {
                this.variables.add(row);
            }
        }
        for(Variable col : this.cols) {
            if(col.get_value() == null) {
                this.variables.add(col);
            }
        }
    }

    private void assign_value(Variable var, Value val) {
        if(var.is_row()) {
            this.rows[var.get_id()].set_value(val);
        }
        if(var.is_col()) {
            this.cols[var.get_id()].set_value(val);
        }
    }

    private boolean forward_checking(Variable var, int step) {
        if(var.is_row()) {
            Value val_1 = this.rows[var.get_id()].get_value();
            for(Variable col : this.cols) {
                if(col.get_value() == null) {
                    Iterator<Value> domain = col.get_domain().iterator();
                    while(domain.hasNext()) {
                        Value val_2 = domain.next();
                        if(val_1.get()[col.get_id()] != val_2.get()[var.get_id()]) {
                            col.push_to_removed(val_2, step);
                            domain.remove();
                        }
                    }
                    if(col.get_domain().isEmpty()) {
                        return false;
                    }
                }
            }
        }
        if(var.is_col()) {
            Value val_1 = this.cols[var.get_id()].get_value();
            for(Variable row : this.rows) {
                if(row.get_value() == null) {
                    Iterator<Value> domain = row.get_domain().iterator();
                    while(domain.hasNext()) {
                        Value val_2 = domain.next();
                        if(val_1.get()[row.get_id()] != val_2.get()[var.get_id()]) {
                            row.push_to_removed(val_2, step);
                            domain.remove();
                        }
                    }
                    if(row.get_domain().isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void arc_consistency(int step) {
        Stack<Arc> stack = new Stack<>();
        for(Variable row : this.rows) {
            for (Variable col : this.cols) {
                if(row.get_value() == null && col.get_value() == null) {
                    stack.push(new Arc(row, col));
                    stack.push(new Arc(col, row));
                }
            }
        }
        while(!stack.isEmpty()) {
            Arc arc = stack.pop();
            if(arc.remove_inconsistent(step)) {
                if(arc.get_var1().is_row()) {
                    for(Variable col : this.cols) {
                        if(col.get_value() == null) {
                            stack.push(new Arc(col, arc.get_var1()));
                        }
                    }
                }
                if(arc.get_var1().is_col()) {
                    for(Variable row : this.rows) {
                        if(row.get_value() == null) {
                            stack.push(new Arc(row, arc.get_var1()));
                        }
                    }
                }
            }
        }
    }

    private void step_back(int step) {
        for(Variable col : this.cols) {
            col.pop_from_removed(step);
        }
        for(Variable row : this.rows) {
            row.pop_from_removed(step);
        }
    }

    private void add_solution() {
        String solution = new String();
        for(Variable row : this.rows) {
            solution += String.valueOf(row.get_value().get()) + "\n";
        }
        this.solutions.add(solution);
    }

    public void print_solutions() {
        if(this.solutions.isEmpty()) {
            System.out.println("null");
        } else {
            for(String solution : this.solutions) {
                System.out.println(solution);
            }
        }
    }

    private class variable_comparator implements Comparator<Variable> {
        @Override
        public int compare(Variable var_1, Variable var_2) {
            if(var_1.get_domain().size() > var_2.get_domain().size()) {
                return 1;
            }
            if(var_1.get_domain().size() < var_2.get_domain().size()) {
                return -1;
            }
            return 0;
        }
    }
}