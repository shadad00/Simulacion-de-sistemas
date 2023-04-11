package animation;

import simulation.CommonBall;
import simulation.PocketBall;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.Buffer;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class OvitoWriter {

    private final int[][] BALL_COLORS = new int[][] {
            {255, 255, 255},
            {255, 255, 0}
    };

    private BufferedWriter writer;
    private int frame = 0;

    public void openFile(String filePath) throws IOException {
        writer = new BufferedWriter(new FileWriter(String.format("%s.xyz", filePath), true));
    }
    public void writeFrame(Set<CommonBall> balls, Set<PocketBall> pockets) throws IOException {
        final int totalParticles = balls.size() + pockets.size();

        writer.write(String.format("%s\n", totalParticles));
        writer.write(String.format("#%s\n", frame++));

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
                    "%s %s %s %s %s %s %s\n",
                    ball.getPosition().getX(),
                    ball.getPosition().getY(),
                    ball.getVelocity().getX(),
                    ball.getVelocity().getY(),
                    BALL_COLORS[ball.getBallNumber()][0],
                    BALL_COLORS[ball.getBallNumber()][1],
                    BALL_COLORS[ball.getBallNumber()][2]
            );

            writer.write(line);
        }
    }

    private void writePocket(Set<PocketBall> pockets) throws IOException {
        for (final PocketBall pocket : pockets) {
            final String line = String.format(
                    "%s %s %s %s %s %s %s\n",
                    pocket.getPosition().getX(),
                    pocket.getPosition().getY(),
                    pocket.getVelocity().getX(),
                    pocket.getVelocity().getY(),
                    125,
                    125,
                    125
            );

            writer.write(line);
        }
    }
}
