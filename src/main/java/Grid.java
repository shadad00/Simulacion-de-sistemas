import java.util.List;
import java.util.Set;

public interface Grid {
    void setParticles(List<Particle> particleList);
    void clearParticles();
    void computeDistanceBetweenParticles();
    Set<ParticleAndDistance> getDistanceSet(Particle particle);
}
