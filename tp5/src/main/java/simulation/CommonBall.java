package simulation;

import lombok.Getter;
import lombok.Setter;
import utils.Pair;

import java.util.Objects;
import java.util.Set;

@Getter
@Setter
public class CommonBall extends Ball implements Comparable<CommonBall> {
    private final int ballNumber;
    protected double dt;
    protected double[] dt_k;

    private double k_n ;
    private double k_t;

    private Pair lastAcceleration = Pair.ZERO;

    private Pair predictedVelocity = Pair.ZERO;

    private Pair predictedAcceleration = Pair.ZERO;

    private static double G = 9.81;

    public CommonBall(CommonBall other){
        super();
        this.position = new Pair(other.position.getX(), other.getPosition().getY());
        this.velocity = new Pair(other.velocity.getX(), other.velocity.getY());
        this.acceleration = new Pair(other.acceleration.getX(), other.acceleration.getY());
        this.force = new Pair(other.force.getX(), other.force.getY());
        this.mass = other.mass;
        this.radius = other.radius;
        this.ballNumber = other.ballNumber;
        this.k_t = other.k_t;
        this.k_n = other.k_n;

    }
    
    public CommonBall(final int ballNumber, Pair position,
                      Pair velocity, Pair acceleration , Pair force,
                      final Double mass, final Double radius,
                      double k_n , double k_t
    ) {
        super(position, velocity, acceleration, force, mass, radius);
        this.ballNumber = ballNumber;
        this.k_t = k_t;
        this.k_n = k_n;
    }

    public void setDt(double dt){
        this.dt = dt;
        dt_k = new double[3];
        for (int k = 0; k < dt_k.length; k++) {
            dt_k[k] = Math.pow(dt, k);
        }
    }

    public void updateWithPrediction(){
        predictedVelocity = this.velocity;

        double rx,ry;
        rx = position.getX() + velocity.getX() * dt + ((2./3. * acceleration.getX()) - (1/6. * lastAcceleration.getX())) * dt_k[2];
        ry = position.getY() + velocity.getY() * dt + ((2./3. * acceleration.getY()) - (1/6. * lastAcceleration.getY())) * dt_k[2];

        double vxp, vyp;
        vxp = velocity.getX() + ((3/2. * acceleration.getX())  - (1/2. * lastAcceleration.getX())) * dt;
        vyp = velocity.getY() + ((3/2. * acceleration.getY())  - (1/2. * lastAcceleration.getY())) * dt;

        setPosition(Pair.of(rx, ry));
        this.predictedVelocity = Pair.of(vxp, vyp);
    }

    public void correctPrediction(){
        double vxc, vyc;
        vxc = velocity.getX() + ((1/3. * predictedAcceleration.getX()) + (5/6. * acceleration.getX()) - (1/6. * lastAcceleration.getX())) * dt;
        vyc = velocity.getY() + ((1/3. * predictedAcceleration.getY()) + (5/6. * acceleration.getY()) - (1/6. * lastAcceleration.getY())) * dt;
        setVelocity(Pair.of(vxc, vyc));
        lastAcceleration = acceleration;
        setAcceleration(predictedAcceleration);
    }


    public void sumForces(final Set<CommonBall> otherBalls, final double tableWidth, final double tableHeight) {
       this.force = new Pair(0,- mass * G);

        for (CommonBall otherBall : otherBalls) {
            Pair forceBetweenBalls = forceBetween(otherBall);
            if (!forceBetweenBalls.equals(Pair.ZERO)){
                this.force.add(forceBetweenBalls);
            }
        }

        this.force.add(forceBetweenLeftWall());
        this.force.add(forceBetweenBottomWall());
        this.force.add(forceBetweenRightWall(tableWidth));
        this.force.add(forceBetweenTopWall(tableHeight));

        predictedAcceleration = Pair.of(this.force.getX() / mass, this.force.getY() / mass);
    }


    public Pair forceBetween(CommonBall otherBall) {
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
        Pair relativeVelocity = Pair.of(velocity.getX() - otherBall.getVelocity().getX(),
                velocity.getY()- otherBall.getVelocity().getY());
        return computeForce(dseta, ex, ey, relativeVelocity);
    }

    public Pair forceBetweenRightWall(double wallX) {
        if (position.getX() + getRadius() <= wallX)
            return Pair.ZERO;
        double dseta = Math.abs(position.getX() + getRadius() - wallX);
        return computeForce(dseta, -1,0, getVelocity());
    }

    public Pair forceBetweenLeftWall() {
        if (position.getX() - getRadius() >= 0)
            return Pair.ZERO;
        double dseta = Math.abs(position.getX() - getRadius());
        return computeForce(dseta, 1,0, getVelocity());
    }

    public Pair forceBetweenTopWall(double wallY) {
        if (position.getY() + getRadius() <= wallY)
            return Pair.ZERO;
        double dseta = Math.abs(position.getY() + getRadius() - wallY);
        return computeForce(dseta, 0,-1, getVelocity());

    }

    public Pair forceBetweenBottomWall() {
        if (position.getY() - getRadius() >= 0)
            return Pair.ZERO;
        double dseta = Math.abs(position.getY() - getRadius());
        return computeForce(dseta, 0,1, getVelocity());

    }

    private Pair computeForce(double dseta, double ex, double ey, Pair relativeVelocity){
        Pair tang = Pair.of(-ey, ex);

        double Fn = - k_n * dseta ;
        double Ft = - k_t * dseta * (relativeVelocity.dot(tang));

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

}
