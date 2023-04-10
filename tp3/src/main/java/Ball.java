public abstract class Ball implements Collisionable<Double>{
    protected   Pair<Double> position;
    protected   Pair<Double> velocity;
    private final Double mass;
    private final Double radius;

    public Ball(Pair<Double> position, Pair<Double> velocity, Double mass, Double radius) {
        this.position = position;
        this.velocity = velocity;
        this.mass = mass;
        this.radius = radius;
    }

    public Pair<Double> getPosition() {
        return position;
    }

    public Pair<Double> getVelocity() {
        return velocity;
    }

    public Double getMass() {
        return mass;
    }

    @Override
    public Double getRadius() {
        return radius;
    }


    abstract public void collide(Collisionable other);

    abstract public boolean isPocket();

    abstract public void updatePosition(Double time);
}
