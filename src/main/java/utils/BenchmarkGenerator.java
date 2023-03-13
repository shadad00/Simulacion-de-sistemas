package utils;

import simulation.*;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BenchmarkGenerator {
    private int numberOfIterations;
    private List<BenchmarkInfo> benchmarkInfoList;

    public void run(double gridSide, int cellQuantity, int particleQuantity, double particleRadius, double cutoffRadius) {
        List<BenchmarkInfo> benchmarkInfoList = new ArrayList<>();

        Grid noPeriodicGrid = new NoPeriodicGrid(gridSide, cellQuantity, cutoffRadius);
        Grid halfDistanceGrid = new PeriodicGridHalfDistance(gridSide, cellQuantity, cutoffRadius);
        List<Grid> gridList = new ArrayList<>();

        gridList.add(noPeriodicGrid);
        gridList.add(halfDistanceGrid);

        Grid testNoPeriodicGrid = new NoPeriodicGrid(gridSide, 1, cutoffRadius);
        Grid testHalfDistanceGrid = new PeriodicGridHalfDistance(gridSide, 1, cutoffRadius);


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

        for (BenchmarkInfo bi : benchmarkInfoList) {
            final String line = String.format("%s,%s,%s,%s,%s,%s,%s\n", bi.gridMethod, bi.evaluationTime, bi.gridSide, bi.cellQuantity, bi.particleQuantity, bi.particleRadius, bi.cutoffRadius);
            writer.write(line);
        }

        writer.close();
    }

    public BenchmarkGenerator(int numberOfIterations) {
        this.numberOfIterations = numberOfIterations;
    }

    public static void main(String[] args) {
        BenchmarkGenerator bg = new BenchmarkGenerator(100);
        bg.run(20, 4, 1000, 0.25, 1);
        try {
            bg.writeCSV("./pruebacsv.csv");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public class BenchmarkInfo {
        private String gridMethod;
        private double evaluationTime;
        private double gridSide;
        private int cellQuantity;
        private int particleQuantity;
        private double particleRadius;
        private double cutoffRadius;

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
