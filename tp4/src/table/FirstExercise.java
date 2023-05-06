package table;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public class FirstExercise {
    private final static double[] WHITE_BALL_Y_RANGE =
            new double[]{56.};

    private final static int SIMULATIONS_PER_Y_POSITION = 1;
    private static final double TABLE_HEIGHT = 112.;
    private static final double TABLE_WIDTH = 224.;
    
    private static final double FINAL_TIME = 100.;

    private static boolean POCKETS = false;

    public static void main(String[] args) {
        runAll();
    }

    public static void runAll()  {

        for (double whiteBallY : WHITE_BALL_Y_RANGE) {
            Table table = new Table(whiteBallY, TABLE_WIDTH, TABLE_HEIGHT, FINAL_TIME,0.);
            for (int i = 0; i < SIMULATIONS_PER_Y_POSITION; i++) {
                for (int j = 2; j <= 6; j++) {
                    Table currentTable = new Table(table);
                    currentTable.setDeltaTime(Math.pow(10, -j));
                    run(currentTable , (int) Math.pow(10, j - 2));
                }
            }
        }
    }

    public static void run(Table currentTable, int persistingMultiplier) {
        String outputFilename = String.format("pool_dt%f", currentTable.getDeltaTime());
        try {
            System.out.println("Generating " + outputFilename + "...");
            new CsvGenerator(outputFilename,
                    currentTable,
                    POCKETS,
                    persistingMultiplier
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
