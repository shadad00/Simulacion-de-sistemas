// Refactor to event
public class Collision<T extends Number> {
    private double collisionTime;
    private Ball ball;
    private Collisionable collisionable;

    private boolean withVerticalWall;
    private boolean withHorizontalWall;

    public Collision(double collisionTime, CommonBall ball, Collisionable collisionable) {
        this.collisionTime = collisionTime;
        this.ball = ball;
        this.collisionable = collisionable;
    }

    public Collision(double collisionTime, CommonBall ball, boolean withVerticalWall, boolean withHorizontalWall) {
        this.collisionTime = collisionTime;
        this.ball = ball;
        this.collisionable = null;
        this.withVerticalWall = withVerticalWall;
        this.withHorizontalWall = withHorizontalWall;
    }

    public double getCollisionTime() {
        return collisionTime;
    }

    public boolean withPocket() {
        return collisionable.isPocket();
    }

    public boolean withVerticalWall() {
        return withVerticalWall;
    }

    public boolean withHorizontalWall() {
        return withHorizontalWall;
    }

    public void apply() {
        if (collisionable2.isPocket()) {

        } else (!collisionable2.isPocket()) {
            collisionable1.collide(collisionable2);
        } else {
            if (collideWallX) {
                collisionable1.updateXVelocity(-1);
            }

            if (collideWallY) {
                collisionable1.updateYVelocity(-1);
            }
        }
    }
}
