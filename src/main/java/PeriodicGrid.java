import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PeriodicGrid extends NoPeriodicGrid{
    public PeriodicGrid(double length, int cellQuantity, double cutoffRadius) {
        //two extra
        super(length, cellQuantity + 2 , cutoffRadius);
    }

    @Override
    public void setParticles(List<Particle> particleList) {
        for (Particle particle : particleList) {
            int particleX = (int) Math.floor(particle.getX() / this.cellLength);
            int particleY = (int) Math.floor(particle.getY() / this.cellLength);
            cells[particleX + 1][particleY + 1].addParticle(particle);
        }
        //fill zero row
        for (int i = 0; i < this.cellQuantity; i++) {
//            cells[0][i] = cells[this.cellQuantity-2][i].getParticles().stream().map((Function<Particle, Particle>) particle -> new Particle(particle.getCellId(),particle.getX(), particle.getY()-length,particle.getRadius(),particle.getCutOffRadius())).collect(Collectors.t());
        }

    }

    public void getAdjacentCellsParticlesWithPeriodicity(){
        generalAdjacentCellsParticles((integer, integer2) -> checkAdjacentWithPeriodicity(integer,integer2));
    }

    private Set<Particle> checkAdjacentWithPeriodicity(int i , int j){
        Set<Particle> adjacentParticles = new HashSet<>();
        adjacentParticles.addAll(this.cells[i][j].getParticles());
        adjacentParticles.addAll(this.cells[(i+1)%this.cellQuantity][(j+1)%this.cellQuantity].getParticles());
        adjacentParticles.addAll(this.cells[(i)%this.cellQuantity][(j+1)%this.cellQuantity].getParticles());
        if((i-1)>=0){
            adjacentParticles.addAll(this.cells[(i-1)%this.cellQuantity][(j)%this.cellQuantity].getParticles());
            adjacentParticles.addAll(this.cells[(i-1)%this.cellQuantity][(j+1)%this.cellQuantity].getParticles());
        }else {
            adjacentParticles.addAll(this.cells[this.cellQuantity-1][(j)%this.cellQuantity].getParticles());
            adjacentParticles.addAll(this.cells[this.cellQuantity-1][(j+1)%this.cellQuantity].getParticles());
        }
        return adjacentParticles;
    }


}
