import java.security.InvalidParameterException;
import java.util.*;

public  class NoPeriodicGrid {
    protected final double length;
    protected final int cellQuantity;
    protected final double cellLength;
    protected final double cutoffRadius;
    protected Cell[][] cells;
    protected final Map<Particle,Set<ParticleAndDistance>> distances ;

    public NoPeriodicGrid(double length, int cellQuantity, double cutoffRadius) {
        if((length / cellQuantity) <= cutoffRadius)
            throw new InvalidParameterException("Invalid parameters provided");
        this.length = length;
        this.cellQuantity = cellQuantity;
        this.cellLength = (length / cellQuantity);
        this.cutoffRadius = cutoffRadius;
        this.distances = new HashMap<>();
        initializeCell(this.cellQuantity);
    }

    protected void initializeCell(int dim){
        this.cells = new Cell[dim][dim];
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                this.cells[i][j]= new Cell(i,j);
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

    public void clearGrid(){
        for (int i = 0; i <this.cellQuantity; i++) {
            for (int j = 0; j < this.cellQuantity; j++) {
                this.cells[i][j]= new Cell(i,j);
            }
        }
    }


    protected void computeDistanceBetweenParticles(){
        for (int x = this.cellQuantity - 1; x >=0 ; x--)
            for (int y = 0; y < this.cellQuantity ; y++)
                if(this.cells[x][y].getParticles().size() > 0)
                    computeDistancesToAdjacentParticles(checkAdjacent(x,y));
    }



    protected void computeDistancesToAdjacentParticles(Set<Particle> adjacentParticlesSet){
        List<Particle> adjacentParticles = new ArrayList<>(adjacentParticlesSet);
        for (int i = 0; i < adjacentParticles.size(); i++)
            for (int j = i+1; j <adjacentParticles.size() ; j++) {
                double distance = adjacentParticles.get(i).distanceTo(adjacentParticles.get(j));
                if(  distance <= adjacentParticles.get(i).getCutOffRadius() ){
                    this.distances.putIfAbsent( adjacentParticles.get(i), new HashSet<>());
                    this.distances.get(adjacentParticles.get(i)).add(
                            new ParticleAndDistance(adjacentParticles.get(j),distance)
                    );
                    this.distances.putIfAbsent( adjacentParticles.get(j) , new HashSet<>());
                    this.distances.get(adjacentParticles.get(j)).add(
                            new ParticleAndDistance(adjacentParticles.get(i),distance)
                    );
                }
            }
    }


    protected Set<Particle> checkAdjacent(int i , int j){
        Set<Particle> adjacentParticles = new HashSet<>(this.cells[i][j].getParticles());
        if((i+1) < this.cellQuantity && (j+1) < this.cellQuantity)
            adjacentParticles.addAll(this.cells[(i+1)][(j+1)].getParticles());
        if((j+1) < this.cellQuantity)
            adjacentParticles.addAll(this.cells[(i)][(j+1)].getParticles());
        if((i-1) >= 0)
            adjacentParticles.addAll(this.cells[(i-1)][(j)].getParticles());
        if((j+1) < this.cellQuantity && (i-1) >=0 )
            adjacentParticles.addAll(this.cells[(i-1)][(j+1)].getParticles());
        return adjacentParticles;
    }

    protected int getColFromParticle(Particle particle){
        return (int) Math.floor(particle.getX() / this.cellLength);
    }

    protected int getRowFromParticle(Particle particle){
        int particleRow = (int) Math.floor(particle.getY() / this.cellLength);
        return (this.cellQuantity-1) - particleRow;
    }

    public Set<ParticleAndDistance> getDistance(Particle particle){
        return this.distances.getOrDefault(particle, new HashSet<>());
    }

}
