package simulation.collisions;

import simulation.CommonBall;
import simulation.Pair;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BallToBallCollision<T extends Number> extends Collision<T> {

    private final int collisionsBall1;
    private final int collisionsBall2;

    public BallToBallCollision(final T collisionTime, final CommonBall ball1, final CommonBall ball2) {
        super(collisionTime, ball1, ball2, false, false);

        collisionsBall1 = ball1.getTotalCollisions();
        collisionsBall2 = ball2.getTotalCollisions();
    }

    @Override
    public boolean isInvalidated() {
        return collisionsBall1 < getBall().getTotalCollisions()
                || collisionsBall2 < getCollisionable().getTotalCollisions();
    }

    public CommonBall getCollisionableBall() {
        return (CommonBall) getCollisionable();
    }

    @Override
    public void collide() {
        // TODO: esto es horrible, deberiamos guardar estos valores cuando los usamos para saber si colisionan o no
        final CommonBall ball1 = getBall();
        final CommonBall ball2 = getCollisionableBall();

        final float sigma = ball1.getRadius() + ball2.getRadius();
        final float deltaX = ball1.getPosition().getX() - ball2.getPosition().getX();
        final float deltaY = ball1.getPosition().getY() - ball2.getPosition().getY();
        final float deltaVelX = ball1.getVelocity().getX() - ball2.getVelocity().getX();
        final float deltaVelY = ball1.getVelocity().getY() - ball2.getVelocity().getY();

        final float deltaVelR = deltaX * deltaVelX + deltaY * deltaVelY;

        final float j = 2 * ball1.getMass() * getCollisionableBall().getMass() * deltaVelR / (sigma * (getBall().getMass() + getCollisionableBall().getMass()));
        final float jX = j * deltaX / sigma;
        final float jY = j * deltaY / sigma;

        final Pair<Float> newVel1 = new Pair<>(
                ball1.getVelocity().getX() - jX / ball1.getMass(),
                ball1.getVelocity().getY() - jY / ball1.getMass()
        );

        final Pair<Float> newVel2 = new Pair<>(
                ball2.getVelocity().getX() + jX / ball2.getMass(),
                ball2.getVelocity().getY() + jY / ball2.getMass()
        );

        ball1.setVelocity(newVel1);
        ball2.setVelocity(newVel2);

        ball1.incrementTotalCollisions();
        ball2.incrementTotalCollisions();
    }

    @Override
    public Set<CommonBall> getCollisionBalls() {
        return Stream.of(getBall(), getCollisionableBall()).collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return String.format(
                "Collision between <%s, %s>",
                getBall().getBallNumber(),
                getCollisionableBall().getBallNumber()
        );
    }
}
