package simulation;

public abstract class Ball implements Collisionable<Float>{
    protected   Pair<Float> position;
    protected   Pair<Float> velocity;
    protected float mass;
    protected float radius;

    protected int totalCollisions;

    public Ball() {
    }

    public Ball(Pair<Float> position, Pair<Float> velocity, float mass, float radius) {
        this.position = position;
        this.velocity = velocity;
        this.mass = mass;
        this.radius = radius;
        this.totalCollisions = 0;
    }

    public Pair<Float> getPosition() {
        return position;
    }

    public Pair<Float> getVelocity() {
        return velocity;
    }

    public void setVelocity(Pair<Float> velocity) {
        this.velocity = velocity;
    }

    public float getMass() {
        return mass;
    }

    @Override
    public Float getRadius() {
        return radius;
    }

    @Override
    public int getTotalCollisions() {
        return totalCollisions;
    }

    public void incrementTotalCollisions() {
        this.totalCollisions++;
    }

    public float distanceTo(Ball ball) {
        float diffX = this.getPosition().getX() - ball.getPosition().getX();
        float diffY = this.getPosition().getY() - ball.getPosition().getY();

        return (float) Math.sqrt(Math.pow(diffX, 2) + Math.pow(diffY, 2)) - this.getRadius() - ball.getRadius();
    }

    abstract public boolean isPocket();

    abstract public void updatePosition(Float time);
}
