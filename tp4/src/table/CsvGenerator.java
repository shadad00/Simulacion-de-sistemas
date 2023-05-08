package table;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CsvGenerator {

    final static String HEADER = "iter,time,ball_id,pos_x,pos_y,vel_x,vel_y,ball_radius,ball_mass,force_x,force_y";
    final static String FORMAT = "%d,%f,%d,%f,%f,%f,%f,%f,%f,%f,%f\n";
    private final BufferedWriter bw;

    public CsvGenerator(String outputFile, double whiteBallY, double width, double height,
                        double finalTime, double deltaTime, boolean pockets,
                        int persistingMultiplier
                        ) throws IOException {
        this(outputFile, new Table(whiteBallY,width,height, finalTime, deltaTime),
                pockets, persistingMultiplier);
    }

    public CsvGenerator(String outputFile, Table table,
                        boolean pockets, int persistingMultiplier) throws IOException{
        File csvFile = new File("./tp4/out/pool/yposition/" + outputFile +".csv");
        if (!csvFile.exists() && !csvFile.createNewFile()) {
            throw new IOException("Unable to create output csv file.");
        }
        if(pockets)
            table.positionPockets();
        bw = new BufferedWriter(new FileWriter(csvFile));
        bw.write(HEADER + "\n");
        writeTable(table, persistingMultiplier);
        int i = 0;
        for (Table currentTable : table){
            i++;
            if ( i == persistingMultiplier){
                writeTable(currentTable, persistingMultiplier);
                i = 0;
            }
        }
        bw.close();

    }

    private void writeTable(Table table, int persistingMultiplier) throws IOException {
        for (CommonBall ball : table.getBalls()) {
            bw.write(String.format(FORMAT,
                    table.getIteration() / persistingMultiplier,
                    table.getSimulationTime(),
                    ball.getBallNumber(),
                    ball.getPosition().getX(),
                    ball.getPosition().getY(),
                    ball.getVelocity().getX(),
                    ball.getVelocity().getY(),
                    ball.getRadius(),
                    ball.getMass(),
                    ball.getForce().getX(),
                    ball.getForce().getY()
            ));
        }
    }

}
