import java.util.Collection;
import java.util.HashSet;
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


    public Set<Particle> getParticles() {
        return particleSet;
    }

    public abstract boolean isSolid();

    public boolean isEmpty() {
        return particleSet.isEmpty();
    }

    public abstract Cell collide();

    public int particleQuantity(){return this.particleSet.size();}

    protected Set<Particle> rotateParticles(int step){
        return this.particleSet.stream().
              map(particle -> new Particle(particle.getVelocity().rotateClockwise(step)))
              .collect(Collectors.toSet()) ;
    }

    public void addAllParticles(Collection<Particle> particles) {
        this.particleSet.addAll(particles);
    }

    public Set<Velocity> getVelocities() {
        return this.particleSet.stream().map(Particle::getVelocity).collect(Collectors.toSet());
    }
}
