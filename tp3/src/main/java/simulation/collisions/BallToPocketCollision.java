package simulation.collisions;

import simulation.CommonBall;
import simulation.PocketBall;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BallToPocketCollision<T extends Number> extends Collision<T> {

    private final int collisionsBall;
    public BallToPocketCollision(final T collisionTime, final CommonBall ball1, final PocketBall pocket) {
        super(collisionTime, ball1, pocket, false, false);

        collisionsBall = ball1.getTotalCollisions();
    }


    @Override
    public boolean isInvalidated() {
        return collisionsBall < getBall().getTotalCollisions();
    }

    @Override
    public void collide() {
        getBall().incrementTotalCollisions();
    }

    @Override
    public Set<CommonBall> getCollisionBalls() {
        return Stream.of(getBall()).collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return String.format(
                "Collision between <%s, pocket>",
                getBall().getBallNumber()
        );
    }
}
