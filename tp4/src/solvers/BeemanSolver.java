package solvers;

import simulation.ParticleDynamics;
import utils.Pair;

public class BeemanSolver implements IntegralSolver {
    @Override
    public ParticleDynamics solve(final ParticleDynamics dynamics, final Pair<Double> force, final double mass, final double dt) {
        double rx = dynamics.getR().getX();
        double vx = dynamics.getV().getX();
        double ax = dynamics.getA().getX();
        double fx = force.getX();

        double ry = dynamics.getR().getY();
        double vy = dynamics.getV().getY();
        double ay = dynamics.getA().getY();
        double fy = force.getY();

        ParticleDynamics antes = new EulerSimpleSolver().solve(dynamics, force, mass, -dt);
        ParticleDynamics despues = new EulerSimpleSolver().solve(dynamics, force, mass, dt);

        double dax = despues.getA().getX();

        double Rx = rx + vx * dt + (2. / 3) * ax * Math.pow(dt, 2) - (1. / 6) * (antes.getA().getX()) * Math.pow(dt, 2);
        double Vx = vx + (((1. / 3) * dax) + ((5. / 6) * ax) - ((1. / 6) * antes.getA().getX())) * dt;
        double Ax = fx / mass;

        double day = despues.getA().getY();


        double Ry = ry + vy * dt + (2. / 3) * ay * Math.pow(dt, 2) - (1. / 6) * (antes.getA().getY()) * Math.pow(dt, 2);
        double Vy = vy + (((1. / 3) * day) + ((5. / 6) * ay) - ((1. / 6) * antes.getA().getY())) * dt;
        double Ay = fy / mass;

        return new ParticleDynamics(
                Pair.of(Rx, Ry),
                Pair.of(Vx, Vy),
                Pair.of(Ax, Ay)
        );
    }

    @Override
    public String toString() {
        return "BeemanSolver";
    }
}
