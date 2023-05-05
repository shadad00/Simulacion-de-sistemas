package simulation;

public abstract class Ball implements Collisionable<Double>{
    protected Pair<Double> position;
    protected Pair<Double> velocity;
    protected Double mass;
    protected Double radius;

    protected int totalCollisions;

    public Ball() {
    }

    public Ball(Pair<Double> position, Pair<Double> velocity, Double mass, Double radius) {
        this.position = position;
        this.velocity = velocity;
        this.mass = mass;
        this.radius = radius;
        this.totalCollisions = 0;
    }

    public Pair<Double> getPosition() {
        return position;
    }

    public Pair<Double> getVelocity() {
        return velocity;
    }

    public void setVelocity(Pair<Double> velocity) {
        this.velocity = velocity;
    }

    public Double getMass() {
        return mass;
    }

    @Override
    public Double getRadius() {
        return radius;
    }

    @Override
    public int getTotalCollisions() {
        return totalCollisions;
    }

    public void incrementTotalCollisions() {
        this.totalCollisions++;
    }

    public double distanceTo(Ball ball) {
        double diffX = this.getPosition().getX() - ball.getPosition().getX();
        double diffY = this.getPosition().getY() - ball.getPosition().getY();

        return Math.sqrt(Math.pow(diffX, 2) + Math.pow(diffY, 2)) - this.getRadius() - ball.getRadius();
    }

    abstract public boolean isPocket();

    abstract public void updatePosition(Double time);
}
