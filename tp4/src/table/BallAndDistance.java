package table;

import java.util.Objects;

public class BallAndDistance {
    private final CommonBall otherBall;
    private final double distance;

    public BallAndDistance(CommonBall otherBall, double distance) {
        this.otherBall = otherBall;
        this.distance = distance;
    }


    public CommonBall getOtherBall() {
        return otherBall;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BallAndDistance that = (BallAndDistance) o;
        return Double.compare(that.distance, distance) == 0 && Objects.equals(otherBall, that.otherBall);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.otherBall);
    }

    public double getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "{" +
                otherBall.getBallNumber() +
                ", " + distance +
                '}';
    }
}
