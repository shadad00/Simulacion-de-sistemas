import java.util.Objects;

public class Particle {
    private static int id = 0;
    private final int particleId;
    private final Velocity velocity;

    public Particle(Velocity velocity) {
        this.velocity = velocity;
        this.particleId = id++;
    }

    public Velocity getVelocity() {
        return velocity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Particle particle = (Particle) o;
        return particleId == particle.particleId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(particleId);
    }
}
