import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws IOException {
        Properties props = new Properties();
        InputStream input = Main.class.getClassLoader().getResourceAsStream("application.properties");
        props.load(input);
        Parser parseData = new Parser(props.getProperty("static.input.path"),props.getProperty("dynamic.input.path"));

        double length= parseData.getSpaceLong();
        int cellQuantity = 5;
        double cutOffRadius = 1.5;
        PeriodicGrid grid = new PeriodicGrid(length,cellQuantity,cutOffRadius);
        grid.setParticles(parseData.getParticleList());
        grid.computeDistanceBetweenParticles();
        writeAnswer(props.getProperty("output.path"), parseData.getParticleList(),grid);
    }


    public static void writeAnswer(String outputPath, List<Particle> particleList, PeriodicGrid grid){
        try {
            FileWriter writer = new FileWriter(outputPath);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);

            for (Particle particle : particleList) {
                bufferedWriter.write("[ "+ particle.getCellId()+" ");
                for (ParticleAndDistance particleAndDistance : grid.getDistance(particle)) {
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
