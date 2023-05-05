package table;


import utils.Pair;

public abstract class Ball {
    protected Pair<Double> position;
    protected Pair<Double> velocity;

    protected Pair<Double> acceleration;
    protected Pair<Double> force;

    protected Double mass;
    protected Double radius;

    public Ball(){}

    public Ball(Pair<Double> position, Pair<Double> velocity, Pair<Double> acceleration, Pair<Double> force
            ,Double mass, Double radius) {
        this.position = position;
        this.velocity = velocity;
        this.acceleration = acceleration;
        this.force = force;
        this.mass = mass;
        this.radius = radius;
    }


    public double distanceTo(Ball ball) {
        double diffX = this.getPosition().getX() - ball.getPosition().getX();
        double diffY = this.getPosition().getY() - ball.getPosition().getY();

        return Math.sqrt(Math.pow(diffX, 2) + Math.pow(diffY, 2)) - this.getRadius() - ball.getRadius();
    }

    abstract public boolean isPocket();

    abstract public void updatePosition(Double dt);

    public Pair<Double> getPosition() {
        return position;
    }

    public void setPosition(Pair<Double> position) {
        this.position = position;
    }

    public Pair<Double> getVelocity() {
        return velocity;
    }

    public void setVelocity(Pair<Double> velocity) {
        this.velocity = velocity;
    }

    public Pair<Double> getForce() {
        return force;
    }

    public void setForce(Pair<Double> force) {
        this.force = force;
    }

    public Double getMass() {
        return mass;
    }

    public void setMass(Double mass) {
        this.mass = mass;
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }

    public Pair<Double> getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Pair<Double> acceleration) {
        this.acceleration = acceleration;
    }
}
