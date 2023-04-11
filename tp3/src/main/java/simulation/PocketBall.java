package simulation;

public class PocketBall extends Ball{

    public PocketBall(Pair<Double> position, Pair<Double> velocity, Double mass, Double radius) {
        super(position, velocity, mass, radius);
    }

    @Override
    public void updatePosition(Double time) {
        return;
    }

    @Override
    public boolean isPocket() {
        return true;
    }


}
