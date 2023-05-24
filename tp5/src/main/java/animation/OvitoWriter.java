package animation;


import simulation.CommonBall;
import simulation.Table;
import utils.Pair;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

import static simulation.UnitConstants.SILO_HEIGHT;
import static simulation.UnitConstants.SILO_WIDTH;

public class OvitoWriter {

    private static String IN_FILE_42 = "tp5/out/silo/silo_fq50_gap0.03_i0.csv";

    private static String OUT_FILE_42 = "tp5/out/silo/silo_fq50_gap0.03_i0";

    private static String[] IN_FILES = {IN_FILE_42};
    private static String[] OUT_FILES = {OUT_FILE_42};
    private static boolean POCKET = true;

    private static CommonBall[] FIXED_BALLS = {
            new CommonBall(-1,Pair.of(0,0),1.,.001),
            new CommonBall(-1,Pair.of(SILO_WIDTH,0),1.,.001),
            new CommonBall(-1,Pair.of(0,SILO_HEIGHT),1.,.001),
            new CommonBall(-1,Pair.of(SILO_WIDTH,SILO_HEIGHT),1.,.001)
    };

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
            ovitoWriter.writeFrame(table.getOffset(),
                    table.getSimulationTime(),table.getBalls());
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
    public void writeFrame(final double offset, final double time, Set<CommonBall> balls/*, Set<PocketBall> pockets*/) throws IOException {
        final int totalParticles = balls.size() + FIXED_BALLS.length;

        writer.write(String.format("%s\n", totalParticles));
        writer.write(String.format("#%s (%s) <%s>\n", frame++, time, "none"));

        writeBalls(balls);
        writeFixed(offset);

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
                    BALL_COLORS[5][0] / 255.,
                    BALL_COLORS[5][1] / 255.,
                    BALL_COLORS[5][2] / 255.,
                    ball.getRadius()
            );

            writer.write(line);
        }
    }

    private void writeFixed(double offset) throws IOException {
        for (final CommonBall ball : FIXED_BALLS) {
            final String line = String.format(
                    "%s %s %s %s %s %s %s %s\n",
                    ball.getPosition().getX(),
                    ball.getPosition().getY() + offset,
                    ball.getVelocity().getX(),
                    ball.getVelocity().getY() + offset,
                    125 / 255.,
                    125 / 255.,
                    125 / 255.,
                    ball.getRadius()
            );

            writer.write(line);
        }
    }
}
