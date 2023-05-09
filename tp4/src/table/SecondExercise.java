package table;

import java.io.IOException;

public class SecondExercise {

    private final static int SIMULATIONS_PER_Y_POSITION = 1;
    private static final double TABLE_HEIGHT = 112.;
    private static final double TABLE_WIDTH = 224.;
    
    private static final double FINAL_TIME = 6.;

    private static final double DELTA_TIME = Math.pow(10, -5);
    private static final int MULTIPLIER = (int) Math.pow(10, 2);

    private static boolean POCKETS = true;

    public static void main(String[] args) {
        runAll();
    }

    public static void runAll()  {
        double start = 42.;
        double end = 56.;
        double dy = ( end - start) / 4 ;
        double current = start;
        while (current <= end){
            for (int i = 0; i < SIMULATIONS_PER_Y_POSITION; i++)
                run(current, DELTA_TIME, MULTIPLIER, i );
            current += dy;
        }
    }

    public static void run(double whiteBallY, double deltaTime, int persistingMultiplier, int iteration) {
        String outputFilename = String.format("pool_y%.2f_i%d", whiteBallY, iteration);
        try {
            System.out.println("Generating " + outputFilename + "...");
            new CsvGenerator("./tp4/out/pool/yposition/",
                    outputFilename,
                    whiteBallY,
                    TABLE_WIDTH,
                    TABLE_HEIGHT,
                    FINAL_TIME,
                    deltaTime,
                    POCKETS,
                    persistingMultiplier
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
