package simulation;

import simulation.collisions.Collision;

public class CommonBall extends Ball {
    private final int ballNumber;

    public CommonBall(final int ballNumber, Pair<Double> position, Pair<Double> velocity, final Double mass, final Double radius, int collision) {
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
    
    public CommonBall(final int ballNumber, Pair<Double> position, Pair<Double> velocity, final Double mass, final Double radius) {
        super(position, velocity, mass, radius);

        this.ballNumber = ballNumber;
    }

    public static CommonBall buildWhiteBall(Pair<Double> position, Pair<Double> velocity, final Double mass, final Double radius) {
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
    public static CommonBall buildColoredBall(final int ballNumber, Pair<Double> position, final Double mass, final Double radius) {
        return new CommonBall(ballNumber, position, new Pair<>(0., 0.), mass, radius);
    }

    @Override
    public void updatePosition(Double time) {
        Double newX = this.position.getX() + time * this.velocity.getX();
        Double newY = this.position.getY() + time * this.velocity.getY();
        this.position.setX(newX);
        this.position.setY(newY);
    }

    public Double getCollisionTime(Collisionable<Double> other) {

        final double sigma = this.getRadius() + other.getRadius();
        final double deltaX = this.getPosition().getX() - other.getPosition().getX();
        final double deltaY = this.getPosition().getY() - other.getPosition().getY();
        final double deltaVelX = this.getVelocity().getX() - other.getVelocity().getX();
        final double deltaVelY = this.getVelocity().getY() - other.getVelocity().getY();

        final double deltaRSquared = Math.pow(deltaX, 2) + Math.pow(deltaY, 2);
        final double deltaVelSquared = Math.pow(deltaVelX, 2) + Math.pow(deltaVelY, 2);
        final double deltaVelR = deltaX * deltaVelX + deltaY * deltaVelY;

        final double d = Math.pow(deltaVelR, 2) - deltaVelSquared * (deltaRSquared - Math.pow(sigma, 2));

        if (deltaVelR >= 0 || d < 0)
            return null;

        return -((deltaVelR + Math.sqrt(d)) / (deltaVelSquared));
    }

    public Collision<Double> getCollision(final CommonBall ball, final double simulationTime) {
        final Double time = getCollisionTime(ball);
        if (time == null)
            return null;

        return Collision.withAnotherBall(simulationTime + time, this, ball);
    }

    public Collision<Double> getCollision(final PocketBall pocket, final double simulationTime) {
        final Double time = getCollisionTime(pocket);
        if (time == null)
            return null;

        return Collision.withPocket(simulationTime + time, this, pocket);
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
