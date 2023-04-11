package simulation;

public abstract class Ball implements Collisionable<Double>{
    protected   Pair<Double> position;
    protected   Pair<Double> velocity;
    private final Double mass;
    private final Double radius;

    private int totalCollisions;

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

    abstract public boolean isPocket();

    abstract public void updatePosition(Double time);
}
