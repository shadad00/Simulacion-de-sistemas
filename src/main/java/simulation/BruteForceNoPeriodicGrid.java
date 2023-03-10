package simulation;

import utils.BenchmarkGenerator;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BruteForceNoPeriodicGrid extends Grid{
    public BruteForceNoPeriodicGrid(double length, double cutoffRadius) {
        super(length, 1, cutoffRadius);
    }

    @Override
    protected void placeParticle(Particle particle) {
        this.cells[0][0].addParticle(particle);
    }

    @Override
    protected double distanceBetweenParticles(Particle particle1, Particle particle2) {
        if (Math.sqrt(Math.pow(particle1.getX() - particle2.getX(), 2) + Math.pow(particle1.getY() - particle2.getY(), 2)) - (particle1.getRadius() + particle2.getRadius()) < 0)
            return 0;
        return Math.sqrt(Math.pow(particle1.getX() - particle2.getX(), 2) + Math.pow(particle1.getY() - particle2.getY(), 2)) - (particle1.getRadius() + particle2.getRadius());
    }

    @Override
    public boolean isPeriodic() {
        return false;
    }

    @Override
    protected Set<Cell> getCandidateCells(int xCell, int yCell) {
        Set<Cell> s = new HashSet<>();
        s.add(this.cells[0][0]);
        return s;
    }

    public static void main(String[] args) {
        double gridSide = 20;
        int cellQuantity = 4;
        double radius = 0.25;
        double cutoff = 4;
        int particleQuantity = 1000;

        BenchmarkGenerator bg = new BenchmarkGenerator(1);
        Set<Particle> particles = bg.particleGenerator(particleQuantity, gridSide, radius, cutoff);

        BruteForcePeriodicGrid bf = new BruteForcePeriodicGrid(gridSide, cutoff);
        PeriodicGridHalfDistance grid = new PeriodicGridHalfDistance(gridSide, cellQuantity, cutoff);

        grid.placeParticles(particles);
        grid.computeDistanceBetweenParticles();
        Map<Particle, Set<ParticleAndDistance>> gridMap = grid.distances;

        bf.placeParticles(particles);
        bf.computeDistanceBetweenParticles();
        Map<Particle, Set<ParticleAndDistance>> bfMap = bf.distances;

        System.out.println("bfmap");
        bfMap.forEach((k, v) -> System.out.println(k + " " + v));
        System.out.println("gridmap");
        gridMap.forEach((k, v) -> System.out.println(k + " " + v));

        System.out.println("son iguales: " + gridMap.equals(bfMap));

    }
}
