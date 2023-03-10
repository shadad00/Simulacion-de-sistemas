package simulation;

import java.util.HashSet;
import java.util.Set;

public class PeriodicGridDuplicateBorders extends Grid {

    private int extendedCellQuantity;

    public PeriodicGridDuplicateBorders(double length, int cellQuantity, double cutoffRadius) {
        super(length, cellQuantity, cutoffRadius);

        this.extendedCellQuantity = cellQuantity + 2;
    }

    @Override
    protected void initializeCells(final int dim) {
        super.initializeCells(cellQuantity + 2);
    }

    @Override
    protected Cell getParticleCell(final Particle particle) {
        final int particleColumn = (int) Math.floor(particle.getX() / this.cellLength);
        final int particleRow = (int) Math.floor(particle.getY() / this.cellLength);

        return cells[particleRow + 1][particleColumn + 1];
    }

    @Override
    protected void placeParticle(final Particle particle) {
        final Cell cell = getParticleCell(particle);
        cell.addParticle(particle);

        placeVirtualParticles(cell.getRow(), cell.getCol(), particle);
    }

    private void placeVirtualParticles(int row , int col, Particle particle) {
        int newRow=-1, newCol=-1;
        Particle diagonalVirtualParticle = particle.getVirtualParticle();

        // Este cellQuantity tiene valor sin aplicar +2 de tener bordes duplicados
        if (row == 1 || row == this.cellQuantity) {
            Particle rowVirtualParticle = particle.getVirtualParticle();
            if (row == 1) {
                rowVirtualParticle.setY(rowVirtualParticle.getY() + this.length);
                newRow = this.cellQuantity + 1 ;
            } else {
                rowVirtualParticle.setY(rowVirtualParticle.getY() - this.length);
                newRow = 0 ;
            }
            cells[newRow][col].addParticle(rowVirtualParticle);
            diagonalVirtualParticle.setY(rowVirtualParticle.getY());
        }

        if (col == 1 || col == this.cellQuantity) {
            Particle columnVirtualParticle = particle.getVirtualParticle();

            if (col == 1){
                columnVirtualParticle.setX(columnVirtualParticle.getX() + this.length);
                newCol = this.cellQuantity + 1 ;
            }
            else{
                columnVirtualParticle.setX(columnVirtualParticle.getX() - this.length);
                newCol = 0;
            }

            cells[row][newCol].addParticle(columnVirtualParticle);
            diagonalVirtualParticle.setX(columnVirtualParticle.getX());
        }

        if (newRow >= 0 && newCol >= 0)
            cells[newRow][newCol].addParticle(diagonalVirtualParticle);
    }

    @Override
    protected double distanceBetweenParticles(Particle p1, Particle p2) {
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

            final boolean isValidX = xNeighbor >= 0 && xNeighbor < this.extendedCellQuantity;
            final boolean isValidY = yNeighbor >= 0 && yNeighbor < this.extendedCellQuantity;

            if (isValidX && isValidY)
                candidateCells.add(this.cells[yNeighbor][xNeighbor]);
        }

        return candidateCells;
    }

    @Override
    public void computeDistanceBetweenParticles() {
        for (int y = 1; y <= cellQuantity; y++)
            for (int x = 1; x <= cellQuantity; x++) {
                final Cell currentCell = this.cells[y][x];

                if (!currentCell.isEmpty()) {
                    final Set<Particle> particlesInNeighborCells = getCandidateParticles(x, y);
                    final Set<Particle> particlesInCurrentCell = currentCell.getParticles();

                    computeDistancesToAdjacentParticles(particlesInCurrentCell, particlesInNeighborCells);
                }
            }
    }


}
