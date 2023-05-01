package solvers;

import simulation.ParticleDynamics;
import utils.Pair;

public class VerletOriginalSolver implements IntegralSolver {
    @Override
    public ParticleDynamics solve(final ParticleDynamics dynamics, final Pair<Double> force, final double mass, final double dt) {
        EulerSimpleSolver euler = new EulerSimpleSolver();


        double rx = dynamics.getR().getX();
        double vx = dynamics.getV().getX();
        double ax = dynamics.getA().getX();
        double fx = force.getX();


        double ay = dynamics.getA().getY();
        double ry = dynamics.getR().getY();
        double vy = dynamics.getV().getY();
        double fy = force.getY();

        ParticleDynamics minusDelta = euler.solve(dynamics, force, mass, -dt);
        double arx = minusDelta.getR().getX();
        double ary = minusDelta.getR().getY();

        double Rx = 2 * rx - arx + (Math.pow(dt, 2) / mass) * fx;
        double Vx = vx + ax * dt;
        double Ax = fx / mass;

        double Ry = 2 * ry - ary + (Math.pow(dt, 2) / mass) * fy;
        double Vy = vy + ay * dt;
        double Ay = fy / mass;



        return new ParticleDynamics(
                Pair.of(Rx, Ry),
                Pair.of(Vx, Vy),
                Pair.of(Ax, Ay)
        );
    }

    @Override
    public String toString() {
        return "VerletOriginalSolver";
    }
}
