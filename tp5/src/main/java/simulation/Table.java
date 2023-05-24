package simulation;


import utils.Pair;

import java.util.*;
import java.util.stream.Collectors;


public class Table implements Iterable<Table> {
    protected static final double BALL_MASS = 1; //g
    protected static final double LOWER_RADIUS = 0.85; // cm
    protected static final double UPPER_RADIUS = 1.15; //cm
    protected static int N = 1;
    protected double deltaTime = Math.pow(10, -3);
    protected static double WIDTH = 20; //cm
    protected static double HEIGHT = 70; //cm

    protected static double AMPLITUDE = 0.15; //cm

    protected int iteration = 0;
    protected Set<CommonBall> balls;
    protected double simulationTime;

    protected double leftGap;
    protected double rightGap;
    protected int frequency;

    protected double offset;


    protected double finalTime = 10;

    protected NoPeriodicGrid cim ;

    private Set<Integer> outBallsId = new HashSet<>();


    public Table(Double simulationTime,
                 Set<CommonBall> commonBalls,
                 int iteration,
                 double finalTime,
                 double deltaTime
    ) {
        this.simulationTime = simulationTime;
        this.balls = commonBalls;
        this.iteration = iteration;
        this.finalTime = finalTime;
        this.deltaTime = deltaTime;
    }


    public Table(final Set<CommonBall> balls, final double time, int iteration, int frequency){
        this.balls = balls;
        this.simulationTime = time;
        this.iteration = iteration;
        this.frequency = frequency;
        moveWalls();
    }

    public Table(Table other){
         this.frequency = other.frequency;
         this.leftGap = other.leftGap;
         this.rightGap = other.rightGap;
         this.iteration = other.iteration;
         this.finalTime = other.finalTime;
         this.deltaTime = other.deltaTime;
         this.balls = new TreeSet<>();
         for (CommonBall ball : other.balls)
             this.balls.add(new CommonBall(ball));
         this.simulationTime = other.simulationTime;
    }


    public Table(final double gapWidth, final int frequency) {
        this.simulationTime = 0.0;
        this.balls = new TreeSet<>();
        this.leftGap = (WIDTH / 2) - (gapWidth / 2);
        this.rightGap = (WIDTH / 2) + (gapWidth / 2);
        this.frequency = frequency;
        positionBalls();
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
        moveWalls();


        // Primero predecimos todos los r
        for (final CommonBall ball : balls)
            ball.updateWithPrediction();

        this.cim = new NoPeriodicGrid( 2 * HEIGHT, 12 );
        this.cim.placeBalls(this.balls);
        this.cim.computeDistanceBetweenBalls();

        Map<CommonBall, Set<CommonBall> > adjacencyMap = new HashMap<>();
        for (final CommonBall ball : balls) {
            Set<BallAndDistance> otherDistance = this.cim.getNeighbors(ball);
            Set<CommonBall> other;
            if (otherDistance != null){
                other = otherDistance.stream().map(
                                BallAndDistance::getOtherBall).
                        collect(Collectors.toSet());
            }else {
                other = new HashSet<>();
            }
            adjacencyMap.put(ball, other);
            ball.sumForces( other, WIDTH, HEIGHT, leftGap, rightGap, offset);
        }

        for (final CommonBall ball : balls) {
            ball.correctPrediction();
        }

        for (final CommonBall ball : balls)
            ball.updateAcceleration( adjacencyMap.get(ball), WIDTH, HEIGHT, leftGap, rightGap, offset);

        this.simulationTime += this.deltaTime;
        reinsertBalls();
        return this;
    }

    public boolean hasFinished(){
        return Double.compare(this.simulationTime, this.finalTime) > 0;
    }



    private void reinsertBalls(){
        this.outBallsId = new HashSet<>();
        Random random = new Random();
        for (CommonBall ball : this.balls) {
            if( (ball.getPosition().getY() <= -(HEIGHT / 10)) &&
                    ((ball.getPosition().getX() - ball.getRadius() >= leftGap) &&
                    (ball.getPosition().getX() + ball.getRadius() <= rightGap)) ){
                outBallsId.add(ball.getBallNumber());
                double yPosition = 40 + (70 - 40) * random.nextDouble();
                ball.setVelocity(Pair.ZERO);
                ball.setAcceleration(Pair.ZERO);
                ball.setPosition(Pair.of(ball.getPosition().getX(), yPosition));
            }
        }

    }

    public Set<Integer> getOutBallsId() {
        return outBallsId;
    }

    private void positionBalls() {
        Random random = new Random();
        for (int i = 0; i < N; i++) {
            Pair position = Pair.of(random.nextDouble() * WIDTH, random.nextDouble() * HEIGHT);
            double ballRadius = LOWER_RADIUS + (UPPER_RADIUS - LOWER_RADIUS) * random.nextDouble();
            CommonBall addingBall = new CommonBall(i, position, BALL_MASS,  ballRadius);
            while(checkNoBallOverlap(addingBall))
                addingBall.setPosition(Pair.of(random.nextDouble() * WIDTH, random.nextDouble() * HEIGHT));
            addingBall.setDt(deltaTime);
            this.balls.add(addingBall);
        }

    }

    private boolean checkNoBallOverlap(Ball pivotBall) {
        for (CommonBall otherBall : balls)
            if (pivotBall.distanceTo(otherBall) < 0)
                return true;
        return false;
    }

    private void moveWalls(){
        offset = AMPLITUDE * Math.sin(frequency * simulationTime);
    }


    public int getIteration() {
        return iteration;
    }


    public Set<CommonBall> getBalls() {
        return balls;
    }

    public double getSimulationTime() {
        return simulationTime;
    }



    public void setDeltaTime(double deltaTime) {
        this.deltaTime = deltaTime;
        this.balls.forEach(ball -> ball.setDt(deltaTime));

    }

    public double getDeltaTime() {
        return deltaTime;
    }

    public double getOffset() {
        return offset;
    }
}
