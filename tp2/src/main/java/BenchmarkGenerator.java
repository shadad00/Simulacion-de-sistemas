import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

public class BenchmarkGenerator {

    private final static int[] particlesArray = {2000,3000,5000};
    private final static int[] gapSizes = {50};
    private final static int N_ITERATIONS = 10;
    private final static double EPSILON = 0.04;
    private final static int MAX_ITER = 10000;

    public static void main(String[] args) throws IOException{
        for(int gapSize : BenchmarkGenerator.gapSizes) {
                BenchmarkGenerator.runAll(gapSize, N_ITERATIONS);
        }
    }

    public static void run(String fileName, int cellGap, int nParticles) throws IOException {
        Grid currentGrid = new Grid(nParticles, cellGap,EPSILON);

        File csv = new File(fileName);
        if (!csv.exists() && !csv.createNewFile()) {
            throw new IOException("Unable to create simulation's output file");
        }


        BufferedWriter bf = new BufferedWriter(new FileWriter(csv));
        String header = "iteration,row,col,up_right,right,down_right,down_left,left,up_left\n";
        final String FORMAT = "%s,%s,%s,%s\n";
        bf.write(header);
        Cell[][] cells;
        for (int i = 0; i < MAX_ITER && !currentGrid.isEquilibrated() ; i++) {
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
            currentGrid = currentGrid.getNextGrid();
        }
        bf.close();
    }


    public static void runAll(final int gap, final int iterations) throws IOException {
        for (int particlesQuantity : particlesArray) {
            for (int i = 0; i < iterations; i++) {
                String filename = String.format("%s_d%d_n%d_i%d.csv", "tp2/out/output", gap, particlesQuantity, i);
                run(filename,gap,particlesQuantity);
            }
        }
    }
}
