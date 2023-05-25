package simulation;

import lombok.Getter;
import lombok.Setter;
import utils.Pair;

import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

import static simulation.UnitConstants.*;

@Getter
@Setter
public class CommonBall extends Ball implements Comparable<CommonBall> {

    private Supplier<Pair> predicted = ()->getPredictedVelocity();
    private Supplier<Pair> corrected = ()->getVelocity();
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
        lastAcceleration = Pair.of(0, -mass * G);
    }
    
    public CommonBall(final int ballNumber, Pair position,
                      final Double mass, final Double radius) {
        super(position, Pair.ZERO, Pair.ZERO, Pair.ZERO, mass, radius);
        this.ballNumber = ballNumber;
        lastAcceleration = Pair.of(0, -mass * G);
    }

    public CommonBall(int ballId, Pair position, Pair velocity, Pair acceleration, Pair force, double ballMass, double ballRadius) {
        this.ballNumber = ballId;
        setPosition(position);
        setVelocity(velocity);
        setAcceleration(acceleration);
        setForce(force);
        this.mass = ballMass;
        this.radius = ballRadius;
        lastAcceleration = Pair.of(0, -mass * G);
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
        setVelocity(Pair.of(vxc, vyc));;
    }

    public void updateAcceleration(final Set<CommonBall> otherBalls, final double tableWidth, final double tableHeight,
                                   final double leftGap, final double rightGap, final double offset){
        lastAcceleration = acceleration;
        sumForces(otherBalls, tableWidth, tableHeight, leftGap, rightGap, offset, corrected);
        acceleration = Pair.of(this.force.getX() / mass, this.force.getY() / mass);
    }


    public void sumForces(final Set<CommonBall> otherBalls, final double tableWidth, final double tableHeight,
                          final double leftGap, final double rightGap, final double offset){
        sumForces(otherBalls, tableWidth, tableHeight, leftGap, rightGap, offset, predicted);
        predictedAcceleration = Pair.of(this.force.getX() / mass, this.force.getY() / mass);
    }

    private void sumForces(final Set<CommonBall> otherBalls, final double tableWidth, final double tableHeight,
                          final double leftGap, final double rightGap, final double offset, Supplier<Pair> velocitySupplier) {
       this.force = new Pair(0, -mass * G);

        for (CommonBall otherBall : otherBalls) {
            if (this.equals(otherBall))
                continue;

            Pair forceBetweenBalls = forceBetween(otherBall, velocitySupplier);

            if (!forceBetweenBalls.equals(Pair.ZERO)){
                this.force.add(forceBetweenBalls);
            }
        }

        this.force.add(forceBetweenLeftWall(velocitySupplier));
        this.force.add(forceBetweenBottomWall(offset, leftGap, rightGap,velocitySupplier));
        this.force.add(forceBetweenRightWall(tableWidth,velocitySupplier));
        this.force.add(forceBetweenTopWall(tableHeight + offset,velocitySupplier));
    }


    public Pair forceBetween(CommonBall otherBall, Supplier<Pair> velocity) {
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
        Pair relativeVelocity = velocity.get().substract(otherBall.getPredictedVelocity());
        return computeForce(dseta, ex, ey, relativeVelocity);
    }

    public Pair forceBetweenRightWall(double wallX, Supplier<Pair> velocity) {
        if (position.getX() + getRadius() <= wallX)
            return Pair.ZERO;

        double dseta = Math.abs(wallX - (position.getX() + getRadius()));
        return computeForce(dseta, 1,0, velocity.get());
    }

    public Pair forceBetweenLeftWall(Supplier<Pair> velocity) {
        if (position.getX() - getRadius() >= 0)
            return Pair.ZERO;

        double dseta = Math.abs(position.getX() - getRadius());
        return computeForce(dseta, -1,0, velocity.get());
    }

    public Pair forceBetweenTopWall(double wallY, Supplier<Pair> velocity) {
        if (position.getY() + getRadius() <= wallY)
            return Pair.ZERO;

        double dseta = Math.abs(position.getY() + getRadius() - wallY);
        return computeForce(dseta, 0,1, velocity.get());
    }

    public Pair forceBetweenBottomWall(double offset, double leftGap, double rightGap, Supplier<Pair> velocity) {
        // if the particle is in the gap, there is no wall.
        if (position.getY() - getRadius() >= offset ||
                (position.getX() - getRadius() >= leftGap && position.getX() + getRadius() <= rightGap) )
            return Pair.ZERO;

        double dseta = Math.abs(position.getY() - getRadius() - offset);
        return computeForce(dseta, 0, -1, velocity.get());
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
