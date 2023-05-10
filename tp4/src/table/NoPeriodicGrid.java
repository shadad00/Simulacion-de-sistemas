package table;


import java.util.*;

public class NoPeriodicGrid extends Grid {

    public NoPeriodicGrid(double length, int cellQuantity) {
        super(length, cellQuantity);
    }

    @Override
    public boolean isPeriodic() {
        return false;
    }

    @Override
    protected void placeBall(CommonBall ball) {
        final Cell cell = getBallCell(ball);

        cell.addBall(ball);
    }

    @Override
    public double distanceBetweenBalls(final CommonBall p1, final CommonBall p2) {
        final double xDelta = p1.getPosition().getX() - p2.getPosition().getX();
        final double yDelta = p1.getPosition().getY() - p2.getPosition().getY();
        final double totalRadius = p1.getRadius() + p2.getRadius();
        final double distance = Math.sqrt((Math.pow(xDelta, 2) + Math.pow(yDelta, 2))) - totalRadius;

        return distance ;
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


    public void clearParticles() {
        for (int i = 0; i < this.cellQuantity; i++) {
            for (int j = 0; j < this.cellQuantity; j++) {
                this.cells[i][j] = new Cell(i, j);
            }
        }
    }

}
