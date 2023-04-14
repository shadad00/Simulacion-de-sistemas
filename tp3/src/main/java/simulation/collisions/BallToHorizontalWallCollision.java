package simulation.collisions;

import simulation.CommonBall;
import simulation.Pair;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BallToHorizontalWallCollision<T extends Number> extends Collision<T> {

    private final int ballCollisions;

    public BallToHorizontalWallCollision(final T collisionTime, CommonBall ball) {
        super(collisionTime, ball, null, false, true);

        ballCollisions = ball.getTotalCollisions();
    }

    @Override
    public boolean isInvalidated() {
        return ballCollisions < getBall().getTotalCollisions();
    }

    @Override
    public void collide() {
        final CommonBall ball = getBall();

        ball.setVelocity(new Pair<>(ball.getVelocity().getX(), -ball.getVelocity().getY()));
        ball.incrementTotalCollisions();
    }

    @Override
    public Set<CommonBall> getCollisionBalls() {
        return Stream.of(getBall()).collect(Collectors.toSet());
    }


    @Override
    public String toString() {
        return String.format(
                "Collision between <%s, horizontal wall>",
                getBall().getBallNumber()
        );
    }

}
