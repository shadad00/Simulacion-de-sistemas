package solvers;

import simulation.ParticleDynamics;
import utils.Pair;

import java.security.InvalidParameterException;

import static java.lang.Math.pow;

public class SpringGear5Solver implements IntegralSolver {
    private static final double[][] alphas = new double[][]{
            {0, 1, 1, -1, -1, -1},
            {1. / 6, 5. / 6, 1, 1. / 3, -1, -1},
            {19. / 90, 3. / 4, 1, 1. / 2, 1. / 12, -1},
            {3. / 16, 251. / 360, 1, 11. / 18, 1. / 6, 1. / 60}
    };
    private final double K;
    private final int order;
    private final double gamma;

    private boolean isInitialized = false;
    private double rx;
    private double rx1;
    private double rx2;
    private double rx3;
    private double rx4;
    private double rx5;

    public SpringGear5Solver(final double k, int order, final double gamma) {
        this.K = k;
        if (order < 2 || order > 5) {
            throw new InvalidParameterException("order must be between 2 and 5");
        }
        this.order = order;
        this.gamma = gamma;
    }

    public ParticleDynamics solve(final ParticleDynamics d, final Pair force, final double mass, final double dt) {
        double km = K / mass;
        if (!isInitialized) {
            rx  = d.getR().getX();
            rx1 = d.getV().getX();
            rx2 = d.getA().getX();
            rx3 = (-K * rx1 - gamma * rx2) / mass;
            rx4 = (-K * rx2 - gamma * rx3) / mass;
            rx5 = (-K * rx3 - gamma * rx4) / mass;
            isInitialized = true;
        }

        double rxp, rxp1, rxp2, rxp3, rxp4, rxp5; // predicciones
        rxp  = rx  + rx1 * dt + rx2 * (pow(dt, 2) / 2) + rx3 * (pow(dt, 3) / 6) + rx4 * (pow(dt, 4) / 24) + rx5 * (pow(dt, 5) / 120);
        rxp1 = rx1 + rx2 * dt + rx3 * (pow(dt, 2) / 2) + rx4 * (pow(dt, 3) / 6) + rx5 * (pow(dt, 4) / 24);
        rxp2 = rx2 + rx3 * dt + rx4 * (pow(dt, 2) / 2) + rx5 * (pow(dt, 3) / 6);
        rxp3 = rx3 + rx4 * dt + rx5 * (pow(dt, 2) / 2);
        rxp4 = rx4 + rx5 * dt;
        rxp5 = rx5;

        /**
         * Evaluar:
         * Evalúo la Fuerza(t+Δt) con las variables predichas y obtengo la
         * aceleración a(t+Δt), luego defino:
         * Δa = Δr2 = a(t+Δt) - ap(t+Δt) = r2(t+Δt) - r2p(t+Δt)
         */


        double drx2 = ((-K * rxp - gamma * rxp1) / mass) - rxp2;
        double R2 = drx2 * (pow(dt, 2) / 2);


        /** Corregir
         * rc =rp+α0 ΔR2
         * r1c = r1p + α1 ΔR2 / (Δt)
         */

        int idx = order - 2;
        rx  = rxp  + alphas[idx][0] * R2;
        rx1 = rxp1 + alphas[idx][1] * R2 / dt;
        rx2 = rxp2 + alphas[idx][2] * R2 * 2 / pow(dt, 2);
        rx3 = rxp3 + alphas[idx][3] * R2 * 6 / pow(dt, 3);
        rx4 = rxp4 + alphas[idx][4] * R2 * 24 / pow(dt, 4);
        rx5 = rxp5 + alphas[idx][5] * R2 * 120 / pow(dt, 5);

        return new ParticleDynamics(
                new Pair(rx, 0.),
                new Pair(rx1, 0.),
                new Pair(rx2, 0.)
        );
    }

    @Override
    public String toString() {
        return "GearPredictorOrder" + order;
    }
}
