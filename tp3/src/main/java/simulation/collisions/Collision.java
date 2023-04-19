package simulation.collisions;

import simulation.Collisionable;
import simulation.CommonBall;
import simulation.PocketBall;

import java.util.Objects;
import java.util.Set;

// Refactor to event
public abstract class Collision<T extends Number> implements Comparable<Collision<T>> {
    private final T collisionTime;
    private final CommonBall ball;
    private final Collisionable<Float> collisionable;
    private final boolean withVerticalWall;
    private final boolean withHorizontalWall;

    protected Collision(T collisionTime, CommonBall ball, Collisionable<Float> collisionable, boolean withVerticalWall, boolean withHorizontalWall) {
        this.collisionTime = collisionTime;
        this.ball = ball;
        this.collisionable = collisionable;
        this.withVerticalWall = withVerticalWall;
        this.withHorizontalWall = withHorizontalWall;
    }

    /**
     * Creates a collision between two balls. The collision will always have the balls sorted by their number
     *  nevertheless the order you pass them.
     * @param collisionTime
     * @param ball
     * @param anotherBall
     * @return
     */
     public static <T extends Number> Collision<T> withAnotherBall(T collisionTime, final CommonBall ball, final CommonBall anotherBall) {
        final CommonBall ball1 = ball.getBallNumber() < anotherBall.getBallNumber() ? ball : anotherBall;
        final CommonBall ball2 = ball.getBallNumber() < anotherBall.getBallNumber() ? anotherBall : ball;

        return new BallToBallCollision<>(collisionTime, ball1, ball2);
    }

    public static Collision<Float> withPocket(float collisionTime, final CommonBall ball, final PocketBall pocket) {
        return new BallToPocketCollision<>(collisionTime, ball, pocket);
    }

    public static Collision<Float> withVerticalWall(float collisionTime, final CommonBall ball) {
        return new BallToVerticalWallCollision<>(collisionTime, ball);
    }

    public static Collision<Float> withHorizontalWall(float collisionTime, final CommonBall ball) {
        return new BallToHorizontalWallCollision<>(collisionTime, ball);
    }

    public T getCollisionTime() {
        return collisionTime;
    }

    public CommonBall getBall() {
        return ball;
    }

    public Collisionable<Float> getCollisionable() {
        return collisionable;
    }

    /**
     * Checks if the collision has been calculated for entities that already collided in another collision.
     * @return true if the collision should be ignored.
     */
    public abstract boolean isInvalidated();

    public abstract void collide();

    /**
     * Returns the balls that participated in the collision.
     * @return a Set with balls.
     */
    public abstract Set<CommonBall> getCollisionBalls();

    public boolean isWithAnotherBall() {
        return collisionable != null && !collisionable.isPocket();
    }

    public boolean isWithPocket() {
        return collisionable != null && collisionable.isPocket();
    }

    public boolean isWithVerticalWall() {
        return withVerticalWall;
    }

    public boolean isWithHorizontalWall() {
        return withHorizontalWall;
    }

    @Override
    public String toString() {
        return "collisions.Collision{" +
                "collisionTime=" + collisionTime +
                ", ball=" + ball +
                ", collisionable=" + collisionable +
                ", withVerticalWall=" + withVerticalWall +
                ", withHorizontalWall=" + withHorizontalWall +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Collision<?> collision = (Collision<?>) o;
        return Float.compare(collision.collisionTime.floatValue(), collisionTime.floatValue()) == 0 && withVerticalWall == collision.withVerticalWall && withHorizontalWall == collision.withHorizontalWall && Objects.equals(ball, collision.ball) && Objects.equals(collisionable, collision.collisionable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(collisionTime, ball, collisionable, withVerticalWall, withHorizontalWall);
    }

    @Override
    public int compareTo(final Collision collision) {
        return Float.compare(this.getCollisionTime().floatValue(), collision.getCollisionTime().floatValue());
    }
}
