import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PeriodicGrid extends NoPeriodicGrid implements Grid {
    public PeriodicGrid(double length, int cellQuantity, double cutoffRadius) {
        super(length, cellQuantity, cutoffRadius);
    }

    @Override
    protected Set<Cell> getCandidateCells(final int xCell, final int yCell) {
        final int[][] neighborOffsets = { {0, 0}, {1, 0}, {1, 1}, {1, 0}, {1, -1} };
        final Set<Cell> candidateCells = new HashSet<>();

        for (int[] offset : neighborOffsets) {
            final int xOffset = offset[0];
            final int yOffset = offset[1];
            // Haciendo el modulo nos ahorramos tener que hacer todos los casos borde
            final int xNeighbor = Math.floorMod(xCell + xOffset, this.cellQuantity);
            final int yNeighbor = Math.floorMod(yCell + yOffset, this.cellQuantity);

            candidateCells.add(this.cells[xNeighbor][yNeighbor]);
        }

        return candidateCells;
    }
}
