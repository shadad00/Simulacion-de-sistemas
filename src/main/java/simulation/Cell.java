package simulation;

import java.util.*;

public class Cell {

    private int row ;
    private int col;
    private Set<Particle> particles ;

    public Cell(final int row, final int col) {
        this.row = row;
        this.col = col;
        this.particles = new HashSet<>();
    }

    public void addParticle(final Particle particle){
        this.particles.add(particle);
    }

    public boolean isEmpty(){
        return particles.isEmpty();
    }

    public Set<Particle> getParticles() {
        return particles;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

}
