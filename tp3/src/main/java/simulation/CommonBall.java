package simulation;

import simulation.collisions.Collision;

public class CommonBall extends Ball {
    private final int ballNumber;

    public CommonBall(final int ballNumber, Pair<Float> position, Pair<Float> velocity, final float mass, final float radius, int collision) {
        this(ballNumber, position, velocity, mass,  radius);
        this.totalCollisions = collision;
    }

    public CommonBall(CommonBall other){
        super();
        this.position = new Pair<>(other.position.getX(), other.getPosition().getY());
        this.velocity = new Pair<>(other.velocity.getX(), other.velocity.getY());
        this.mass = other.mass;
        this.radius = other.radius;
        this.totalCollisions = other.totalCollisions;
        this.ballNumber = other.ballNumber;
    }
    
    public CommonBall(final int ballNumber, Pair<Float> position, Pair<Float> velocity, final float mass, final float radius) {
        super(position, velocity, mass, radius);

        this.ballNumber = ballNumber;
    }

    public static CommonBall buildWhiteBall(Pair<Float> position, Pair<Float> velocity, final float mass, final float radius) {
        return new CommonBall(0, position, velocity, mass, radius);
    }

    /**
     * Builds a colored ball. Assumes colored balls will always be still, no velocity at their creation.
     * @param ballNumber    A number between 1 and 15.
     * @param position      Position of the ball.
     * @param mass          Mass of the ball.
     * @param radius        Radius of the ball.
     * @return a simulation.CommonBall.
     */
    public static CommonBall buildColoredBall(final int ballNumber, Pair<Float> position, final float mass, final float radius) {
        return new CommonBall(ballNumber, position, new Pair<>(0.f, 0.f), mass, radius);
    }


    public Float getCollisionTime(Collisionable<Float> other) {

        final float sigma = this.getRadius() + other.getRadius();
        final float deltaX = this.getPosition().getX() - other.getPosition().getX();
        final float deltaY = this.getPosition().getY() - other.getPosition().getY();
        final float deltaVelX = this.getVelocity().getX() - other.getVelocity().getX();
        final float deltaVelY = this.getVelocity().getY() - other.getVelocity().getY();

        final float deltaRSquared = (float) (Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
        final float deltaVelSquared = (float) (Math.pow(deltaVelX, 2) + Math.pow(deltaVelY, 2));
        final float deltaVelR = deltaX * deltaVelX + deltaY * deltaVelY;

        final float d = (float) (Math.pow(deltaVelR, 2) - deltaVelSquared * (deltaRSquared - Math.pow(sigma, 2)));

        if (deltaVelR >= 0 || d < 0)
            return null;

        return (float) (-((deltaVelR + Math.sqrt(d)) / (deltaVelSquared)));
    }

    public Collision<Float> getCollision(final CommonBall ball, final float simulationTime) {
        final Float time = getCollisionTime(ball);
        if (time == null)
            return null;

        return Collision.withAnotherBall(simulationTime + time, this, ball);
    }

    public Collision<Float> getCollision(final PocketBall pocket, final float simulationTime) {
        final Float time = getCollisionTime(pocket);
        if (time == null)
            return null;

        return Collision.withPocket(simulationTime + time, this, pocket);
    }

    @Override
    public void updatePosition(Float time) {
        float newX = this.position.getX() + time * this.velocity.getX();
        float newY = this.position.getY() + time * this.velocity.getY();
        this.position.setX(newX);
        this.position.setY(newY);
    }

    @Override
    public boolean isPocket() {
        return false;
    }



    public int getBallNumber() {
        return ballNumber;
    }

    @Override
    public String toString() {
        return "simulation.CommonBall{" +
                "ballNumber=" + ballNumber +
                ", position=" + position +
                ", velocity=" + velocity +
                '}';
    }
}
