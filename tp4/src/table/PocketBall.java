package table;

import simulation.Ball;
import simulation.Pair;

public class PocketBall extends Ball {

    public PocketBall(Pair<Double> position, Pair<Double> velocity, Double mass, Double radius) {
        super(position, velocity, mass, radius);
    }

    @Override
    public void updatePosition(Double dt) {
        return;
    }

    @Override
    public boolean isPocket() {
        return true;
    }


}
