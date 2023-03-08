import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        double length= 20;
        int cellQuantity = 5;
        double cutOffRadius = 1.5;
        List<Particle> particleList = new ArrayList<>();
        particleList.add(new Particle(0,0,0,2));
        particleList.add(new Particle(0,19.0,0,2));
        particleList.add(new Particle(17.0,17.0,0,2));
        PeriodicGrid grid = new PeriodicGrid(length,cellQuantity,cutOffRadius);
        grid.setParticles(particleList);
        grid.computeDistanceBetweenParticles();
        grid.getDistances().forEach(System.out::println);


    }
}
