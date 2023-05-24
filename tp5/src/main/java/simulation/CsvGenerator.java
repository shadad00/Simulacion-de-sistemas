package simulation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CsvGenerator {

    final static String S_HEADER = "iter,time,ball_id,pos_x,pos_y,vel_x,vel_y,ball_radius,ball_mass,force_x,force_y";
    final static String S_FORMAT = "%d,%f,%d,%.15f,%.15f,%f,%f,%f,%f,%f,%f\n";
    private final BufferedWriter sbw;

    final static String F_HEADER = "time,ball_id";
    final static String F_FORMAT = "%f,%d\n";
    private final BufferedWriter fbw;


    public CsvGenerator(String outputDirectory, String outputFile, double gapWidth, int frequency )
            throws IOException {
        this(outputDirectory,outputFile, new Table(gapWidth, frequency), 10);
    }

    public CsvGenerator(String outputDirectory, String outputFile, Table table,int persistingMultiplier)
            throws IOException{

        File siloCsvFile = new File( outputDirectory+"silo/" + outputFile +".csv");
        if (!siloCsvFile.exists() && !siloCsvFile.createNewFile()) {
            throw new IOException("Unable to create output csv file.");
        }
        File fluxCsvFile = new File( outputDirectory+ "flux/" + outputFile +".csv");
        if (!fluxCsvFile.exists() && !fluxCsvFile.createNewFile()) {
            throw new IOException("Unable to create output csv file.");
        }

        sbw = new BufferedWriter(new FileWriter(siloCsvFile));
        sbw.write(S_HEADER + "\n");
        fbw = new BufferedWriter(new FileWriter(fluxCsvFile));
        fbw.write(F_HEADER + "\n");
        writeTable(table, persistingMultiplier);
        int i = 0;
        int j = 0;
        Table last = new Table(table);
        for (Table currentTable : table){
            i++;
            if ( i == persistingMultiplier){
                writeTable(currentTable, persistingMultiplier);
                j++;
                i = 0;
            }
            last = currentTable;
        }
        if (last != null)
            writeTableByIteration(last, j + 1);
        sbw.close();
        fbw.close();
    }

    private void writeTableByIteration(Table table, int iteration) throws IOException {
        for (CommonBall ball : table.getBalls()) {
            sbw.write(String.format(S_FORMAT,
                    iteration,
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


    private void writeTable(Table table, int persistingMultiplier) throws IOException {
        for (CommonBall ball : table.getBalls()) {
            sbw.write(String.format(S_FORMAT,
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
        for (Integer ballId : table.getOutBallsId()){
            fbw.write(String.format(F_FORMAT,table.getSimulationTime(), ballId));
        }
    }

}
