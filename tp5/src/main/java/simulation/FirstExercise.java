package simulation;

import java.io.IOException;

public class FirstExercise {

    private static final int[] FREQUENCIES = {5, 10, 15, 20, 30, 50};
    private static final double GAP = 3.;

    private static final int SIMULATION_PER_FREQUENCY = 1;

    public static void main(String[] args) {
        runAll();
    }

    public static void runAll()  {

        for (int freq : FREQUENCIES) {
            for (int i = 0; i < SIMULATION_PER_FREQUENCY; i++) {
                    Table currentTable = new Table(GAP, freq);
                    run(currentTable , 10,freq, i );
            }
        }
    }

    public static void run(Table currentTable, int persistingMultiplier,int freq,  int j) {
        String outputFilename = String.format("silo_fq%d_i%d",freq,  j );
        try {
            System.out.println("Generating " + outputFilename + "...");
            new CsvGenerator("./tp5/out/",
                    outputFilename,
                    currentTable,
                    persistingMultiplier
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
