package table;

import utils.Pair;

import java.util.HashSet;
import java.util.Set;

public class ej2bTable extends Table{

    private final Set<PocketBall> pocketBalls;
    protected double initWhiteBallY;


    public ej2bTable(Set<CommonBall> balls, double width, double height,
                     double time, int iteration) {
        super(balls, width, height, time, iteration);
        this.pocketBalls = new HashSet<>();
        positionPockets();
    }

    public ej2bTable(ej2bTable other) {
        super(other);
        this.initWhiteBallY = other.initWhiteBallY;
        this.pocketBalls = new HashSet<>(other.pocketBalls);
    }

    public ej2bTable(double whiteBallY, double width, double height, final double finalTime, final double deltaTime) {
        super(width, height, finalTime, deltaTime);
        this.initWhiteBallY = whiteBallY;
        this.pocketBalls = new HashSet<>();
        positionWhiteBall(whiteBallY);
        positionPockets();
    }


    public ej2bTable(Double width, Double height, Double simulationTime,
                     Set<CommonBall> commonBalls, Set<PocketBall> pocketBalls,
                     int iteration, final double finalTime, final double deltaTime) {
        super(width, height, simulationTime, commonBalls, iteration, finalTime, deltaTime);
        this.pocketBalls = pocketBalls;
    }

    private void positionPockets() {
        for (int i = 0; i <= 1; i++) {
            double y = i * 112.;
            for (int j = 0; j < 3; j++) {
                double x = j * (224.0 / 2);
                PocketBall pocketBall = new PocketBall(new Pair<>(x, y), new Pair<>(0., 0.),
                        0., POCKET_DIAMETER / 2);
                pocketBalls.add(pocketBall);
            }
        }
    }

    private void positionWhiteBall(double whiteBallY) {
        Pair<Double> whiteBallPosition = new Pair<>(WHITE_BALL_INITIAL_X, whiteBallY);
        Pair<Double> whiteBallVelocity = new Pair<>(WHITE_BALL_INITIAL_X_VEL, WHITE_BALL_INITIAL_Y_VEL);
        CommonBall whiteBall = CommonBall.buildWhiteBall(whiteBallPosition, whiteBallVelocity, BALL_MASS, BALL_DIAMETER / 2);
        balls.add(whiteBall);
    }




}
