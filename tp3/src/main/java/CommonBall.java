import java.util.Optional;

public class CommonBall extends Ball{
    public CommonBall(Pair<Double> position, Pair<Double> velocity, Double mass, Double radius) {
        super(position, velocity, mass, radius);
    }

    @Override
    public void updatePosition(Double time) {
        Double newX = this.position.getX() + time * this.velocity.getX();
        Double newY = this.position.getY() + time * this.velocity.getY();
        this.position.setX(newX);
        this.position.setY(newY);
    }

    public Optional<Collision> getCollision(Collisionable other) {
        final double sigma = this.getRadius() - other.getRadius();
        final double deltaX = this.getPosition().getX() - other.getPosition().getX();
        final double deltaY = this.getPosition().getY() - other.getPosition().getY();
        final double deltaVelX = this.getVelocity().getX() - other.getVelocity().getX();
        final double deltaVelY = this.getVelocity().getY() - other.getVelocity().getY();

        final double deltaRSquared = Math.pow(deltaX, 2) + Math.pow(deltaY, 2);
        final double deltaVelSquared = Math.pow(deltaVelX, 2) + Math.pow(deltaVelY, 2);
        final double deltaVelR = deltaX * deltaVelX + deltaY * deltaVelY;

        final double d = Math.pow(deltaVelR, 2) - deltaVelSquared * (deltaRSquared - Math.pow(sigma, 2))

        if (deltaVelR >= 0 || d < 0)
            return Optional.empty();

        final double collisionTime = -((deltaVelR + Math.sqrt(d)) / (deltaVelSquared));

        return new Collision(collisionTime, this, other);
    }


    @Override
    public void collide(Collisionable other) {

    }

    @Override
    public boolean isPocket() {
        return false;
    }



}
