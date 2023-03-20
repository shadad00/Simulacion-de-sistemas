import java.io.IOException;
import java.util.HashSet;

public class Ovito {

    public static final int MAX_FRAMES = 1;

    public static void main(String[] args) throws IOException {
        // Reemplazar esto por la grilla real
        final int gridSize = 50;
        final Cell[][] cells = new Cell[gridSize][gridSize];
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                if (i == 0 || j == 0 || i == cells.length - 1 || j == cells[i].length - 1) {
                    cells[i][j] = new SolidCell(new HashSet<>());
                } else {
                    cells[i][j] = new CommonCell();
                }
            }
        }

        final int nParticles = 3000;
        for (int i = 0; i < nParticles; i++) {
            final int xCell = (int) Math.floor(Math.random() * gridSize / 2);
            final int yCell = (int) Math.floor(Math.random() * gridSize);
            final int velocityIndex = (int) Math.floor(Math.random() * Velocity.values().length);

            final Velocity velocity = Velocity.values()[velocityIndex];

            cells[yCell][xCell].addParticle(new Particle(velocity));
        }

        Grid grid = new Grid(cells, 20, 20);
        for (int i = 0; i < MAX_FRAMES; i++) {
            Frame.writeHexagonalCellVelocities(grid, i, "res/test_velocities_");
//            grid = grid.getNextGrid();
        }
    }
}
