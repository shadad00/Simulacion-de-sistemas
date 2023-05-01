package solvers;

import simulation.ParticleDynamics;
import utils.Pair;

public class EulerSimpleSolver implements IntegralSolver {
    @Override
    public ParticleDynamics solve(final ParticleDynamics dynamics, final Pair<Double> force, final double mass, final double dt) {
        double rx = dynamics.getR().getX();
        double vx = dynamics.getV().getX();
        double fx = force.getX();

        double Rx = rx + dt * vx + (Math.pow(dt, 2) / (2 * mass)) * fx;
        double Vx = vx + (fx / mass) * dt;
        double Ax = fx / mass;

        double ry = dynamics.getR().getY();
        double vy = dynamics.getV().getY();
        double fy = force.getY();

        double Ry = ry + dt * vy + (Math.pow(dt, 2) / (2 * mass)) * fy;
        double Vy = vy + (fy / mass) * dt;
        double Ay = fy / mass;

        return new ParticleDynamics(Pair.of(Rx, Ry),
                Pair.of(Vx, Vy),
                Pair.of(Ax, Ay));
    }

    @Override
    public String toString() {
        return "EulerSimpleSolver";
    }
}
