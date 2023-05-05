package table;

import java.io.IOException;

public class BenchmarkGenerator {
    private final static double[] WHITE_BALL_Y_RANGE =
            new double[]{42.};

    private final static int SIMULATIONS_PER_Y_POSITION = 1;
    private static final double TABLE_HEIGHT = 112.;
    private static final double TABLE_WIDTH = 224.;
    
    private static final double FINAL_TIME = 100.;
    private static final double DELTA_TIME = .001;
    private static boolean POCKETS = true;

    public static void main(String[] args) {
        runAll();
    }

    public static void runAll()  {
        for (double whiteBallY : WHITE_BALL_Y_RANGE) {
            for (int i = 0; i < SIMULATIONS_PER_Y_POSITION; i++) {
                run(whiteBallY, i);
            }
        }
    }

    public static void run(double whiteBallY, int iter) {
        String outputFilename = String.format("pool_y%.2f_i%d", whiteBallY, iter);
        try {
            System.out.println("Generating " + outputFilename + "...");
            new CsvGenerator(outputFilename,
                    whiteBallY,
                    TABLE_WIDTH,
                    TABLE_HEIGHT,
                    FINAL_TIME,
                    DELTA_TIME,
                    POCKETS
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
