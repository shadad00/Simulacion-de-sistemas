package animation;

import simulation.CommonBall;
import simulation.PocketBall;
import simulation.Table;
import simulation.collisions.Collision;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

public class OvitoWriter {

    private static String IN_FILE_42 = "/home/shadad/Desktop/tp2pod/simulacion-de-sistemas/tp3/src/main/java/animation/pool_y42.00_i0.csv";
    private static String IN_FILE_50 = "/home/shadad/Desktop/tp2pod/simulacion-de-sistemas/tp3/src/main/java/animation/pool_y50.00_i0.csv";
    private static String IN_FILE_56 = "/home/shadad/Desktop/tp2pod/simulacion-de-sistemas/tp3/src/main/java/animation/pool_y56.00_i0.csv";
    private static String OUT_FILE_42 = "/home/shadad/Desktop/tp2pod/simulacion-de-sistemas/tp3/src/main/java/animation/pool_y42.00";
    private static String OUT_FILE_50 = "/home/shadad/Desktop/tp2pod/simulacion-de-sistemas/tp3/src/main/java/animation/pool_y50.00";
    private static String OUT_FILE_56 = "/home/shadad/Desktop/tp2pod/simulacion-de-sistemas/tp3/src/main/java/animation/pool_y56.00";

    private static String FIRST_SIMULATION = "/home/shadad/Desktop/tp2pod/simulacion-de-sistemas/tp3/src/main/java/animation/Simulation1.csv";
    private static String SECOND_SIMULATION = "/home/shadad/Desktop/tp2pod/simulacion-de-sistemas/tp3/src/main/java/animation/Simulation2.csv";

    private static String FIRST_SIMULATION_OUT = "/home/shadad/Desktop/tp2pod/simulacion-de-sistemas/tp3/src/main/java/animation/Simulation1";
    private static String SECOND_SIMULATION_OUT = "/home/shadad/Desktop/tp2pod/simulacion-de-sistemas/tp3/src/main/java/animation/Simulation2";


    private static String[] IN_FILES = {IN_FILE_42, IN_FILE_50, IN_FILE_56};
    private static String[] IN_FILES_DET = {FIRST_SIMULATION,SECOND_SIMULATION};

    private static String[] OUT_FILES_DET = {FIRST_SIMULATION_OUT,SECOND_SIMULATION_OUT};

    private static String[] OUT_FILES = {OUT_FILE_42, OUT_FILE_50, OUT_FILE_56};



    public static void main(String[] args) throws IOException {
        for (int i = 0; i < IN_FILES_DET.length; i++) {
            generateAnimation(IN_FILES_DET[i], OUT_FILES_DET[i]);
        }
    }


    public static void generateAnimation(String inFile, String outFile) throws IOException {
        Parser parser = new Parser(inFile);
        OvitoWriter ovitoWriter = new OvitoWriter();
        ovitoWriter.openFile(outFile);
        for (Table table : parser) {
            ovitoWriter.writeFrame(table.getSimulationTime(),table.getBalls(),
                    table.getPocketBalls(),null);
        }
        ovitoWriter.closeFile();
    }

    private final int[][] BALL_COLORS = new int[][] {
            {255, 255, 255},
            {255, 255, 0},
            {0, 0, 255},
            {255, 0, 0},
            {113, 75, 181},
            {252, 146, 56},
            {48, 107, 106},
            {144, 19, 25},
            {20, 20, 20},
            {255, 255, 0},
            {0, 0, 255},
            {255, 0, 0},
            {113, 75, 181},
            {252, 146, 56},
            {48, 107, 106},
            {144, 19, 25},
            {57, 57, 53},
    };

    private BufferedWriter writer;
    private int frame = 0;

    public void openFile(String filePath) throws IOException {
        writer = new BufferedWriter(new FileWriter(String.format("%s.xyz", filePath), false));
    }
    public void writeFrame(final double time, Set<CommonBall> balls, Set<PocketBall> pockets, final Collision collision) throws IOException {
        final int totalParticles = balls.size() + pockets.size();

        writer.write(String.format("%s\n", totalParticles));
        writer.write(String.format("#%s (%s) <%s>\n", frame++, time, collision == null ? "none" : collision));

        writeBalls(balls);
        writePocket(pockets);

        writer.flush();
    }

    public void closeFile() throws IOException {
        writer.close();
    }

    private void writeBalls(Set<CommonBall> balls) throws IOException {
        for (final CommonBall ball : balls) {
            final String line = String.format(
                    "%s %s %s %s %s %s %s %s\n",
                    ball.getPosition().getX(),
                    ball.getPosition().getY(),
                    ball.getVelocity().getX(),
                    ball.getVelocity().getY(),
                    BALL_COLORS[ball.getBallNumber()][0] / 255.,
                    BALL_COLORS[ball.getBallNumber()][1] / 255.,
                    BALL_COLORS[ball.getBallNumber()][2] / 255.,
                    ball.getRadius()
            );

            writer.write(line);
        }
    }

    private void writePocket(Set<PocketBall> pockets) throws IOException {
        for (final PocketBall pocket : pockets) {
            final String line = String.format(
                    "%s %s %s %s %s %s %s %s\n",
                    pocket.getPosition().getX(),
                    pocket.getPosition().getY(),
                    pocket.getVelocity().getX(),
                    pocket.getVelocity().getY(),
                    125 / 255.,
                    125 / 255.,
                    125 / 255.,
                    pocket.getRadius()
            );

            writer.write(line);
        }
    }
}
