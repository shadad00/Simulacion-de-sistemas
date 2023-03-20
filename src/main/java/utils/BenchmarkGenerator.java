package utils;

import simulation.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class BenchmarkGenerator {
    private final int numberOfIterations;
    private final List<BenchmarkInfo> benchmarkInfoList = new ArrayList<>();

    public void run(double gridSide, int cellQuantity, int particleQuantity, double particleRadius, double cutoffRadius) {

        Grid noPeriodicGrid = new NoPeriodicGrid(gridSide, cellQuantity);
        Grid halfDistanceGrid = new PeriodicGridHalfDistance(gridSide, cellQuantity);
        List<Grid> gridList = new ArrayList<>();

        gridList.add(noPeriodicGrid);
        gridList.add(halfDistanceGrid);

        for (int i = 0; i < numberOfIterations; i++) {
            Set<Particle> particleSet = particleGenerator(particleQuantity, gridSide, particleRadius, cutoffRadius);

            gridList.forEach((g) -> {
                g.clearParticles();
                g.placeParticles(particleSet);

                long startTime = System.nanoTime();
                g.computeDistanceBetweenParticles();
                long endTime = System.nanoTime();

                BenchmarkInfo benchmarkInfo = new BenchmarkInfo(g.getClass().getSimpleName(),
                        endTime - startTime,
                        gridSide,
                        cellQuantity,
                        particleQuantity,
                        particleRadius,
                        cutoffRadius);

                this.benchmarkInfoList.add(benchmarkInfo);
            });

        }
    }

    public void writeCSV(File file) throws IOException {
        final BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));

        String line = String.format("%s,%s,%s,%s,%s,%s,%s\n", "grid method", "eval time (ns)", "grid side", "cell quantity", "particle quantity", "particle radius", "cutoff radius");
        writer.write(line);
        for (BenchmarkInfo bi : benchmarkInfoList) {
            line = String.format("%s,%s,%s,%s,%s,%s,%s\n", bi.gridMethod, bi.evaluationTime, bi.gridSide, bi.cellQuantity, bi.particleQuantity, bi.particleRadius, bi.cutoffRadius);
            writer.write(line);
        }

        writer.close();
    }

    public BenchmarkGenerator(int numberOfIterations) {
        this.numberOfIterations = numberOfIterations;
    }

    public static void main(String[] args) {
        BenchmarkGenerator bg = new BenchmarkGenerator(10);
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd:HH:mm");
        String filename = "./results"+dateFormat.format(currentDate)+".csv";
        File file = new File(filename);
        double gridSide = 20.0;
        int particleQ = 1000;
        double particleRadius = 1;
        double cutoffRadius = 1;

        try {
            if (!file.exists() && !file.createNewFile()) {
                throw new RuntimeException("Unable to create csv file.");
            }
            for (int m = 1; (gridSide / m) > particleRadius ; m++)
                bg.run(gridSide, m, particleQ, particleRadius, cutoffRadius);

            bg.writeCSV(file);

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static class BenchmarkInfo {
        private final String gridMethod;
        private final double evaluationTime;
        private final double gridSide;
        private final int cellQuantity;
        private final int particleQuantity;
        private final double particleRadius;
        private final double cutoffRadius;

        public BenchmarkInfo(String gridMethod, double evaluationTime, double gridSide, int cellQuantity, int particleQuantity, double particleRadius, double cutoffRadius) {
            this.gridMethod = gridMethod;
            this.evaluationTime = evaluationTime;
            this.gridSide = gridSide;
            this.cellQuantity = cellQuantity;
            this.particleQuantity = particleQuantity;
            this.particleRadius = particleRadius;
            this.cutoffRadius = cutoffRadius;
        }

        @Override
        public String toString() {
            return "BenchmarkInfo{" +
                    "gridMethod='" + gridMethod.getClass().getSimpleName() + '\'' +
                    ", evaluationTime=" + evaluationTime + "ms" +
                    ", gridSide=" + gridSide +
                    ", cellQuantity=" + cellQuantity +
                    ", particleQuantity=" + particleQuantity +
                    ", particleRadius=" + particleRadius +
                    ", cutoffRadius=" + cutoffRadius +
                    '}';
        }
    }

    public Set<Particle> particleGenerator(int particleQuantity, double gridSide, double particleRadius, double cutoffRadius) {
        Set<Particle> particles = new HashSet<>();
        for (int i = 0; i < particleQuantity; i++) {
            double x = Math.random() * gridSide;
            double y = Math.random() * gridSide;

            particles.add(new Particle(x, y, particleRadius, cutoffRadius));
        }

        return particles;
    }
}
