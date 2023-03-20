import java.io.IOException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class Main {

    public static void main(String[] args) throws IOException{
        Main.run(10000,1000);
    }

    public static void run(int maxIter, int nParticles) throws IOException {
        Grid currentGrid = new Grid(nParticles, 50);

        File csv = new File("output.csv");
        if (!csv.exists()) {
            csv.createNewFile();
        }
        BufferedWriter bf = new BufferedWriter(new FileWriter(csv));
        String header = "time,left,right\n";
        bf.write(header);
        String line= String.format("%s,%s,%s\n", 0, currentGrid.getLeftParticles(), currentGrid.getRightParticles());
        bf.write(line);
        for (int i = 1; i < maxIter ; i++) {
            Grid nextGrid = currentGrid.getNextGrid();
            line = String.format("%s,%s,%s\n", i, currentGrid.getLeftParticles(), currentGrid.getRightParticles());
            bf.write(line);
            currentGrid = nextGrid;
        }
        bf.close();
    }

}
