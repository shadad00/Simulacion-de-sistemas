import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class leftRightAggregator {



    public static void main(String[] args) {
        String directory = "/home/shadad/Desktop/tp2pod/simulacion-de-sistemas/tp2/out";
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(directory))) {
            for (Path file : stream) {
                if(file.toString().contains("secondItemVaryingN") && !file.toString().contains("aggregate") && Files.isRegularFile(file)) {
                   System.out.println(file);
                    run(file.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void run(String mainCSV) {
        String leftRightCSV = mainCSV.split("\\.")[0] + "_aggregate.csv";
        final String HEADER = "iteration,left_particle,right_particle,solid_collision,particle_collision";
        try {
            Parser parser = new Parser(mainCSV);
            File file = new File(leftRightCSV);
            if (!file.exists() &&  !file.createNewFile()) {
               throw new RuntimeException("Unable to create output file. ");
            }
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(String.format("%s\n", HEADER));
            for (Grid grid : parser) {
                int left = grid.getLeftParticles();
                int right = grid.getTotalParticles() - left;
                int solidCollision = grid.getSolidCellCollisionNumber();
                int particleCollision = grid.getParticleCollisionNumber();
                bw.write(String.format("%s,%d,%d,%d,%d\n", parser.getCurrentIteration(), left, right,solidCollision,particleCollision));
            }
            bw.close();
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
