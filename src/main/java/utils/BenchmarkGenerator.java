package utils;

import simulation.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BenchmarkGenerator {
    private final int numberOfIterations;
    private List<BenchmarkInfo> benchmarkInfoList;

    public void run(double gridSide, int cellQuantity, int particleQuantity, double particleRadius, double cutoffRadius) {
        List<BenchmarkInfo> benchmarkInfoList = new ArrayList<>();

        Grid duplicateBorderGrid = new PeriodicGridDuplicateBorders(gridSide, cellQuantity, cutoffRadius);
        Grid noPeriodicGrid = new NoPeriodicGrid(gridSide, cellQuantity, cutoffRadius);
        Grid halfDistanceGrid = new PeriodicGridHalfDistance(gridSide, cellQuantity, cutoffRadius);
        List<Grid> gridList = new ArrayList<>();
        gridList.add(duplicateBorderGrid);
        gridList.add(noPeriodicGrid);
        gridList.add(halfDistanceGrid);


        for (int i = 0; i < numberOfIterations; i++) {
            Set<Particle> particleSet = particleGenerator(particleQuantity, gridSide, particleRadius, cutoffRadius);

            gridList.forEach((g) -> {
                g.clearParticles();
                g.placeParticles(particleSet);

                long startTime = System.currentTimeMillis();
                g.computeDistanceBetweenParticles();
                long endTime = System.currentTimeMillis();

                BenchmarkInfo benchmarkInfo = new BenchmarkInfo(g.getClass().getSimpleName(),
                        endTime - startTime,
                        gridSide,
                        cellQuantity,
                        particleQuantity,
                        particleRadius,
                        cutoffRadius);

                benchmarkInfoList.add(benchmarkInfo);
            });

        }
        this.benchmarkInfoList = benchmarkInfoList;
    }

    public void writeCSV(String filename) throws IOException {
        if (benchmarkInfoList == null) {
            return;
        }

        final BufferedWriter writer = new BufferedWriter(new FileWriter(filename));

        writeHeaders(writer);

        for (BenchmarkInfo bi : benchmarkInfoList) {
            final String line = String.format("%s,%s,%s,%s,%s,%s,%s\n", bi.gridMethod, bi.evaluationTime, bi.gridSide, bi.cellQuantity, bi.particleQuantity, bi.particleRadius, bi.cutoffRadius);
            writer.write(line);
        }

        writer.close();
    }

    private void writeHeaders(BufferedWriter bf) throws IOException {
        final String line = String.format("%s,%s,%s,%s,%s,%s,%s\n", "grid method", "eval time (ms)", "grid side", "cell quantity", "particle quantity", "particle radius", "cutoff radius");
        bf.write(line);
    }

    public BenchmarkGenerator(int numberOfIterations) {
        this.numberOfIterations = numberOfIterations;
    }

    public static void main(String[] args) {
        BenchmarkGenerator bg = new BenchmarkGenerator(10);
        bg.run(20, 4, 1000, 0.25, 1);
        try {
            bg.writeCSV("./pruebacsv.csv");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public class BenchmarkInfo {
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
