package simulation;

import java.security.InvalidParameterException;
import java.util.*;

public abstract class Grid {
    // Formato de offsets {x, y}
    final static int[][] NEIGHBOR_OFFSETS = { {1, 0}, {1, 1}, {0, 1}, {1, -1} };

    protected Cell[][] cells;
    protected Map<Integer, CommonBall> idParticlesMap;
    protected double width;
    protected int cellQuantity;
    protected double cellWidth;
    protected Map<CommonBall, Set<BallAndDistance>> distances;

    public Grid(final double width, final int cellQuantity) {
        this.width = width;
        this.cellQuantity = cellQuantity;
        this.cellWidth = width / cellQuantity;
        this.distances = new HashMap<>();

        initializeCells(this.cellQuantity);
    }

    public abstract boolean isPeriodic();

    public void placeBalls(final Collection<CommonBall> balls) {
        validateCutoffRadius(balls);

        balls.forEach(this::placeBall);
    }

    private void validateCutoffRadius(final Collection<CommonBall> balls) {
        final double maxRadius = balls.stream().mapToDouble(value -> value.getRadius()).max().getAsDouble();
        final double maxCutoffRadius = maxRadius;

        // L / M > max_radio_p + max_radio_p + max_radio_alcance
        if (width / cellQuantity <= maxRadius * 2 + maxCutoffRadius)
            throw new InvalidParameterException("L/M should be greater than maxRadius * 2 + maxCutoffRadius");
    }

    protected abstract void placeBall(final CommonBall ball);

    public void computeDistanceBetweenBalls(){
        for (int y = 0; y < cells.length; y++)
            for (int x = 0; x < cells.length; x++) {
                final Cell currentCell = this.cells[y][x];

                if (!currentCell.isEmpty()) {
                    final Set<CommonBall> ballsInNeighborCells = getCandidateBalls(x, y);
                    final Set<CommonBall> ballsInCurrentCell = currentCell.getBalls();

                    computeDistancesToAdjacentBalls(ballsInCurrentCell, ballsInNeighborCells);
                }
            }
    }

    protected Map<CommonBall, Set<BallAndDistance>> computeDistancesToAdjacentBalls(
            final Set<CommonBall> currentCellBalls,
            final Set<CommonBall> neighborCellsBalls
    ) {
        final Set<CommonBall> candidates = new HashSet<>();
        candidates.addAll(currentCellBalls);
        candidates.addAll(neighborCellsBalls);

        for (final CommonBall currentCellBall : currentCellBalls)
            for (final CommonBall candidate : candidates) {
                if (currentCellBall.equals(candidate))
                    continue;

                final double distance = distanceBetweenBalls(currentCellBall, candidate);
                final double cutoffRadius = 0;

                if (distance <= cutoffRadius) {
                    distances.putIfAbsent(currentCellBall, new HashSet<>());
                    distances.get(currentCellBall).add(new BallAndDistance(candidate, distance));

                    distances.putIfAbsent(candidate, new HashSet<>());
                    distances.get(candidate).add(new BallAndDistance(currentCellBall,distance));
                }
            }

        return distances;
    }
    protected abstract double distanceBetweenBalls(final CommonBall particle1, final CommonBall particle2);

    protected void initializeCells(int dim){
        this.cells = new Cell[dim + 5][dim + 5];

        for (int y = 0; y < dim + 5; y++) {
            for (int x = 0; x < dim + 5; x++) {
                this.cells[y][x] = new Cell(x, y);
            }
        }
    }

    protected Cell getBallCell(final CommonBall ball) {
        final int particleColumn = (int) Math.floor(ball.getPosition().getX() / this.cellWidth);
        final int particleRow = (int) Math.floor(ball.getPosition().getY() / this.cellWidth);

        return cells[particleRow + 5 ][particleColumn + 5];
    }


    protected abstract Set<Cell> getCandidateCells(final int xCell, final int yCell);

    protected Set<CommonBall> getCandidateBalls(final int xCell, final int yCell) {
        final Set<Cell> candidateCells = getCandidateCells(xCell, yCell);
        final Set<CommonBall> candidateParticles = new HashSet<>();

        for (final Cell cell : candidateCells)
            candidateParticles.addAll(cell.getBalls());

        return candidateParticles;
    }

    public Set<BallAndDistance> getNeighbors(final CommonBall ball) {
        return distances.get(ball);
    }
}