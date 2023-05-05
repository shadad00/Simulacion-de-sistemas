package table;


import utils.Pair;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Math.pow;

public class Table implements Iterable<Table> {
    protected static final double BALL_DIAMETER = 5.7;
    protected static final double POCKET_DIAMETER = BALL_DIAMETER * 2;
    protected static final double BALL_MASS = 165.0;
    protected static final double LOWER_EPSILON = 0.02;
    protected static final double UPPER_EPSILON = 0.03;
    public static final double WHITE_BALL_INITIAL_X = 56.;
    public static final double WHITE_BALL_INITIAL_X_VEL = 200.;
    public static final double WHITE_BALL_INITIAL_Y_VEL = 0.;
    public static final double TRIANGLE_X_START = 168.56;
    public static final double TRIANGLE_Y_START = 56.;
    private static final int BALLS_GOAL = 8;

    protected int iteration = 0;
    protected final Set<CommonBall> balls;
    protected double simulationTime;
    protected final double width;
    protected final double height;
    protected double initWhiteBallY;
    protected double finalTime=0;
    protected double deltaTime = 0;

    protected Set<PocketBall> pocketBalls = null;

    public Table(final Set<CommonBall> balls, final double width, final double height,
                 final double time, int iteration){
        this.height = height;
        this.width = width;
        this.balls = balls;
        this.simulationTime = time;
        this.iteration = iteration;
    }

    public Table(Table other){
         this.iteration = other.iteration;
         this.finalTime = other.finalTime;
         this.deltaTime = other.deltaTime;
         this.initWhiteBallY = other.initWhiteBallY;
         this.balls = new HashSet<>();
         for (CommonBall ball : other.balls)
             this.balls.add(new CommonBall(ball));
         this.simulationTime = other.simulationTime;
         this.width = other.width;
         this.height = other.height;
    }



    public Table(double whiteBallY,final double width, final double height, final double finalTime, final double deltaTime) {
        this.simulationTime = 0.0;
        this.width = width;
        this.height = height;
        this.finalTime = finalTime;
        this.deltaTime = deltaTime;
        this.balls = new HashSet<>();
        positionWhiteBall(whiteBallY);
        positionColorBalls();
    }

    @Override
    public Iterator<Table> iterator() {
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return !hasFinished();
            }

            @Override
            public Table next() {
                incrementIteration();
                return getNextTable();
            }
        };
    }

    private void incrementIteration(){
        this.iteration++;
    }

    public Table getNextTable() {
        balls.forEach(this::updateForce);
        balls.forEach((ball)-> ball.updatePosition(this.deltaTime));
        this.simulationTime += this.deltaTime;
        return new Table(this);
    }

    public boolean hasFinished(){
        return this.balls.size() <= BALLS_GOAL || Double.compare(this.simulationTime, this.finalTime) >= 0;
    }

    public Table(Double width,
                 Double height,
                 Double simulationTime,
                 Set<CommonBall> commonBalls,
                 int iteration,
                 double finalTime,
                 double deltaTime
                   ) {
        this.width = width;
        this.height = height;
        this.simulationTime = simulationTime;
        this.balls = commonBalls;
        this.iteration = iteration;
        this.finalTime = finalTime;
        this.deltaTime = deltaTime;
    }

    private void updateForce(CommonBall ball){
        Pair newForce = Pair.of(0, 0);

        for (CommonBall otherBall : balls) {
            if (ball.equals(otherBall))
                continue;

            Pair forceBetweenBalls = ball.forceBetween(otherBall);;
            newForce.add(forceBetweenBalls);
        }

        newForce.add(ball.forceBetweenLeftWall());
        newForce.add(ball.forceBetweenBottomWall());
        newForce.add(ball.forceBetweenRightWall(width));
        newForce.add(ball.forceBetweenTopWall(height));

        ball.setForce(newForce);
    }

    private static Pair addForces(Pair force, Pair otherForce) {
        double xForce = force.getX() + otherForce.getX();
        double yForce = force.getY() + otherForce.getY();

        force.setX(xForce);
        force.setY(yForce);

        return force;
    }

    private void positionColorBalls() {
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        int ballNumber = 1;
        double rb = BALL_DIAMETER / 2;
        double rbe = rb + UPPER_EPSILON / 2;
        double h  = Math.sqrt(3 * Math.pow(rb, 2) + 3 * rb * UPPER_EPSILON + 5. / 4 * Math.pow(UPPER_EPSILON, 2));

        for (int i = 0; i < 5; i++) {
            double xRow = TRIANGLE_X_START + h * i - (UPPER_EPSILON - rnd.nextDouble(LOWER_EPSILON, UPPER_EPSILON));
            double yStart = TRIANGLE_Y_START - ( rbe * i );
            for (int j = 0; j <= i; j++) {
                double y = yStart + (BALL_DIAMETER + UPPER_EPSILON / 2) * j + (UPPER_EPSILON - rnd.nextDouble(LOWER_EPSILON, UPPER_EPSILON));

                CommonBall colorBall = CommonBall.buildColoredBall(ballNumber++,
                        new Pair(xRow, y),
                        BALL_MASS,
                        BALL_DIAMETER / 2);

                balls.add(colorBall);
            }
        }

        checkNoBallOverlap();
    }

    private void checkNoBallOverlap() {
        for (CommonBall ball : balls)
            for (CommonBall otherBall : balls) {
                if (ball.equals(otherBall))
                    continue;

                if (ball.distanceTo(otherBall) < 0)
                    throw new RuntimeException(String.format("Ball overlap between %s and %s", ball.getBallNumber(), otherBall.getBallNumber()));

            }
    }

    private void positionWhiteBall(double whiteBallY) {
        Pair whiteBallPosition = new Pair(WHITE_BALL_INITIAL_X, whiteBallY);
        Pair whiteBallVelocity = new Pair(WHITE_BALL_INITIAL_X_VEL, WHITE_BALL_INITIAL_Y_VEL);
        CommonBall whiteBall = CommonBall.buildWhiteBall(whiteBallPosition, whiteBallVelocity, BALL_MASS, BALL_DIAMETER / 2);
        balls.add(whiteBall);
    }


    public int getIteration() {
        return iteration;
    }

    public void setIteration(int iteration) {
        this.iteration = iteration;
    }

    public Set<CommonBall> getBalls() {
        return balls;
    }

    public double getSimulationTime() {
        return simulationTime;
    }

    public void setSimulationTime(double simulationTime) {
        this.simulationTime = simulationTime;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getFinalTime() {
        return finalTime;
    }

    public void setFinalTime(double finalTime) {
        this.finalTime = finalTime;
    }

    public double getDeltaTime() {
        return deltaTime;
    }

    public void setDeltaTime(double deltaTime) {
        this.deltaTime = deltaTime;
    }

    public Set<PocketBall> getPocketBalls() {
        if (this.pocketBalls == null)
            return new HashSet<>();
        return this.pocketBalls;
    }



}
