import simulation.CommonBall;
import simulation.Table;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class Main {
    private final static double[] WHITE_BALL_Y_RANGE = new double[]{42., 56.};
    private final static int WHITE_BALL_Y_POSITIONS = 5;
    private final static int SIMULATIONS_PER_Y_POSITION = 3;

    public static void main(String[] args) throws IOException {
        for (int i = 0; i < WHITE_BALL_Y_POSITIONS; i++) {
            final double whiteBallY = Math.random() * (WHITE_BALL_Y_RANGE[1] - WHITE_BALL_Y_RANGE[0]) + WHITE_BALL_Y_RANGE[0];

            for (int j = 0; j < SIMULATIONS_PER_Y_POSITION; j++) {
                final Table table = new Table(whiteBallY, 224, 112);
            }
        }
    }
}
