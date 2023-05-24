package simulation;

import java.io.IOException;

public class SecondExercise {

    private static final int OPTIMAL_FREQUENCY = 5;
    private static final double[] GAPS = {4,5,6};

    private static final int SIMULATION_PER_FREQUENCY = 1;

    public static void main(String[] args) {
        runAll();
    }

    public static void runAll()  {

        for (double gap : GAPS) {
            for (int i = 0; i < SIMULATION_PER_FREQUENCY; i++) {
                    Table currentTable = new Table(gap, OPTIMAL_FREQUENCY);
                    run(currentTable , 10,gap, i );
            }
        }
    }

    public static void run(Table currentTable, int persistingMultiplier,double gap,  int j) {
        String outputFilename = String.format("silo_gap%.2f_i%d",gap,  j );
        try {
            System.out.println("Generating " + outputFilename + "...");
            new CsvGenerator("./tp5/out/fixed_frequency/",
                    outputFilename,
                    currentTable,
                    persistingMultiplier
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
