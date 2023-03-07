import java.util.Objects;

public class DistancePair {
    private final Particle firstParticle;
    private final Particle secondParticle;
    private final double distance;

    public DistancePair(Particle firstParticle, Particle secondParticle, double distance) {
        this.firstParticle = firstParticle;
        this.secondParticle = secondParticle;
        this.distance = distance;
    }

    public Particle getFirstParticle() {
        return firstParticle;
    }

    public Particle getSecondParticle() {
        return secondParticle;
    }

    public double getDistance() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DistancePair that = (DistancePair) o;
        return  (Objects.equals(firstParticle, that.firstParticle) && Objects.equals(secondParticle, that.secondParticle)) ||
                (Objects.equals(firstParticle, that.secondParticle) && Objects.equals(secondParticle, that.firstParticle));
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstParticle, secondParticle, distance);
    }

    @Override
    public String toString() {
        return "DistancePair{" +
                "firstParticle=" + firstParticle +
                ", secondParticle=" + secondParticle +
                ", distance=" + distance +
                '}';
    }
}
