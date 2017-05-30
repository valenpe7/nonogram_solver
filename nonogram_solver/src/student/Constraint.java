package student;

/**
 * Created by valen_000 on 29. 3. 2017.
 */

public class Constraint {

    private int length;
    private char color;

    Constraint(int length, char color) {
        this.length = length;
        this.color = color;
    }

    public void set_length(int length) {
        this.length = length;
    }

    public int get_length() {
        return this.length;
    }

    public void set_color(char color) {
        this.color = color;
    }

    public char get_color() {
        return this.color;
    }

}