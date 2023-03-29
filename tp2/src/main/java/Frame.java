import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Frame {

    public static void writeHexagonalCellVelocities(final Grid grid, final int frame, final String path) throws IOException {
        final Cell[][] cells = grid.getCells();
        final BufferedWriter writer = new BufferedWriter(new FileWriter(String.format("%s.xyz", path), true));

        writer.write(String.format("%s\n", cells.length * cells[0].length));
        writer.write(String.format("#%s\n", frame));

        grid.hexagonalIteration((cell, xCell, yCell) -> {
            final Collection<Particle> particles = cell.getParticles();
            final double xTotalVelocity = Velocity.xMomentum(particles.stream().map(Particle::getVelocity).collect(Collectors.toList()));
            final double yTotalVelocity = -Velocity.yMomentum(particles.stream().map(Particle::getVelocity).collect(Collectors.toList()));

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
                alpha = 1 - ((double) (particles.size() + 8) / 16);
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

    public static void writeDensityAreas(final Grid grid, final int frame, final String path) throws IOException {
        final DensityArea[][] areaGrid = cellsToDensityArea(grid, 60);
        final int areaGridSize = areaGrid.length;
        final Cell[][] cells = grid.getCells();

        if (cells.length != cells[0].length) {
            throw new IllegalArgumentException("cellsToDensityArea method works with squared grids only");
        }

        final BufferedWriter writer = new BufferedWriter(new FileWriter(String.format("%s.xyz", path), true));

        writer.write(String.format("%s\n", areaGridSize * areaGridSize));
        writer.write(String.format("#%s\n", frame));

        for (int i = 0; i < areaGridSize; i++) {
            for (int j = 0; j < areaGridSize; j++) {
                final DensityArea area = areaGrid[i][j];
                final double xTotalVelocity = area.xMomentum();
                final double yTotalVelocity = -area.yMomentum();
                final double vectorLength = Math.sqrt(Math.pow(xTotalVelocity, 2) + Math.pow(yTotalVelocity, 2));

                final String line = String.format(
                        "%s %s %s %s %s %s %s %s\n",
                        j,
                        i,
                        xTotalVelocity / vectorLength,
                        yTotalVelocity / vectorLength,
                        255,
                        0,
                        0,
                        1 - Math.min(area.size() / 3.0, 1)
                );

                try {
                    writer.write(line);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        writer.close();
    }

    private static DensityArea[][] cellsToDensityArea(final Grid grid, final int areasPerSide) {
        final Cell[][] cells = grid.getCells();
        final double scalingFactor = cells.length * 1.0 / areasPerSide;
        final DensityArea[][] areaGrid = new DensityArea[areasPerSide][areasPerSide];
        for (int i = 0; i < areasPerSide; i++) {
            for (int j = 0; j < areasPerSide; j++) {
                areaGrid[i][j] = new DensityArea();
            }
        }

        grid.hexagonalIteration((cell, xCell, yCell) -> {
            final int xArea = (int) Math.floor(xCell / scalingFactor);
            final int yArea = (int) Math.floor(yCell / Grid.HEXAGONAL_GRID_VERTICAL_SPACING / scalingFactor);

            areaGrid[yArea][xArea].addParticles(cell.getParticles());
        });

        return areaGrid;
    }

    static class DensityArea {

        private Set<Particle> particles;
        public DensityArea() {
            this.particles = new HashSet<>();
        }

        public void addParticles(final Collection<Particle> particle) {
            particles.addAll(particle);
        }

        public double xMomentum() {
            final List<Velocity> velocitySet = particles.stream().map(Particle::getVelocity).collect(Collectors.toList());

            return Velocity.xMomentum(velocitySet);
        }

        public double yMomentum() {
            final List<Velocity> velocitySet = particles.stream().map(Particle::getVelocity).collect(Collectors.toList());

            return Velocity.yMomentum(velocitySet);
        }

        public int size() {
            return particles.size();
        }
    }
}
