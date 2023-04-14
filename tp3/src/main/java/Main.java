import simulation.CommonBall;
import simulation.Table;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException {
        final Table table = new Table(56.2, 224, 112);

        File csvFile = new File("prueba.csv");
        if (!csvFile.exists() && !csvFile.createNewFile()) {
            throw new IOException("boludito");
        }
        BufferedWriter bw = new BufferedWriter(new FileWriter(csvFile));
        final String HEADER = "iter,time,ball_id,pos_x,pos_y,vel_x,vel_y,ball_radius,ball_mass,collision_count";
        final String FORMAT = "%d,%f,%d,%f,%f,%f,%f,%f,%f,%d\n";

        bw.write(HEADER + "\n");
        for (Table currentTable : table) {
            for (CommonBall ball : currentTable.getBalls()) {
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
                        ball.getTotalCollisions()
                        ));
            }
        }
    }
}
