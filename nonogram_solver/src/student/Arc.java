package student;

import java.util.*;

/**
 * Created by valen_000 on 30. 3. 2017.
 */

public class Arc {

    private Variable var_1;
    private Variable var_2;

    Arc(Variable var_1, Variable var_2) {
        this.var_1 = var_1;
        this.var_2 = var_2;
    }

    public boolean remove_inconsistent(int step) {
        boolean to_remove = true;
        boolean removed = false;
        Iterator<Value> domain = this.var_1.get_domain().iterator();
        while(domain.hasNext()) {
            Value val_1 = domain.next();
            to_remove = true;
            cycle: for(Value val_2 : this.var_2.get_domain()) {
                if(val_1.get()[this.var_2.get_id()] == val_2.get()[this.var_1.get_id()]) {
                    to_remove = false;
                    break cycle;
                }
            }
            if(to_remove) {
                this.var_1.push_to_removed(val_1, step);
                domain.remove();
                removed = true;
            }
        }
        return removed;
    }

    public void set_var1(Variable var_1) {
        this.var_1 = var_1;
    }

    public Variable get_var1() {
        return this.var_1;
    }

    public void set_var2(Variable var_2) {
        this.var_2 = var_2;
    }

    public Variable get_var2() {
        return this.var_2;
    }

}