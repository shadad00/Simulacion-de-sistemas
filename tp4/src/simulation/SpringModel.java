package simulation;

import com.opencsv.CSVWriter;
import solvers.*;
import utils.Pair;

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
    private final IntegralSolver integralSolver;
    private final Particle particle;

    public SpringModel(final IntegralSolver integralSolver) {
        this.integralSolver = integralSolver;
        double vx_i = - SPRING_A * (SPRING_GAMMA / (2 * PARTICLE_MASS));
        this.particle = new Particle(1,
                new ParticleDynamics(Pair.of(SPRING_A, 0),
                        Pair.of(vx_i, 0),
                        Pair.of(0, 0)),
                PARTICLE_MASS,
                PARTICLE_RADIUS);
    }

    public static void main(String[] args) {
        IntegralSolver[] solvers = new IntegralSolver[]{
                new EulerSimpleSolver(),
                new VerletOriginalSolver(),
                new BeemanSolver(),
                new SpringGear5Solver()
        };

        double[] dts = new double[]{1e-2, 1e-3, 1e-4};

        for (IntegralSolver solver : solvers) {
            for (double dt : dts) {
                SpringModel springModel = new SpringModel(solver);
                springModel.run(dt);
            }
        }
    }

    public void run(double dt) {
        String path = String.format("./tp4/out/spring/csv/%s_dt%.1e.csv", integralSolver.toString(), dt);
        try (CSVWriter writer = new CSVWriter(new FileWriter(path))) {
            writer.writeNext(CSV_HEADER, false);
            double time = 0;
            int step = 0;
            while (time <= FINAL_TIME) {
                ArrayList<String> strings = new ArrayList<>();
                strings.add(String.valueOf(step));
                strings.add(String.valueOf(time));
                strings.addAll(List.of(particle.getCsvStrings()));
                String[] s = new String[strings.size()];
                writer.writeNext(strings.toArray(s), false);
                particle.evolve(integralSolver, dt);

                time += dt;
                step++;
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
