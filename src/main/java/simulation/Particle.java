package simulation;

import java.awt.*;
import java.util.Objects;

public class Particle {

    private static int particlesCreated = 0;

    private int particleId;
    private double x;
    private double y;
    private final double radius;
    private final double cutOffRadius;
    private Color color;

    public Particle(double radius, double cutOffRadius){
        this(0, 0 , radius, cutOffRadius);
    }

    public Particle( double x, double y, double radius, double cutOffRadius) {
        this(particlesCreated++, x, y, radius, cutOffRadius);
    }
    private Particle( int particleId, double x, double y, double radius, double cutOffRadius) {
        this.particleId = particleId;
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.cutOffRadius = cutOffRadius;
        this.color = new Color(255, 0, 0);
    }

    public Particle getVirtualParticle(){
        return new Particle(this.getParticleId(),this.getX(), this.getY(),this.getRadius(),this.getCutOffRadius());
    }

    public int getParticleId() {
        return particleId;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getRadius() {
        return radius;
    }

    public Color getColor() {
        return color;
    }

    public double getCutOffRadius() {
        return cutOffRadius;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Particle other = (Particle) o;
        return other.getParticleId() == this.particleId;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
//        return Objects.hash(particleId);
    }

    @Override
    public String toString() {
        return "simulation.Particle{" +
                "  id=" + particleId +
                ", x=" + x +
                ", y=" + y +
                '}';
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setColor(final int r, final int g, final int b) {
        this.color = new Color(r, g, b);
    }

    public static void resetParticlesCreatedCounter() {
        particlesCreated = 0;
    }
}
