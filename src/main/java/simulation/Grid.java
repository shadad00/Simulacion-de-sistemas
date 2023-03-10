package simulation;

import java.security.InvalidParameterException;
import java.util.*;

public abstract class Grid {
    // Formato de offsets {x, y}
    final static int[][] NEIGHBOR_OFFSETS = { {1, 0}, {1, 1}, {0, 1}, {1, -1} };

    protected Cell[][] cells;
    protected Map<Integer, Particle> idParticlesMap;
    protected double length;
    protected int cellQuantity;
    protected double cellLength;
    protected double cutoffRadius;
    protected Map<Particle, Set<ParticleAndDistance>> distances;

    public Grid(final double length, final int cellQuantity, final double cutoffRadius) {
        if (length / cellQuantity <= cutoffRadius)
            throw new InvalidParameterException("Invalid parameters provided");

        this.length = length;
        this.cellQuantity = cellQuantity;
        this.cutoffRadius = cutoffRadius;
        this.cellLength = length / cellQuantity;
        this.distances = new HashMap<>();

        initializeCells(this.cellQuantity);
    }

    public abstract boolean isPeriodic();

    public void placeParticles(final Collection<Particle> particles) {
        particles.forEach(this::placeParticle);
    }

    protected abstract void placeParticle(final Particle particle);

    public void computeDistanceBetweenParticles(){
        for (int y = 0; y < cells.length; y++)
            for (int x = 0; x < cells.length; x++) {
                final Cell currentCell = this.cells[y][x];

                if (!currentCell.isEmpty()) {
                    final Set<Particle> particlesInNeighborCells = getCandidateParticles(x, y);
                    final Set<Particle> particlesInCurrentCell = currentCell.getParticles();

                    computeDistancesToAdjacentParticles(particlesInCurrentCell, particlesInNeighborCells);
                }
            }
    }

    protected Map<Particle, Set<ParticleAndDistance>> computeDistancesToAdjacentParticles(
            final Set<Particle> currentCellParticles,
            final Set<Particle> neighborCellsParticles
    ) {
        final Set<Particle> candidates = new HashSet<>();
        candidates.addAll(currentCellParticles);
        candidates.addAll(neighborCellsParticles);

        for (final Particle currentCellParticle : currentCellParticles)
            for (final Particle candidate : candidates) {
                if (currentCellParticle.equals(candidate))
                    continue;

                final double distance = distanceBetweenParticles(currentCellParticle, candidate);

                if (distance <= currentCellParticle.getCutOffRadius()) {
                    distances.putIfAbsent(currentCellParticle, new HashSet<>());
                    distances.get(currentCellParticle).add(new ParticleAndDistance(candidate, distance));

                    distances.putIfAbsent(candidate, new HashSet<>());
                    distances.get(candidate).add(new ParticleAndDistance(currentCellParticle,distance));
                }
            }

        return distances;
    }
    protected abstract double distanceBetweenParticles(final Particle particle1, final Particle particle2);

    protected void initializeCells(int dim){
        this.cells = new Cell[dim][dim];

        for (int y = 0; y < dim; y++) {
            for (int x = 0; x < dim; x++) {
                this.cells[y][x] = new Cell(x, y);
            }
        }
    }

    protected Cell getParticleCell(final Particle particle) {
        final int particleColumn = (int) Math.floor(particle.getX() / this.cellLength);
        final int particleRow = (int) Math.floor(particle.getY() / this.cellLength);

        return cells[particleRow][particleColumn];
    }

    public void clearParticles() {
        for (int i = 0; i < this.cellQuantity; i++) {
            for (int j = 0; j < this.cellQuantity; j++) {
                this.cells[i][j] = new Cell(i, j);
            }
        }
    }

    protected abstract Set<Cell> getCandidateCells(final int xCell, final int yCell);

    protected Set<Particle> getCandidateParticles(final int xCell, final int yCell) {
        final Set<Cell> candidateCells = getCandidateCells(xCell, yCell);
        final Set<Particle> candidateParticles = new HashSet<>();

        for (final Cell cell : candidateCells)
            candidateParticles.addAll(cell.getParticles());

        return candidateParticles;
    }

    public Set<ParticleAndDistance> getNeighbors(final Particle particle) {
        return distances.get(particle);
    }
}