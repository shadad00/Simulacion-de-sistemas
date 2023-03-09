import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
//        Properties props = new Properties();
//        InputStream input = Main.class.getClassLoader().getResourceAsStream("application.properties");
//        props.load(input);
        final String staticInputPath = "src/main/java/Static100.txt";
        final String dynamicInputPath = "src/main/java/Dynamic100.txt";
        final String outPath = "src/main/java/distance.txt";

//        Parser parseData = new Parser(props.getProperty("static.input.path"),props.getProperty("dynamic.input.path"));
        Parser parseData = new Parser(staticInputPath, dynamicInputPath);

        double length= parseData.getSpaceLong();
        int cellQuantity = 2;
        double cutOffRadius = 15;
        Grid grid = new PeriodicGrid(length, cellQuantity, cutOffRadius);
        grid.setParticles(parseData.getParticleList());
        long startTime = System.currentTimeMillis();
        grid.computeDistanceBetweenParticles();
        long endTime = System.currentTimeMillis();
        writeAnswer(outPath, parseData.getParticleList(), grid,endTime - startTime);

        // Dibujando el resultado para que ovito lo pueda mostrar con colores
        final Map<Integer, Particle> particlesMap = parseData.getParticleList().stream().collect(
                Collectors.toMap(Particle::getParticleId, x -> x));
        final Particle selectedParticle = particlesMap.get(65);
        selectedParticle.setColor(255, 255, 0);
        final Set<ParticleAndDistance> neighbors = grid.getDistanceSet(selectedParticle);
        for (ParticleAndDistance particleAndDistance : neighbors) {
            particleAndDistance.getOtherParticle().setColor(0, 255, 0);
        }

        Frame.write(parseData.getParticleList(), "frame.xyz", 1);
    }


    public static void writeAnswer(String outputPath, List<Particle> particleList, Grid grid, long timeElapsed){
        try {
            FileWriter writer = new FileWriter(outputPath);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write("Execution time(ms) : "+ timeElapsed +" \n" );
            for (Particle particle : particleList) {
                bufferedWriter.write("[ "+ particle.getParticleId()+" ");
                for (ParticleAndDistance particleAndDistance : grid.getDistanceSet(particle)) {
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
