package simulation;

import java.io.IOException;

public class ExtraExercise {

    private static final int FREQUENCY = 0;
    private static final double[] GAPS = {0.04};

    private static final int SIMULATION_PER_FREQUENCY = 1;

    public static void main(String[] args) {
        runAll();
    }

    public static void runAll()  {
        for (double gap : GAPS) {
            for (int i = 0; i < SIMULATION_PER_FREQUENCY; i++) {
                Table currentTable = new Table(gap, FREQUENCY);
                run(currentTable,100, gap, i);
            }
        }
    }

    public static void run(Table currentTable, int persistingMultiplier,double gap,  int j) {
        String outputFilename = String.format("silo_fq0_gap%.2f_i%d", gap, j);
        try {
            System.out.println("Generating " + outputFilename + "...");
            new CsvGenerator("tp5/out/",
                    outputFilename,
                    currentTable,
                    persistingMultiplier
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
