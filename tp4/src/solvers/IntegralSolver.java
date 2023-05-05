package solvers;

import simulation.ParticleDynamics;
import utils.Pair;

@FunctionalInterface
public interface IntegralSolver {
    ParticleDynamics solve(ParticleDynamics dynamics, Pair force, double mass, double dt);
}
