package simulation;

public class PocketBall extends Ball{

    public PocketBall(Pair<Float> position, Pair<Float> velocity, float mass, float radius) {
        super(position, velocity, mass, radius);
    }

    @Override
    public void updatePosition(Float time) {
        return;
    }


    @Override
    public boolean isPocket() {
        return true;
    }


}
