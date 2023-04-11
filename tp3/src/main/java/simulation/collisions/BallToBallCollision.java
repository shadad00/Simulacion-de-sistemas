package simulation.collisions;

import simulation.CommonBall;
import simulation.Pair;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BallToBallCollision extends Collision {

    private int collisionsBall1;
    private int collisionsBall2;

    public BallToBallCollision(final double collisionTime, final CommonBall ball1, final CommonBall ball2) {
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

        final double sigma = ball1.getRadius().doubleValue() + ball2.getRadius().doubleValue();
        final double deltaX = ball1.getPosition().getX().doubleValue() - ball2.getPosition().getX().doubleValue();
        final double deltaY = ball1.getPosition().getY().doubleValue() - ball2.getPosition().getY().doubleValue();
        final double deltaVelX = ball1.getVelocity().getX().doubleValue() - ball2.getVelocity().getX().doubleValue();
        final double deltaVelY = ball1.getVelocity().getY().doubleValue() - ball2.getVelocity().getY().doubleValue();

        final double deltaVelR = deltaX * deltaVelX + deltaY * deltaVelY;

        final double j = 2 * ball1.getMass() * getCollisionableBall().getMass() * deltaVelR / (sigma * (getBall().getMass() + getCollisionableBall().getMass()));
        final double jX = j * deltaX / sigma;
        final double jY = j * deltaY / sigma;

        final Pair<Double> newVel1 = new Pair<>(
                ball1.getVelocity().getX() - jX / ball1.getMass(),
                ball1.getVelocity().getY() - jY / ball1.getMass()
        );

        final Pair<Double> newVel2 = new Pair<>(
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
