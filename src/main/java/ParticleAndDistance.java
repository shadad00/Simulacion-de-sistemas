import java.util.Objects;

public class ParticleAndDistance {
    private final Particle otherParticle;
    private final double distance;

    public ParticleAndDistance(Particle otherParticle, double distance) {
        this.otherParticle = otherParticle;
        this.distance = distance;
    }


    public Particle getOtherParticle() {
        return otherParticle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParticleAndDistance that = (ParticleAndDistance) o;
        return Double.compare(that.distance, distance) == 0 && Objects.equals(otherParticle, that.otherParticle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(otherParticle, distance);
    }

    public double getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "{" +
                 otherParticle.getCellId() +
                ", " + distance +
                '}';
    }
}
