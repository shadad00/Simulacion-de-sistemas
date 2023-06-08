package simulation;

import lombok.Getter;
import lombok.Setter;
import utils.Pair;

import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

import static simulation.UnitConstants.*;

@Getter
@Setter
public class CommonBall extends Ball implements Comparable<CommonBall> {

    private final int ballNumber;
    protected double dt;
    protected double[] dt_k;

    private Pair lastAcceleration;

    private Pair predictedVelocity = Pair.ZERO;

    private Pair predictedAcceleration = Pair.ZERO;


    public CommonBall(CommonBall other){
        super();
        this.position = new Pair(other.position.getX(), other.getPosition().getY());
        this.velocity = new Pair(other.velocity.getX(), other.velocity.getY());
        this.acceleration = new Pair(other.acceleration.getX(), other.acceleration.getY());
        this.force = new Pair(other.force.getX(), other.force.getY());
        this.mass = other.mass;
        this.radius = other.radius;
        this.ballNumber = other.ballNumber;
        lastAcceleration = Pair.of(0,mass * G);
    }
    
    public CommonBall(final int ballNumber, Pair position,
                      final Double mass, final Double radius) {
        super(position, Pair.ZERO, Pair.ZERO, Pair.ZERO, mass, radius);
        this.ballNumber = ballNumber;
        lastAcceleration = Pair.of(0,G);
    }

    public CommonBall(int ballId, Pair position, Pair velocity,
                      Pair acceleration, Pair force, double ballMass, double ballRadius) {
        this.ballNumber = ballId;
        setPosition(position);
        setVelocity(velocity);
        setAcceleration(acceleration);
        setForce(force);
        this.mass = ballMass;
        this.radius = ballRadius;
        lastAcceleration = Pair.of(0,G);
    }

    public void setDt(double dt){
        this.dt = dt;
        dt_k = new double[3];
        for (int k = 0; k < dt_k.length; k++) {
            dt_k[k] = Math.pow(dt, k);
        }
    }

    public void updateWithPrediction(){

        double rx,ry;
        rx = position.getX() + velocity.getX() * dt + ((2./3. * acceleration.getX()) - (1/6. * lastAcceleration.getX())) * dt_k[2];
        ry = position.getY() + velocity.getY() * dt + ((2./3. * acceleration.getY()) - (1/6. * lastAcceleration.getY())) * dt_k[2];

        double vxp, vyp;
        vxp = velocity.getX() + ((3/2. * acceleration.getX()) - (1/2. * lastAcceleration.getX())) * dt;
        vyp = velocity.getY() + ((3/2. * acceleration.getY()) - (1/2. * lastAcceleration.getY())) * dt;

        setPosition(Pair.of(rx, ry));
        this.predictedVelocity = Pair.of(vxp, vyp);
    }

    public void correctPrediction(){
        double vxc, vyc;
        vxc = velocity.getX() + ((1/3. * predictedAcceleration.getX()) + (5/6. * acceleration.getX()) - (1/6. * lastAcceleration.getX())) * dt;
        vyc = velocity.getY() + ((1/3. * predictedAcceleration.getY()) + (5/6. * acceleration.getY()) - (1/6. * lastAcceleration.getY())) * dt;
        setVelocity(Pair.of(vxc, vyc));
        acceleration = lastAcceleration;
        lastAcceleration = predictedAcceleration;
    }


    public void sumForces(final Set<CommonBall> otherBalls, final double tableWidth, final double tableHeight,
                          final double leftGap, final double rightGap, final double offset){
        // we're predicting the acceleration, so use predicted velocity.
        sumForces(otherBalls, tableWidth, tableHeight, leftGap, rightGap, offset, CommonBall::getPredictedVelocity);
        predictedAcceleration = Pair.of(this.force.getX() / mass, this.force.getY() / mass);
    }

    private void sumForces(final Set<CommonBall> otherBalls, final double tableWidth, final double tableHeight,
                          final double leftGap, final double rightGap, final double offset, Function<CommonBall, Pair> velocityFunction) {
        this.force = Pair.of(0,mass * G);

        double maxRadius = this.getRadius();

        otherBalls.add( //right wall
                new CommonBall(-1,Pair.of(tableWidth + maxRadius , position.getY()), mass, maxRadius));
        otherBalls.add( //left wall
                new CommonBall(-2, Pair.of(0 - maxRadius, position.getY()), mass, maxRadius));
        otherBalls.add( //top wall
                new CommonBall(-3, Pair.of(position.getX(), tableHeight + offset + maxRadius), mass, maxRadius));

        final boolean isOverGap = (position.getX() >= leftGap) && (position.getX() <= rightGap);
        if (!isOverGap) {
            otherBalls.add( // bottom wall but not in the gap
                    new CommonBall(-4,
                            Pair.of(position.getX(), offset - maxRadius), mass, maxRadius)
            );
        } else { //the ball is in the gap. I place a ball in the corner.
            new CommonBall(-4, Pair.of(leftGap, offset), mass, 0.0);
            new CommonBall(-5, Pair.of(rightGap, offset), mass, 0.0);
        }



        for (CommonBall otherBall : otherBalls) {
            if (this.equals(otherBall))
                continue;

            Pair forceBetweenBalls = forceBetween(otherBall, velocityFunction);

            if (!forceBetweenBalls.equals(Pair.ZERO)){
                this.force.add(forceBetweenBalls);
            }
        }
    }


    public Pair forceBetween(CommonBall otherBall, Function<CommonBall, Pair> velocityFunction) {
        double xDiff = otherBall.getPosition().getX() - getPosition().getX();
        double yDiff = otherBall.getPosition().getY() - getPosition().getY();
        double dist = Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));
        double sumRadius = getRadius() + otherBall.getRadius();

        // Si no estan en colision, la fuerza entre ellos es nula -> <0, 0>
        if (dist > sumRadius)
            return Pair.ZERO;

        double dseta = sumRadius - dist;
        double ex, ey;
        ex = xDiff / dist;
        ey = yDiff / dist;
        Pair relativeVelocity = velocityFunction.apply(this).substract(velocityFunction.apply(otherBall));
        return computeForce(dseta, ex, ey, relativeVelocity);
    }

    private Pair computeForce(double dseta, double ex, double ey, Pair relativeVelocity){
        Pair tang = Pair.of(-ey, ex);

        double Fn = -K_n * dseta ;
        double Ft = -K_t * dseta * (relativeVelocity.dot(tang));

        double Fx = Fn * (ex) + Ft * (-ey);
        double Fy = Fn * (ey) + Ft * (ex);

        return Pair.of(Fx, Fy);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommonBall that = (CommonBall) o;
        return ballNumber == that.ballNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ballNumber);
    }

    @Override
    public int compareTo(CommonBall o) {
        return Integer.compare(this.ballNumber, o.ballNumber);
    }

    public Double getRadius() {
        return radius;
    }

    @Override
    public String toString() {
        return "simulation.CommonBall{" +
                "ballNumber=" + ballNumber +
                ", position=" + position +
                ", velocity=" + velocity +
                '}';
    }

    @Override
    public boolean isPocket() {
        return false;
    }

    public int getBallNumber() {
        return ballNumber;
    }

    public Pair getPredictedVelocity() {
        return predictedVelocity;
    }
}
