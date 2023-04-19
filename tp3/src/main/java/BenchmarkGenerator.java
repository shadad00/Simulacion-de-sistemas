import java.io.IOException;

public class BenchmarkGenerator {
    private final static float[] WHITE_BALL_Y_RANGE =
            new float[]{42.f, 43.75f, 45.5f, 47.25f, 49.f,50f, 50.75f, 52.5f, 54.25f, 56.f};

    private final static int SIMULATIONS_PER_Y_POSITION = 200;
    private static final float TABLE_HEIGHT = 112.f;
    private static final float TABLE_WIDTH = 224.f;

    public static void main(String[] args) {
        runAll();
    }

    public static void runAll()  {
        for (float whiteBallY : WHITE_BALL_Y_RANGE) {
            for (int i = 0; i < SIMULATIONS_PER_Y_POSITION; i++) {
                run(whiteBallY, i);
            }
        }
    }

    public static void run(float whiteBallY, int iter) {
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
