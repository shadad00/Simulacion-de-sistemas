package utils.generators;

import simulation.Particle;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.util.ArrayList;
import java.util.List;

public class ControlSquareGrid {
    public static void main(String[] args) throws IOException {
        ControlSquareGrid.generateGrid(20, 2, 1);
    }

    public static void generateGrid(final int areaSideLength, final double cutoffRadius, final double step) throws IOException {
        final List<Particle> particles = createParticles(areaSideLength, cutoffRadius, step);

        saveParticlesFiles(particles, areaSideLength, step, cutoffRadius);
    }

    private static List<Particle> createParticles(final int areaSideLength, final double cutoffRadius, final double step) {
        System.out.printf(
                "Generating control grid with areaSideLength=%s, cutoffRadius=%.2f, step=%.2f%n",
                areaSideLength,
                cutoffRadius,
                step
        );
        final List<Particle> particles = new ArrayList<>();

        for (int y = 0; y < areaSideLength; y += step) {
            for (int x = 0; x < areaSideLength; x += step) {
                final Particle particle = new Particle(x, y, 0.25, cutoffRadius);
                System.out.println();

                particles.add(particle);
            }
        }

        return particles;
    }

    private static void saveParticlesFiles(
            final List<Particle> particles,
            final int areaSideLength,
            final double step,
            final double cutoffRadius
    ) throws IOException {
        saveParticlesStaticFile(particles, areaSideLength, step, cutoffRadius);
        saveParticlesDynamicFile(particles, areaSideLength, step, cutoffRadius);
    }

    private static void saveParticlesStaticFile(
            final List<Particle> particles,
            final int areaSideLength,
            final double step,
            final double cutoffRadius
    ) throws IOException {
        final String filename = String.format("res/grids/static_control_%s_%.2f_%.2f.xyz", areaSideLength, step, cutoffRadius);
        final BufferedWriter writer = new BufferedWriter(new FileWriter(filename));

        final int particlesSize = particles.size();
        writer.write(String.format("%s\n", particlesSize));
        writer.write(String.format("%s\n", (int) areaSideLength));

        for (final Particle particle : particles) {
            final String line = String.format("%s %s\n", particle.getRadius(), particle.getCutOffRadius());

            writer.write(line);
        }

        writer.close();
    }

    private static void saveParticlesDynamicFile(
            final List<Particle> particles,
            final int areaSideLength,
            final double step,
            final double cutoffRadius
    ) throws IOException {
        final String filename = String.format("res/grids/dynamic_control_%s_%.2f_%.2f.xyz", areaSideLength, step, cutoffRadius);
        final BufferedWriter writer = new BufferedWriter(new FileWriter(filename));

        for (final Particle particle : particles) {
            final String line = String.format("%s %s\n", particle.getX(), particle.getY());

            writer.write(line);
        }

        writer.close();
    }
}
