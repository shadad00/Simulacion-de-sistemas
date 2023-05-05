package table;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CsvGenerator {

    final static String HEADER = "iter,time,ball_id,pos_x,pos_y,vel_x,vel_y,ball_radius,ball_mass,collision_count";
    final static String FORMAT = "%d,%f,%d,%f,%f,%f,%f,%f,%f,%d\n";
    private final BufferedWriter bw;

    public CsvGenerator(String outputFile, double whiteBallY, double width, double height,
                        double finalTime, double deltaTime
                        ) throws IOException {
        this(outputFile, new Table(whiteBallY,width,height, finalTime, deltaTime));
    }

    public CsvGenerator(String outputFile, Table table) throws IOException{
        File csvFile = new File("./" + outputFile +".csv");
        if (!csvFile.exists() && !csvFile.createNewFile()) {
            throw new IOException("Unable to create output csv file.");
        }

        bw = new BufferedWriter(new FileWriter(csvFile));
        bw.write(HEADER + "\n");
        writeTable(table);
        for (Table currentTable : table)
            writeTable(currentTable);
        bw.close();

    }

    private void writeTable(Table table) throws IOException {
        for (CommonBall ball : table.getBalls()) {
            bw.write(String.format(FORMAT,
                    table.getIteration(),
                    table.getSimulationTime(),
                    ball.getBallNumber(),
                    ball.getPosition().getX(),
                    ball.getPosition().getY(),
                    ball.getVelocity().getX(),
                    ball.getVelocity().getY(),
                    ball.getRadius(),
                    ball.getMass(),
                    0
            ));
        }
    }

}
