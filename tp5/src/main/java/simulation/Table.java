package simulation;


import utils.Pair;

import java.util.*;
import java.util.stream.Collectors;

import static simulation.UnitConstants.*;


public class Table implements Iterable<Table> {
    protected static int N = 200;
    protected double finalTime = 20;
    protected double deltaTime = Math.pow(10, -4);

    protected int iteration = 0;
    protected Set<CommonBall> balls;
    protected double simulationTime;

    protected double leftGap;
    protected double rightGap;
    protected int frequency;

    protected double offset;

    protected NoPeriodicGrid cim ;

    private Set<Integer> outBallsId = new HashSet<>();


    public Table(Double simulationTime,
                 Set<CommonBall> commonBalls,
                 int frequency
    ) {
        this.simulationTime = simulationTime;
        this.balls = commonBalls;
        this.frequency = frequency;
        moveWalls();
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
        this.leftGap = (SILO_WIDTH / 2) - (gapWidth / 2);
        this.rightGap = (SILO_WIDTH / 2) + (gapWidth / 2);
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

        this.cim = new NoPeriodicGrid( 2 * SILO_HEIGHT, 40 );
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
            }else
                other = new HashSet<>();
            adjacencyMap.put(ball,other);
            ball.sumForces(new HashSet<>(other), SILO_WIDTH, SILO_HEIGHT, leftGap, rightGap, offset);
        }

        for (final CommonBall ball : balls) {
            ball.correctPrediction();
        }

        for (final CommonBall ball : balls)
            ball.updateAcceleration( new HashSet<>(adjacencyMap.get(ball)), SILO_WIDTH, SILO_HEIGHT, leftGap, rightGap, offset);

        reinsertBalls();
        this.simulationTime += this.deltaTime;
        return this;
    }

    public boolean hasFinished(){
        return Double.compare(this.simulationTime, this.finalTime) > 0;
    }



    private void reinsertBalls(){
        this.outBallsId = new HashSet<>();
        Random random = new Random();
        for (CommonBall ball : this.balls) {
            if( ball.getPosition().getY() <= 0) {
                do {
                    double xPos = BALL_UPPER_RADIUS + (SILO_WIDTH - 2 * BALL_UPPER_RADIUS) * random.nextDouble();
                    double yPos = REINSERT_LOWER_BOUND + (REINSERT_UPPER_BOUND - REINSERT_LOWER_BOUND) * random.nextDouble();
                    
                    ball.setPosition(Pair.of(xPos, yPos));
                } while (!checkNoBallOverlap(ball));

                ball.setVelocity(Pair.ZERO);
                ball.setAcceleration(Pair.ZERO);

                outBallsId.add(ball.getBallNumber());
            }
        }
    }

    public Set<Integer> getOutBallsId() {
        return outBallsId;
    }

    private void positionBalls() {
        Random random = new Random();
        for (int i = 0; i < N; i++) {
            double ballRadius = BALL_LOWER_RADIUS + (BALL_UPPER_RADIUS - BALL_LOWER_RADIUS) * random.nextDouble();

            CommonBall addingBall;
            do {
                final double xPos = ballRadius + random.nextDouble() * (SILO_WIDTH - 2 * ballRadius);
                final double yPos = ballRadius + random.nextDouble() * (SILO_HEIGHT - 2 * ballRadius);
                final Pair position = Pair.of(xPos, yPos);

                addingBall = new CommonBall(i, position, BALL_MASS, ballRadius);
            } while (checkNoBallOverlap(addingBall));

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

    private void moveWalls() {
        offset = SILO_VIBRATION_AMPLITUDE * Math.sin(frequency * simulationTime);
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
