package table;

import lombok.Getter;
import lombok.Setter;
import utils.Pair;

import static java.lang.Math.pow;

@Getter
@Setter
public class CommonBall extends Ball {
    private final int ballNumber;

    public CommonBall(CommonBall other){
        super();
        this.position = new Pair<>(other.position.getX(), other.getPosition().getY());
        this.velocity = new Pair<>(other.velocity.getX(), other.velocity.getY());
        this.acceleration = new Pair<>(other.acceleration.getX(), other.acceleration.getY());
        this.force = new Pair<>(other.force.getX(), other.force.getY());
        this.mass = other.mass;
        this.radius = other.radius;
        this.ballNumber = other.ballNumber;
    }
    
    public CommonBall(final int ballNumber, Pair<Double> position,
                      Pair<Double> velocity,Pair<Double> acceleration ,Pair<Double> force,
                      final Double mass, final Double radius) {
        super(position, velocity, acceleration, force,mass, radius);
        this.ballNumber = ballNumber;
    }

    @Override
    public void updatePosition(Double dt) {
        double rx = position.getX();
        double rx1 = velocity.getX();
        double rx2 = 1.;
        double rx3 = 1.;
        double rx4 = 1.;
        double rx5 = 1.;
        double ry = position.getY();
        double ry1 = velocity.getY();
        double ry2 = 1.;
        double ry3 = 1.;
        double ry4 = 1.;
        double ry5 = 1.;

        double rxp, rxp1, rxp2;
        rxp  = rx + rx1 * dt + rx2 * (pow(dt, 2) / 2) + rx3 * (pow(dt, 3) / 6) + rx4 * (pow(dt, 4) / 24) + rx5 * (pow(dt, 5) / 120);
        rxp1 = rx1 + rx2 * dt + rx3 * (pow(dt, 2) / 2) + rx4 * (pow(dt, 3) / 6) + rx5 * (pow(dt, 4) / 24);
        rxp2 = rx2 + rx3 * dt + rx4 * (pow(dt, 2) / 2) + rx5 * (pow(dt, 3) / 6);

        double ryp, ryp1, ryp2;
        ryp  = ry + ry1 * dt + ry2 * (pow(dt, 2) / 2) + ry3 * (pow(dt, 3) / 6) + ry4 * (pow(dt, 4) / 24) + ry5 * (pow(dt, 5) / 120);
        ryp1 = ry1 + ry2 * dt + ry3 * (pow(dt, 2) / 2) + ry4 * (pow(dt, 3) / 6) + ry5 * (pow(dt, 4) / 24);
        ryp2 = ry2 + ry3 * dt + ry4 * (pow(dt, 2) / 2) + ry5 * (pow(dt, 3) / 6);

        double drx2 = evaluate(rxp2, rx) - rxp2;
        double R2x = drx2 * pow(dt, 2) / 2;

        double dry2 = evaluate(ryp2, ry) - ryp2;
        double R2y = dry2 * pow(dt, 2) / 2;

        double rxc, rxc1, ryc, ryc1;
        rxc = rxp;
        rxc1 = rxp1 +  R2x / dt;
        ryc = ryp;
        ryc1 = ryp1 +  R2y / dt;

        setPosition(new Pair<>(rxc,ryc));
        setVelocity(new Pair<>(rxc1,ryc1));
        setAcceleration(new Pair<>(force.getX()/mass, force.getY()/mass));
    }

    @Override
    public boolean isPocket() {
        return false;
    }

    public int getBallNumber() {
        return ballNumber;
    }

    @Override
    public String toString() {
        return "simulation.CommonBall{" +
                "ballNumber=" + ballNumber +
                ", position=" + position +
                ", velocity=" + velocity +
                '}';
    }


    public static CommonBall buildWhiteBall(Pair<Double> position, Pair<Double> velocity, final Double mass, final Double radius) {
        return new CommonBall(0, position, velocity,
                new Pair<>(0., 0.),new Pair<>(0., 0.),
                mass, radius);
    }

    public static CommonBall buildColoredBall(final int ballNumber, Pair<Double> position, final Double mass, final Double radius) {
        return new CommonBall(ballNumber, position, new Pair<>(0., 0.),
                new Pair<>(0., 0.),new Pair<>(0., 0.),
                mass, radius);
    }

}
