package simulation;

import com.opencsv.CSVWriter;
import solvers.*;
import utils.Pair;

import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SpringModel {
    public static final double SPRING_K = Math.pow(10, 4);     // [N/m]
    public static final int SPRING_A = 100;
    public static final double SPRING_GAMMA = 100;             // [kg/s
    public static final int PARTICLE_MASS = 70;
    public static final int PARTICLE_RADIUS = 1;
    public static final String[] CSV_HEADER = {
            "step", "time",
            "id", "rx", "ry", "vx", "vy",
            "ax", "ay", "mass", "radius"
    };
    private static final double FINAL_TIME = 5;                // [s]
    protected final IntegralSolver integralSolver;
    protected final Particle particle;

    public SpringModel(final IntegralSolver integralSolver) {
        this.integralSolver = integralSolver;
        double rx = SPRING_A;
        double rx1 = -SPRING_A * (SPRING_GAMMA / (2 * PARTICLE_MASS));
        double rx2 = -(SPRING_K / PARTICLE_MASS) * rx;
        this.particle = new Particle(1,
                new ParticleDynamics(Pair.of(rx, 0),
                        Pair.of(rx1, 0),
                        Pair.of(rx2, 0)),
                PARTICLE_MASS,
                PARTICLE_RADIUS);
    }

    public static void main(String[] args) {

        double[] dts = new double[]{1e-2, 1e-3, 1e-4, 1e-5, 1e-6};
        double dt2 = 1e-3;

        for (double dt : dts) {
            IntegralSolver[] solvers = new IntegralSolver[]{
                    new EulerSimpleSolver(),
                    new VerletOriginalSolver(),
                    new BeemanSolver(SPRING_K, SPRING_GAMMA),
                    new SpringGear5Solver(SPRING_K, 5, SPRING_GAMMA)
            };
            for (IntegralSolver solver : solvers) {
                SpringModel springModel = new SpringModel(solver);
                springModel.run(dt, dt2);
            }
        }
    }

    public void run(double dt, double dt2) {
        String path = String.format("./tp4/out/spring/csv/%s_dt%.1e.csv", integralSolver.toString(), dt);
        try (CSVWriter writer = new CSVWriter(new FileWriter(path))) {
            writer.writeNext(CSV_HEADER, false);
            double time = 0;
            int step = 0;
            int printStep = dt2 > dt ? (int) Math.ceil(dt2 / dt) : 1;
            while (time <= FINAL_TIME) {
                if (step % printStep == 0) {
                    ArrayList<String> strings = new ArrayList<>();
                    strings.add(String.valueOf(step));
                    strings.add(String.valueOf(time));
                    strings.addAll(List.of(particle.getCsvStrings()));
                    String[] s = new String[strings.size()];
                    writer.writeNext(strings.toArray(s), false);
                }
                particle.evolve(integralSolver, dt);
                time += dt;
                step++;
//                k++;
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
