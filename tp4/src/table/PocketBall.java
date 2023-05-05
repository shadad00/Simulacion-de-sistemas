package table;

import utils.Pair;

public class PocketBall extends Ball {

    public PocketBall(Pair position, Pair velocity, Double mass, Double radius) {
        super(position, velocity, new Pair(0.,0.),new Pair(0.,0.),
                mass, radius);
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
