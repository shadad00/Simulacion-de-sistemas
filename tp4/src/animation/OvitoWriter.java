package animation;


import table.CommonBall;
import table.PocketBall;
import table.Table;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

public class OvitoWriter {

    private static String IN_FILE_42 = "tp4/src/animation/pool_y56.00_i0.csv";

    private static String OUT_FILE_42 = "tp4/src/animation/pool_y42.00_i0";

    private static boolean POCKET = true;

    private static String[] IN_FILES = {IN_FILE_42};

    private static String[] OUT_FILES = {OUT_FILE_42};



    public static void main(String[] args) throws IOException {
        for (int i = 0; i < IN_FILES.length; i++) {
            generateAnimation(IN_FILES[i], OUT_FILES[i], POCKET);
        }
    }


    public static void generateAnimation(String inFile, String outFile, boolean pocket) throws IOException {
        Parser parser = new Parser(inFile, pocket);
        OvitoWriter ovitoWriter = new OvitoWriter();
        ovitoWriter.openFile(outFile);
        for (Table table : parser) {
            ovitoWriter.writeFrame(table.getSimulationTime(),table.getBalls(),
                    table.getPocketBalls());
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
    public void writeFrame(final double time, Set<CommonBall> balls, Set<PocketBall> pockets) throws IOException {
        final int totalParticles = balls.size() + pockets.size();

        writer.write(String.format("%s\n", totalParticles));
        writer.write(String.format("#%s (%s) <%s>\n", frame++, time, "none"));

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
