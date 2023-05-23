package simulation;


import utils.Pair;

public abstract class Ball {
    protected Pair position;
    protected Pair velocity;

    protected Pair acceleration;
    protected Pair force;

    protected Double mass;
    protected Double radius;

    public Ball(){}

    public Ball(Pair position, Pair velocity, Pair acceleration, Pair force
            , Double mass, Double radius) {
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

    public Pair getPosition() {
        return position;
    }

    public void setPosition(Pair position) {
        this.position = position;
    }

    public Pair getVelocity() {
        return velocity;
    }

    public void setVelocity(Pair velocity) {
        this.velocity = velocity;
    }

    public Pair getForce() {
        return force;
    }

    public void setForce(Pair force) {
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

    public Pair getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Pair acceleration) {
        this.acceleration = acceleration;
    }

}
