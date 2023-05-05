package utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pair {
    private double x;
    private double y;

    public static final Pair ZERO = Pair.of(0., 0.);

    public Pair(final double x, final double y) {
        this.x = x;
        this.y = y;
    }

    public static Pair of(final double x, final double y) {
        return new Pair(x, y);
    }

    public void add(final Pair otherForce) {
        this.x += otherForce.getX();
        this.y += otherForce.getY();
    }

    @Override
    public String toString() {
        return String.format("{%s, %s}", x, y);
    }
}
