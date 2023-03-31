import java.io.IOException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException{
        Main.run(3000,5000);
    }

    public static void run(int maxIter, int nParticles) throws IOException {
        Grid currentGrid = new Grid(nParticles, 200,0.05);

        File csv = new File("changing3ParticlesRule_d2000_n5000_i0.csv");
        if (!csv.exists() && !csv.createNewFile()) {
            throw new IOException("Unable to create output.csv");
        }


        BufferedWriter bf = new BufferedWriter(new FileWriter(csv));
        String header = "iteration, row, col, up_right, right, down_right, down_left, left, up_left\n";
        final String FORMAT = "%s,%s,%s,%s\n";
        bf.write(header);
        Cell[][] cells;
        for (int i = 0; i < maxIter /*&& !currentGrid.isEquilibrated()*/ ; i++) {
            cells = currentGrid.getCells();
            for (int j = 0; j < cells.length; j++) {
                for (int k = 0; k < cells.length; k++) {
                    Cell currentCell = cells[j][k];
                    if (!currentCell.isEmpty()){
                        Set<Particle> particles = currentCell.getParticles();
                        StringBuilder value = new StringBuilder();
                        Set<Velocity> velocitySet = particles.stream().map(Particle::getVelocity).collect(Collectors.toSet());
                        for (Velocity velocity : Velocity.values()) {
                            if(velocitySet.contains(velocity))
                                value.append("1");
                            else
                                value.append("0");
                            if (!velocity.equals(Velocity.UP_LEFT))
                                value.append(",");
                            }

                        String result = new String(value);
                        bf.write(String.format(FORMAT, i, j, k, result));
                    }
                }
            }
            int leftParticles = currentGrid.getLeftParticles();
            System.out.println("i " + i + " | left = " + leftParticles + " | right = " + (nParticles-leftParticles));
            currentGrid = currentGrid.getNextGrid();
        }
        bf.close();
    }

}



