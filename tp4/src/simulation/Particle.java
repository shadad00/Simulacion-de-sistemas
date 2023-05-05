package simulation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import solvers.IntegralSolver;
import utils.Pair;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

@Getter
@AllArgsConstructor
public class Particle {


    public static final double GAMMA = 100; // ks / s
    public static final double K = Math.pow(10, 4); // N / m
    private final long id;
    private final double mass;
    private final double radius;
    private ParticleDynamics dynamics;
    private Function<Particle, Double> forceFunction;
    private Set<Particle> neighbours;

    public Particle(final long id, final ParticleDynamics dynamics, final double mass, final double radius) {
        this.id = id;
        this.dynamics = dynamics;
        this.mass = mass;
        this.radius = radius;
        this.neighbours = new HashSet<>();
    }

    private Pair getForce() {
        double fy = 0;
        double fx = -((K * dynamics.getR().getX()) + (GAMMA * dynamics.getV().getX()));

        return Pair.of(fx, fy);
    }

    public void evolve(IntegralSolver solver, double deltaTime) {
        this.dynamics = solver.solve(this.dynamics, getForce(), this.mass, deltaTime);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Particle particle = (Particle) o;
        return id == particle.id;
    }

    @Override
    public String toString() {
        return String.format("r: %s | v: %s | a: %s",
                dynamics.getR(),
                dynamics.getV(),
                dynamics.getA());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String[] getCsvStrings() {
        return new String[]{
                String.valueOf(this.id),
                String.valueOf(this.dynamics.getR().getX()),
                String.valueOf(this.dynamics.getR().getY()),
                String.valueOf(this.dynamics.getV().getX()),
                String.valueOf(this.dynamics.getV().getY()),
                String.valueOf(this.dynamics.getA().getX()),
                String.valueOf(this.dynamics.getA().getY()),
                String.valueOf(this.getMass()),
                String.valueOf(this.getRadius()),
        };
    }

    public String getCsv() {
        return String.format("%d,%f,%f,%f,%f,%f,%f,%f,%f",
                id,
                dynamics.getR().getX(),
                dynamics.getR().getY(),
                dynamics.getV().getX(),
                dynamics.getV().getY(),
                dynamics.getA().getX(),
                dynamics.getA().getY(),
                mass,
                radius);
    }
}
