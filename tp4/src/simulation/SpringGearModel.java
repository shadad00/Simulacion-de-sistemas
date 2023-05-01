package simulation;


import com.opencsv.CSVWriter;
import solvers.SpringGear5Solver;
import utils.Pair;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SpringGearModel {
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
    protected final SpringGear5Solver solver;
    protected final Particle particle;

    public SpringGearModel() {
        this.solver = new SpringGear5Solver(SPRING_K, 5, SPRING_GAMMA);
        double rx = SPRING_A;
        double rx1 = -SPRING_A * (SPRING_GAMMA / (2 * PARTICLE_MASS));
        double rx2 = -(SPRING_K / PARTICLE_MASS) * rx;
        this.particle = new Particle(1,
                new ParticleDynamics(
                        new Pair<>(rx,0.),
                        new Pair<>(rx1, 0.),
                        new Pair<>(rx2, 0.)
                ), PARTICLE_MASS, PARTICLE_RADIUS);
        System.out.println(particle);
    }

    public static void main(String[] args) {
        double[] dts = new double[]{1e-2, 1e-3, 1e-4, 1e-5, 1e-6};

        for (double dt : dts) {
            SpringGearModel model = new SpringGearModel();
            System.out.println("Running for dt: " + dt);
            model.run(dt);
        }
    }

    public void run(final double dt) {
        String path = String.format("./tp4/out/spring/csv/%s_dt%.1e.csv", solver.toString(), dt);
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
                particle.evolve(solver, dt);

                time += dt;
                step++;
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
