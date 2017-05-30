package student;

import java.util.*;

/**
 * Created by valen_000 on 29. 3. 2017.
 */

public class Variable {

    private static final char NONE = '_';

    private int id, size;
    private boolean row, col;
    private Value assigned_value;
    private Queue<Value> domain;
    private Stack<Value> removed_values;
    private List<Constraint> constraints;
    private Map<Integer, Integer> track;


    Variable(int id, int size, boolean row, boolean col) {
        this.id = id;
        this.size = size;
        this.row = row;
        this.col = col;
        this.assigned_value = null;
        this.constraints = new ArrayList<>();
        this.domain = new PriorityQueue<>(new value_comparator());
        this.removed_values = new Stack<>();
        this.track = new HashMap<>();
    }

    public void push_to_removed(Value val, int step) {
        int count = this.track.containsKey(step) ? this.track.get(step) : 0;
        this.track.put(step, count + 1);
        this.removed_values.push(val);
    }

    public void pop_from_removed(int step) {
        if(this.track.containsKey(step)) {
            for(int i = 0; i < this.track.get(step); i++) {
                this.domain.add(this.removed_values.pop());
            }
            this.track.remove(step);
        }
    }

    public void set_constraint(Constraint constr) {
        this.constraints.add(constr);
    }

    public List<Constraint> get_constraints() {
        return this.constraints;
    }

    public void set_domain() {
        this.find_values(0,0, NONE, new char[this.size]);
    }

    public Queue<Value> get_domain() {
        return this.domain;
    }

    public void print_domain() {
        for(Value value : this.domain) {
            System.out.println(value.get());
        }
    }

    public void set_value(Value val) {
        this.assigned_value = val;
    }

    public Value get_value() {
        return this.assigned_value;
    }

    private void find_values(int index, int constr_id, char prev, char[] val) {
        if(index < this.size) {
            if(constr_id < this.constraints.size()) {
                Constraint constr = this.constraints.get(constr_id);
                if(constr.get_length() + index <= this.size && (prev == NONE || (prev != NONE && prev != constr.get_color()))) {
                    for(int i = 0; i < constr.get_length(); i++) {
                        val[index + i] = constr.get_color();
                    }
                    find_values(index + constr.get_length(), constr_id + 1, constr.get_color(), val);
                }
            }
            val[index] = NONE;
            find_values(index + 1, constr_id, NONE, val);
        } else {
            if(constr_id >= this.constraints.size()) {
                this.domain.add(new Value(val.clone()));
            }
        }
    }

    public int get_id() {
        return this.id;
    }

    public int get_size() {
        return this.size;
    }

    public boolean is_row() {
        return this.row;
    }

    public boolean is_col() {
        return this.col;
    }

    private class value_comparator implements Comparator<Value> {
        @Override
        public int compare(Value val_1, Value val_2) {
            if(val_1.get_rank() > val_2.get_rank()) {
                return 1;
            }
            if(val_1.get_rank() < val_2.get_rank()) {
                return -1;
            }
            return 0;
        }
    }

}