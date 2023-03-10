package simulation;

import java.util.HashSet;
import java.util.Set;

public class BruteForcePeriodicGrid extends Grid{
    public BruteForcePeriodicGrid(double length, double cutoffRadius) {
        super(length, 1, cutoffRadius);
    }

    @Override
    public boolean isPeriodic() {
        return true;
    }

    @Override
    protected void placeParticle(Particle particle) {
        this.cells[0][0].addParticle(particle);
    }

    @Override
    protected double distanceBetweenParticles(Particle particle1, Particle particle2) {
        double diffX = Math.abs(particle1.getX() - particle2.getX());
        double diffY = Math.abs(particle1.getY() - particle2.getY());

        if (diffX > this.length / 2) {
            diffX = length - diffX;
        }
        if (diffY > this.length / 2) {
            diffY = length - diffY;
        }

        double v = Math.pow(diffX, 2) + Math.pow(diffY, 2);

        if (Math.sqrt(v) - (particle1.getRadius() + particle2.getRadius()) < 0)
            return 0;

        return Math.sqrt(v) - (particle1.getRadius() + particle2.getRadius());
    }

    @Override
    protected Set<Cell> getCandidateCells(int xCell, int yCell) {
        Set<Cell> s = new HashSet<>();
        s.add(this.cells[0][0]);
        return s;
    }
}
