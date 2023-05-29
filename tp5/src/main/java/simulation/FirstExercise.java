package simulation;

import java.io.IOException;

import static simulation.UnitConstants.GAP;

public class FirstExercise {

    private static final int SIMULATION_PER_FREQUENCY = 1;

    public static void main(String[] args) {
        runAll();
    }

    public static void runAll()  {

        for (int freq : UnitConstants.FREQUENCIES) {
            for (int i = 0; i < SIMULATION_PER_FREQUENCY; i++) {
                    Table currentTable = new Table(GAP, freq);
                    run(currentTable , 100, freq, i);
            }
        }
    }

    public static void run(Table currentTable, int persistingMultiplier,int freq,  int j) {
        String outputFilename = String.format("silo_fq%d_gap%.2f_i%d",freq,GAP,  j );
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
