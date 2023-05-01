package simulation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import solvers.SpringGear5Solver;

@AllArgsConstructor
@Getter
@ToString
public class Particle5 {
    public static final double GAMMA = 100;
    private static final double K = Math.pow(10, 4);
    private int id;
    private ParticleDynamics5 dynamics;
    private double mass;
    private double radius;

    private double getForce() {
        return -((K * dynamics.rx) + (GAMMA * dynamics.rx1));
    }

    public void evolve(SpringGear5Solver solver, double deltaTime) {
        this.dynamics = solver.solve(this.dynamics, getForce(), this.mass, deltaTime);
    }


    public String[] getCsvStrings() {
        return new String[]{
                String.valueOf(this.id),
                String.valueOf(this.dynamics.rx),
                String.valueOf(0),
                String.valueOf(this.dynamics.rx1),
                String.valueOf(0),
                String.valueOf(this.dynamics.rx2),
                String.valueOf(0),
                String.valueOf(this.getMass()),
                String.valueOf(this.getRadius()),
        };
    }

}
