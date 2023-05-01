package solvers;

import simulation.Particle;
import simulation.ParticleDynamics;
import utils.Pair;

public class BeemanSolver implements IntegralSolver {
    private ParticleDynamics last = null;
    private final double gamma;
    private final double k;

    public BeemanSolver(double k, double gamma) {
        this.gamma = gamma;
        this.k = k;
    }

    @Override
    public ParticleDynamics solve(final ParticleDynamics dynamics, final Pair<Double> force, final double mass, final double dt) {
        double km = k / mass;
        double rx = dynamics.getR().getX();
        double vx = dynamics.getV().getX();
        double ax = dynamics.getA().getX();

        double ry = dynamics.getR().getY();
        double vy = dynamics.getV().getY();
        double ay = dynamics.getA().getY();

        ParticleDynamics antes = (last == null) ? new EulerSimpleSolver().solve(dynamics, force, mass, -dt) : last;

        double Rx = rx + vx * dt + (2. / 3) * ax * Math.pow(dt, 2) - (1. / 6) * (antes.getA().getX()) * Math.pow(dt, 2);
        double Vxp = vx + (3 / 2.) * ax * dt - (1/2.)* antes.getA().getX() * dt;
        double Ax = (-km * Rx -  (gamma/mass) * Vxp);
        double Vxc = vx + (1/3.)* Ax * dt + (5/6.) * ax * dt - (1/6.) * (antes.getA().getX()) * dt;


        double Ry = ry + vy * dt + (2. / 3) * ay * Math.pow(dt, 2) - (1. / 6) * (antes.getA().getY()) * Math.pow(dt, 2);
        double Vyp = vy + (3 / 2.) * ay * dt - (1/2.)* antes.getA().getY() * dt;
        double Ay = (-km * Ry -  (gamma/mass) * Vyp);
        double Vyc = vy + (1/3.)* Ay * dt + (5/6.) * ay * dt - (1/6.) * (antes.getA().getY()) * dt;

        this.last = dynamics;
        return new ParticleDynamics(
                Pair.of(Rx, Ry),
                Pair.of(Vxc, Vyc),
                Pair.of(Ax, Ay)
        );
    }

    @Override
    public String toString() {
        return "BeemanSolver";
    }
}
