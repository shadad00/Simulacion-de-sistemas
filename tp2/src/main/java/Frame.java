import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;

public class Frame {

    public static void writeHexagonalCellVelocities(final Grid grid, final int frame, final String path) throws IOException {
        final Cell[][] cells = grid.getCells();
        final BufferedWriter writer = new BufferedWriter(new FileWriter(String.format("%s%s.xyz", path, frame)));

        writer.write(String.format("%s\n", cells.length * cells[0].length));
        writer.newLine();

        grid.hexagonalIteration((cell, xCell, yCell) -> {
            final Collection<Particle> particles = cell.getParticles();
            final double xTotalVelocity = Velocity.xMomentum(particles.stream().map(Particle::getVelocity).collect(Collectors.toList()));
            final double yTotalVelocity = Velocity.yMomentum(particles.stream().map(Particle::getVelocity).collect(Collectors.toList()));
            
            double rCell, gCell, bCell, alpha;

            if (cell.isSolid()) {
                rCell = 125;
                gCell = 125;
                bCell = 125;
                alpha = 0;
            } else {
                rCell = cell.isEmpty() ? 0 : 255;
                gCell = cell.isEmpty() ? 255 : 0;
                bCell = 0;
                alpha = 1 - ((double) (particles.size() + 2) / 8);
            }

            final String line = String.format(
                    "%s %s %s %s %s %s %s %s\n",
                    xCell,
                    yCell,
                    xTotalVelocity,
                    yTotalVelocity,
                    rCell,
                    gCell,
                    bCell,
                    alpha
            );

            try {
                writer.write(line);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        writer.close();
    }

}
