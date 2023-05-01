package utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pair<T extends Number> {
    private T x;
    private T y;

    public Pair(final T x, final T y) {
        this.x = x;
        this.y = y;
    }

    public static Pair<Double> of(final double x, final double y) {
        return new Pair<>(x, y);
    }

    @Override
    public String toString() {
        return String.format("{%s, %s}", x, y);
    }
}
