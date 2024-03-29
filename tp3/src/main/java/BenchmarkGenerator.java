import java.io.IOException;

public class BenchmarkGenerator {
    private final static double[] WHITE_BALL_Y_RANGE =
            new double[]{42., 43.75, 45.5, 47.25, 49.,50, 50.75, 52.5, 54.25, 56.};

    private final static int SIMULATIONS_PER_Y_POSITION = 200;
    private static final double TABLE_HEIGHT = 112.;
    private static final double TABLE_WIDTH = 224.;

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
                    TABLE_HEIGHT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
