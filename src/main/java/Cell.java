import java.util.*;

public class Cell {

    private int row ;
    private int col;
    private List<Particle> particles ;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.particles = new ArrayList<>();
    }

    public void addParticle(Particle particle){
        this.particles.add(particle);
    }

    public boolean isEmpty(){
        return particles.isEmpty();
    }

    public List<Particle> getParticles() {
        return particles;
    }


}
