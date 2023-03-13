import animation.Frame;
import simulation.*;
import utils.Parser;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
//        Properties props = new Properties();
//        InputStream input = Main.class.getClassLoader().getResourceAsStream("application.properties");
//        props.load(input);
        final String staticInputPath = "res/grids/static_control_20_1.00_2.00.xyz";
        final String dynamicInputPath = "res/grids/dynamic_control_20_1.00_2.00.xyz";
        final String outPath = "src/main/java/distance.txt";

//        utils.Parser parseData = new utils.Parser(props.getProperty("static.input.path"),props.getProperty("dynamic.input.path"));

        final int[] particleIds = {0, 16, 180, 237, 330, 359};
//        final int[] particleIds = {180};
        for (int id : particleIds) {
            Parser parseData = new Parser(staticInputPath, dynamicInputPath);
            double length= parseData.getSpaceLong();
            int cellQuantity = 7;
            final double maxParticleRadius = parseData.getParticleList()
                    .stream()
                    .map(p -> p.getRadius())
                    .max(Double::compareTo)
                    .get();

            Grid grid = new PeriodicGridHalfDistance(length, cellQuantity);
            grid.placeParticles(parseData.getParticleList());
            long startTime = System.currentTimeMillis();
            grid.computeDistanceBetweenParticles();
            long endTime = System.currentTimeMillis();

            writeAnswer(outPath, parseData.getParticleList(), grid,endTime - startTime);

            final Map<Integer, Particle> particlesMap = parseData.getParticleList().stream().collect(
                    Collectors.toMap(Particle::getParticleId, x -> x));
            final Particle selectedParticle = particlesMap.get(id);
            selectedParticle.setColor(255, 255, 0);
            final Set<ParticleAndDistance> neighbors = grid.getNeighbors(selectedParticle);
            for (final ParticleAndDistance particleAndDistance : neighbors) {
                final Particle otherParticle = particleAndDistance.getOtherParticle();
                particlesMap.get(otherParticle.getParticleId()).setColor(0, 255, 0);
            }

            Frame.write(parseData.getParticleList(), String.format("frame_%s.xyz", id), 1);

            Particle.resetParticlesCreatedCounter();
        }
    }

    public static void writeAnswer(String outputPath, List<Particle> particleList, Grid grid, long timeElapsed){
        try {
            FileWriter writer = new FileWriter(outputPath);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write("Execution time(ms) : "+ timeElapsed +" \n" );
            for (Particle particle : particleList) {
                bufferedWriter.write("[ "+ particle.getParticleId() +" ");
                for (ParticleAndDistance particleAndDistance : grid.getNeighbors(particle)) {
                    // Write a string to the file
                    bufferedWriter.write(particleAndDistance.toString() + " ");
                }
                bufferedWriter.write("] \n");
            }
            // Close the writer and buffered writer
            bufferedWriter.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
