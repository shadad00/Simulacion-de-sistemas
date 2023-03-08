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
        int cellQuantity = 1;
        double cutOffRadius = 1.5;
        NoPeriodicGrid grid = new NoPeriodicGrid(length,cellQuantity,cutOffRadius);
        grid.setParticles(parseData.getParticleList());
        long startTime = System.currentTimeMillis();
        grid.computeDistanceBetweenParticles();
        long endTime = System.currentTimeMillis();
        writeAnswer(props.getProperty("output.path"), parseData.getParticleList(),grid,endTime - startTime);
    }


    public static void writeAnswer(String outputPath, List<Particle> particleList, NoPeriodicGrid grid, long timeElapsed){
        try {
            FileWriter writer = new FileWriter(outputPath);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write("Execution time(ms) : "+ timeElapsed +" \n" );
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
