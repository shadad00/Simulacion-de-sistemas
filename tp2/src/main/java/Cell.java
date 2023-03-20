import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class Cell {
    protected final Set<Particle> particleSet;

    public Cell() {
        this.particleSet = new HashSet<>();
    }

    protected Cell(Set<Particle> particles) {
        this.particleSet = particles;
    }

    public void addParticle(Particle particle){
        this.particleSet.add(particle);
    }

    public void removeParticle(Particle particle){
        this.particleSet.remove(particle);
    }

    public abstract Cell collide();

    public int particleQuantity(){return this.particleSet.size();}

    public boolean isEmpty() {
        return particleSet.isEmpty();
    }

    protected Set<Particle> rotateParticles(int step){
      return this.particleSet.stream().
              map(particle -> new Particle(particle.getVelocity().rotateClockwise(step)))
              .collect(Collectors.toSet()) ;
    }

}
