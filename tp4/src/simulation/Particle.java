package simulation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import solvers.IntegralSolver;
import utils.Pair;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
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
                getFullPrecision(this.dynamics.getR().getX()),
                getFullPrecision(this.dynamics.getR().getY()),
                getFullPrecision(this.dynamics.getV().getX()),
                getFullPrecision(this.dynamics.getV().getY()),
                getFullPrecision(this.dynamics.getA().getX()),
                getFullPrecision(this.dynamics.getA().getY()),
                getFullPrecision(this.getMass()),
                getFullPrecision(this.getRadius()),
        };
    }

    private String getFullPrecision(double num) {
        DecimalFormat df = new DecimalFormat("#.################################");
//        System.out.printf("Formatteando num = %f%n", num);
//        BigDecimal bd = new BigDecimal(num);
//        bd = bd.divide(BigDecimal.valueOf(Math.pow(10, 10)));
        return df.format(num);
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
