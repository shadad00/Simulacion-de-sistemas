package simulation;

import java.security.InvalidParameterException;
import java.util.*;

public class NoPeriodicGrid extends Grid {

    public NoPeriodicGrid(double length, int cellQuantity, double cutoffRadius) {
        super(length, cellQuantity, cutoffRadius);
    }

    @Override
    protected void placeParticle(Particle particle) {
        final Cell cell = getParticleCell(particle);
        cell.addParticle(particle);
    }

    @Override
    public double distanceBetweenParticles(final Particle p1, final Particle p2) {
        final double xDelta = p1.getX() - p2.getX();
        final double yDelta = p1.getY() - p2.getY();
        final double totalRadius = p1.getRadius() + p2.getRadius();
        final double distance = Math.sqrt((Math.pow(xDelta, 2) + Math.pow(yDelta, 2))) - totalRadius;

        return distance >= 0 ? distance : 0 ;
    }

    @Override
    protected Set<Cell> getCandidateCells(final int xCell, final int yCell) {
        final Set<Cell> candidateCells = new HashSet<>();

        candidateCells.add(this.cells[yCell][xCell]);

        for (int[] offset : NEIGHBOR_OFFSETS) {
            final int xOffset = offset[0];
            final int yOffset = offset[1];
            final int xNeighbor = xCell + xOffset;
            final int yNeighbor = yCell + yOffset;

            final boolean isValidX = xNeighbor >= 0 && xNeighbor < this.cellQuantity;
            final boolean isValidY = yNeighbor >= 0 && yNeighbor < this.cellQuantity;

            if (isValidX && isValidY)
                candidateCells.add(this.cells[yNeighbor][xNeighbor]);
        }

        return candidateCells;
    }

}
