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
        String directory = "/Users/benjamacbook/OneDrive/ITBA/2023_1C/ss/Simulacion-de-sistemas/tp2/out";
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(directory))) {
            for (Path file : stream) {
                if(!file.toString().contains("aggregate") && Files.isRegularFile(file)) {
                   System.out.println(file.toString());
                    run(file.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void run(String mainCSV) {
        String leftRightCSV = mainCSV.split("\\.")[0] + "_aggregate.csv";
        final String HEADER = "iteration,left_particle,right_particle";
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
                bw.write(String.format("%s,%d,%d\n", parser.getCurrentIteration(), left, right));
            }
            bw.close();
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
