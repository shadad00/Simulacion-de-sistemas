package simulation;

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
        if( !this.otherParticle.equals(that.otherParticle))
            return true;
        return  Double.compare(that.distance, distance) <= 0  ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.otherParticle);
    }

    public double getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "{" +
                 otherParticle.getParticleId() +
                ", " + distance +
                '}';
    }
}
