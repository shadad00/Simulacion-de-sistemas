package simulation;

import java.util.HashSet;
import java.util.Set;

public class PeriodicGridHalfDistance extends Grid {

    private double halfLength;

    public PeriodicGridHalfDistance(final double length, final int cellQuantity, final double cutoffRadius) {
        super(length, cellQuantity, cutoffRadius);

        this.halfLength = length / 2;
    }

    @Override
    protected void placeParticle(final Particle particle) {
        final Cell cell = this.getParticleCell(particle);
        cell.addParticle(particle);
    }

    @Override
    protected double distanceBetweenParticles(final Particle p1, final Particle p2) {
        final double totalRadius = p1.getRadius() + p2.getRadius();

        double xDiff = Math.abs(p1.getX() - p2.getX());
        if (xDiff > halfLength)
            xDiff = length - xDiff;

        double yDiff = Math.abs(p1.getY() - p2.getY());
        if (yDiff > halfLength)
            yDiff = length - yDiff;

        final double distance = Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2)) - totalRadius;

        return distance < 0 ? 0 : distance;
    }

    @Override
    protected Set<Cell> getCandidateCells(int xCell, int yCell) {
        final Set<Cell> candidateCells = new HashSet<>();

        candidateCells.add(this.cells[yCell][xCell]);

        for (int[] offset : NEIGHBOR_OFFSETS) {
            final int xOffset = offset[0];
            final int yOffset = offset[1];
            // Haciendo el modulo damos la vuelta a la grilla automaticamente
            // floorMod es necesario porque si no, usando % te tira valores negativos tambien y rompe todo
            final int xNeighbor = Math.floorMod(xCell + xOffset, this.cellQuantity);
            final int yNeighbor = Math.floorMod(yCell + yOffset, this.cellQuantity);

            candidateCells.add(this.cells[yNeighbor][xNeighbor]);
        }

        return candidateCells;
    }
}
