import java.security.InvalidParameterException;
import java.util.*;
import java.util.function.BiFunction;

public  class NoPeriodicGrid {
    protected final double length;
    protected final int cellQuantity;

    protected final double cellLength;
    protected final double cutoffRadius;
    protected final Cell[][] cells;

    protected final Set<DistancePair> distances ;

    public NoPeriodicGrid(double length, int cellQuantity, double cutoffRadius) {
        if((length / cellQuantity) <= cutoffRadius)
            throw new InvalidParameterException("Invalid parameters provided");
        this.length = length;
        this.cellQuantity = cellQuantity;
        this.cellLength = (length / cellQuantity);
        this.cutoffRadius = cutoffRadius;
        this.cells = new Cell[this.cellQuantity][this.cellQuantity];
        this.distances = new HashSet<>();
        for (int i = 0; i <this.cellQuantity; i++) {
            for (int j = 0; j < this.cellQuantity; j++) {
                this.cells[i][j]= new Cell(i,j);
            }
        }

    }
    
    
    public void setParticles(List<Particle> particleList){
        for (Particle particle : particleList) {
            int particleX = (int) Math.floor(particle.getX() / this.cellLength);
            int particleY = (int) Math.floor(particle.getY() / this.cellLength);
            cells[particleX][particleY].addParticle(particle);
        }
    }

    public void clearGrid(){
        for (int i = 0; i <this.cellQuantity; i++) {
            for (int j = 0; j < this.cellQuantity; j++) {
                this.cells[i][j]= new Cell(i,j);
            }
        }
    }



    public void getAdjacentCellsParticlesWithoutPeriodicity(){
        generalAdjacentCellsParticles((integer, integer2) -> checkAdjacentWithoutPeriodicity(integer,integer2));
    }

    protected void generalAdjacentCellsParticles(BiFunction<Integer, Integer,Set<Particle>> particleGenerator){
        for (int x = this.cellQuantity - 1; x >=0 ; x--)
            for (int y = 0; y < this.cellQuantity ; y++)
                if(this.cells[x][y].getParticles().size() > 0)
                    computeDistancesToAdjacentParticles(particleGenerator.apply(x,y));
    }



    protected void computeDistancesToAdjacentParticles(Set<Particle> adjacentParticlesSet){
        List<Particle> adjacentParticles = new ArrayList<>(adjacentParticlesSet);
        for (int i = 0; i < adjacentParticles.size(); i++) {
            for (int j = i+1; j <adjacentParticles.size() ; j++) {
                if(adjacentParticles.get(i).distanceTo(adjacentParticles.get(j)) <=
                    adjacentParticles.get(i).getCutOffRadius()
                ){
                    this.distances.add(
                            new DistancePair(adjacentParticles.get(i),adjacentParticles.get(j),
                                    adjacentParticles.get(i).distanceTo(adjacentParticles.get(j))));
                }
            }
        }
    }



    protected Set<Particle> checkAdjacentWithoutPeriodicity(int i , int j){
        Set<Particle> adjacentParticles = new HashSet<>();
        adjacentParticles.addAll(this.cells[i][j].getParticles());
        if((i+1) < this.cellQuantity && (j+1) < this.cellQuantity)
            adjacentParticles.addAll(this.cells[(i+1)][(j+1)].getParticles());
        if((j+1) < this.cellQuantity)
            adjacentParticles.addAll(this.cells[(i)][(j+1)].getParticles());
        if((i-1) >=0)
            adjacentParticles.addAll(this.cells[(i-1)][(j)].getParticles());
        if((j+1) < this.cellQuantity && (i-1) >=0 )
            adjacentParticles.addAll(this.cells[(i-1)][(j+1)].getParticles());
        return adjacentParticles;
    }



    public Set<DistancePair> getDistances() {
        return distances;
    }
}
