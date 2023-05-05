package table;

import simulation.Ball;
import simulation.Pair;
import solvers.BeemanSolver;

public class CommonBall extends Ball {
    private final int ballNumber;

    public CommonBall(final int ballNumber, Pair<Double> position, Pair<Double> velocity, final Double mass, final Double radius, int collision) {
        this(ballNumber, position, velocity, mass,  radius);
    }

    public CommonBall(CommonBall other){
        super();
        this.position = new Pair<>(other.position.getX(), other.getPosition().getY());
        this.velocity = new Pair<>(other.velocity.getX(), other.velocity.getY());
        this.mass = other.mass;
        this.radius = other.radius;
        this.ballNumber = other.ballNumber;
    }
    
    public CommonBall(final int ballNumber, Pair<Double> position, Pair<Double> velocity, final Double mass, final Double radius) {
        super(position, velocity, mass, radius);
        this.ballNumber = ballNumber;
    }

    @Override
    public void updatePosition(Double dt) {
        //todo: implement
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
        return new CommonBall(0, position, velocity, mass, radius);
    }

    public static CommonBall buildColoredBall(final int ballNumber, Pair<Double> position, final Double mass, final Double radius) {
        return new CommonBall(ballNumber, position, new Pair<>(0., 0.), mass, radius);
    }

}
