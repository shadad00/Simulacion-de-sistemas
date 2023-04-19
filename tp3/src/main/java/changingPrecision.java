import animation.Parser;
import simulation.Table;

import java.io.IOException;


public class changingPrecision {

    public static void main(String[] args) throws IOException {
        Parser parser = new Parser("/home/shadad/Desktop/tp2pod/simulacion-de-sistemas/tp3/src/main/java/initial_condition.csv");
        Table initialTable = null;
        for (Table table : parser) {
            initialTable = new Table(table.getBalls(),table.getWidth(),table.getHeight(),table.getSimulationTime(),0);
        }
        if (initialTable != null)
            new CsvGenerator("float",initialTable);

    }

}
