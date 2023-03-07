import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Grid {
    private double length;
    private int cellQuantity;
    
    private double cellLength; 
    private double cutoffRadius;
    private Cell[][] cells;

    private HashMap<> neighbours ;

    public Grid(double length, int cellQuantity, double cutoffRadius) {
        if((length / cellQuantity) <= cutoffRadius)
            throw new InvalidParameterException("Invalid parameters provided");
        this.length = length;
        this.cellQuantity = cellQuantity;
        this.cellLength = (length / cellQuantity);
        this.cutoffRadius = cutoffRadius;
        this.cells = new Cell[this.cellQuantity][this.cellQuantity];
        this.neighbours = new HashMap<>();
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


    public void getAdjacentCellsParticles(){
        for (int x = this.cellQuantity - 1; x >=0 ; x--) {
            for (int y = 0; y < this.cellQuantity ; y++) {
                Set<Particle> candidates = new HashSet<>();
                candidates.addAll(this.cells[x][y].getParticles());
                candidates.addAll(this.cells[(x+1)%this.cellQuantity][(y+1)%this.cellQuantity].getParticles());
                candidates.addAll(this.cells[(x)%this.cellQuantity][(y+1)%this.cellQuantity].getParticles());
                candidates.addAll(this.cells[(x-1)%this.cellQuantity][(y)%this.cellQuantity].getParticles());
                candidates.addAll(this.cells[(x-1)%this.cellQuantity][(y+1)%this.cellQuantity].getParticles());
            }
        }
    }

    public void computeDistances(Set<Particle> adjacentParticles){
        //todo: change set into list ?
        for (Particle pivotParticle : adjacentParticles) {
            for (Particle iteratingParticle : adjacentParticles) {
                if(!pivotParticle.equals(iteratingParticle) &&
                        pivotParticle.distanceTo(iteratingParticle) <= cutoffRadius){
                        //add distance.
                }
            }
        }

    }



}
