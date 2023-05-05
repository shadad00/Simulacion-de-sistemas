package table;

import lombok.Getter;
import lombok.Setter;
import simulation.Collisionable;
import simulation.Pair;

@Getter
@Setter
public abstract class Ball implements Collisionable<Double> {
    protected Pair<Double> position;
    protected Pair<Double> velocity;
    protected Pair<Double> Force;

    protected Double mass;
    protected Double radius;


    public Ball(Pair<Double> position, Pair<Double> velocity,Double mass, Double radius) {
        this.position = position;
        this.velocity = velocity;
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
}
