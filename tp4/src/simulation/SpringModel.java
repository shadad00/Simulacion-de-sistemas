package simulation;

import com.opencsv.CSVWriter;
import solvers.BeemanSolver;
import solvers.EulerSimpleSolver;
import solvers.IntegralSolver;
import solvers.VerletOriginalSolver;
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
    protected final IntegralSolver integralSolver;
    protected final Particle particle;

    public SpringModel(final IntegralSolver integralSolver) {
        this.integralSolver = integralSolver;
        double vx0 = -SPRING_A * (SPRING_GAMMA / (2 * PARTICLE_MASS));
        double ax0 = -(SPRING_K / PARTICLE_MASS) * SPRING_A - SPRING_GAMMA * vx0 / PARTICLE_MASS;
        this.particle = new Particle(1,
                new ParticleDynamics(Pair.of(SPRING_A, 0),
                        Pair.of(vx0, 0),
                        Pair.of(ax0, 0)),
                PARTICLE_MASS,
                PARTICLE_RADIUS);
    }

    public static void main(String[] args) {
        IntegralSolver[] solvers = new IntegralSolver[]{
                new EulerSimpleSolver(),
                new VerletOriginalSolver(),
                new BeemanSolver(),
        };

        double[] dts = new double[]{1e-2, 1e-3, 1e-4, 1e-5, 1e-6};
        double dt2 = 1e-2;

        for (IntegralSolver solver : solvers) {
            for (double dt : dts) {
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
            int printStep = dt2 > dt ? (int) (dt2 / dt) : 1;
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
            }
            System.out.println("dt : " + dt);
            System.out.println("LAST TIME :" + time);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
