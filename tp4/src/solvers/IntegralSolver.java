package solvers;

import simulation.ParticleDynamics;
import utils.Pair;

@FunctionalInterface
public interface IntegralSolver {
    ParticleDynamics solve(ParticleDynamics dynamics, Pair<Double> force, double mass, double dt);
}
