import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public  class NoPeriodicGrid implements Grid {
    protected final double length;
    protected final int cellQuantity;
    protected final double cellLength;
    protected final double cutoffRadius;
    protected Cell[][] cells;
    protected final Map<Particle,Set<ParticleAndDistance>> distances;

    public NoPeriodicGrid(double length, int cellQuantity, double cutoffRadius) {
        if (length / cellQuantity <= cutoffRadius)
            throw new InvalidParameterException("Invalid parameters provided");

        this.length = length;
        this.cellQuantity = cellQuantity;
        this.cellLength = length / cellQuantity;
        this.cutoffRadius = cutoffRadius;
        this.distances = new HashMap<>();

        initializeCell(this.cellQuantity);
    }

    protected void initializeCell(int dim){
        this.cells = new Cell[dim][dim];
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                this.cells[i][j] = new Cell(i, j);
            }
        }
    }

    public void getParticlesMap() {
        final Map<Integer, Particle> particlesMap = new HashMap<Integer, Particle>();

        for (int x = 0; x < this.cellQuantity; x++) {
            for (int y = 0; y < this.cellQuantity; y++) {
                final Cell cell = this.cells[x][y];

                for (final Particle particle : cell.getParticles()) {
                    particlesMap.put(particle.getParticleId(), particle);
                }
            }
        }
    }
    
    public void setParticles(List<Particle> particleList){
        particleList.forEach(this::saveParticle);
    }

    protected void saveParticle(Particle particle){
        cells[getRowFromParticle(particle)][getColFromParticle(particle)]
                .addParticle(particle);
    }

    public void clearParticles() {
        for (int i = 0; i < this.cellQuantity; i++) {
            for (int j = 0; j < this.cellQuantity; j++) {
                this.cells[i][j] = new Cell(i, j);
            }
        }
    }


    public void computeDistanceBetweenParticles(){
        for (int x = 0; x < cells.length; x++)
            for (int y = 0; y < cells.length; y++) {
                final Cell currentCell = this.cells[x][y];

                if (currentCell.getParticles().size() > 0) {
                    final Set<Particle> particlesInNeighborCells = getCandidateParticles(x, y);
                    final Set<Particle> particlesInCurrentCell = currentCell.getParticles();

                    computeDistancesToAdjacentParticles(particlesInCurrentCell, particlesInNeighborCells);
                }
            }
    }

    protected void computeDistancesToAdjacentParticles(
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

                double distance = currentCellParticle.distanceTo(candidate);

                if (distance <= currentCellParticle.getCutOffRadius()) {
                    this.distances.putIfAbsent(currentCellParticle, new HashSet<>());
                    this.distances.get(currentCellParticle).add(new ParticleAndDistance(candidate, distance));

                    this.distances.putIfAbsent(candidate, new HashSet<>());
                    this.distances.get(candidate).add(new ParticleAndDistance(currentCellParticle,distance));
                }
            }
    }

    // Este metodo deberia ser el unico a reimplementar en la grilla periodica, el resto se mantiene igual
    protected Set<Cell> getCandidateCells(final int xCell, final int yCell) {
        // Ponemos como candidatos a la celda en la que estamos y las que estan sobre la L
        final int[][] neighborOffsets = { {0, 0}, {1, 0}, {1, 1}, {1, 0}, {1, -1} };
        final Set<Cell> candidateCells = new HashSet<>();

        for (int[] offset : neighborOffsets) {
            final int xOffset = offset[0];
            final int yOffset = offset[1];
            final int xNeighbor = xCell + xOffset;
            final int yNeighbor = yCell + yOffset;

            final boolean isValidX = xNeighbor >= 0 && xNeighbor < this.cellQuantity;
            final boolean isValidY = yNeighbor >= 0 && yNeighbor < this.cellQuantity;

            if (isValidX && isValidY)
                candidateCells.add(this.cells[xNeighbor][yNeighbor]);
        }

        return candidateCells;
    }

    protected Set<Particle> getCandidateParticles(final int xCell, final int yCell) {
        final Set<Cell> candidateCells = getCandidateCells(xCell, yCell);
        final Set<Particle> candidateParticles = new HashSet<>();

        for (Cell cell : candidateCells)
            candidateParticles.addAll(cell.getParticles());

        return candidateParticles;
    }

    protected int getColFromParticle(Particle particle){
        return (int) Math.floor(particle.getX() / this.cellLength);
    }

    protected int getRowFromParticle(Particle particle){
        int particleRow = (int) Math.floor(particle.getY() / this.cellLength);
        return (this.cellQuantity-1) - particleRow;
    }

    public Set<ParticleAndDistance> getDistanceSet(Particle particle){
        return this.distances.getOrDefault(particle, new HashSet<>());
    }

}
