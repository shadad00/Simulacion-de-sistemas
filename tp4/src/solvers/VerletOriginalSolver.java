package solvers;

import simulation.ParticleDynamics;
import utils.Pair;

public class VerletOriginalSolver implements IntegralSolver {

    private ParticleDynamics last = null;
    @Override
    public ParticleDynamics solve(final ParticleDynamics dynamics, final Pair force, final double mass, final double dt) {
        EulerSimpleSolver euler = new EulerSimpleSolver();


        double rx = dynamics.getR().getX();
        double vx = dynamics.getV().getX();
        double ax = dynamics.getA().getX();
        double fx = force.getX();


        double ry = dynamics.getR().getY();
        double vy = dynamics.getV().getY();
        double ay = dynamics.getA().getY();
        double fy = force.getY();

        ParticleDynamics minusDelta = (last == null) ? euler.solve(dynamics, force, mass, -dt) : last;
        double arx = minusDelta.getR().getX();
        double ary = minusDelta.getR().getY();

        double Rx = 2 * rx - arx + (Math.pow(dt, 2) / mass) * fx;
        double Vx = (Rx - arx) / (2 * dt);
        double Ax = fx / mass;

        double Ry = 2 * ry - ary + (Math.pow(dt, 2) / mass) * fy;
        double Vy = (Ry - ary) / (2 * dt);
        double Ay = fy / mass;


        this.last = dynamics;
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
