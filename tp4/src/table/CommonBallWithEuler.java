package table;

import utils.Pair;

public class CommonBallWithEuler extends CommonBall{

    private Pair lastPosition = new Pair(0.,0.);

    public CommonBallWithEuler(CommonBall other) {
        super(other);
    }

    public CommonBallWithEuler(int ballNumber, Pair position, Pair velocity, Pair acceleration, Pair force, Double mass, Double radius) {
        super(ballNumber, position, velocity, acceleration, force, mass, radius);
    }


    @Override
    public void updateWithPrediction(Double dt) {
        lastPosition = position;
        position.setX(position.getX() + velocity.getX() * dt);
        position.setY(position.getY() + velocity.getY() * dt);
    }

    @Override
    public void correctPrediction(Double dt) {
        acceleration.setX(force.getX() / mass);
        acceleration.setY(force.getY() / mass);
        velocity.setX(velocity.getX() + acceleration.getX() * dt);
        velocity.setY(velocity.getY() + acceleration.getY() * dt);
        position.setX(lastPosition.getX() + velocity.getX() * dt );
        position.setY(lastPosition.getY() + velocity.getY() * dt);
    }


    public static CommonBallWithEuler buildWhiteBall(Pair position, Pair velocity, final Double mass, final Double radius) {
        return new CommonBallWithEuler(0, position, velocity,
                new Pair(0., 0.),new Pair(0., 0.),
                mass, radius);
    }

    public static CommonBallWithEuler buildColoredBall(final int ballNumber, Pair position, final Double mass, final Double radius) {
        return new CommonBallWithEuler(ballNumber, position, new Pair(0., 0.),
                new Pair(0., 0.),new Pair(0., 0.),
                mass, radius);
    }

}
