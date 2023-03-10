package animation;

import simulation.Particle;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Frame<T extends Particle> {

    final int frameNumber;
    final List<T> particles;

    public Frame(final int frameNumber, final List<T> particles) {
        this.frameNumber = frameNumber;
        this.particles = particles;
    }

    public static <T extends Particle> void write(final List<T> particles, final String filename, final int frameNumber)
            throws IOException {
        final int particlesSize = particles.size();

        final BufferedWriter writer = new BufferedWriter(new FileWriter(filename));

        writer.write(String.format("%s\n", particlesSize));
        writer.newLine();

        // Podriamos refactorear esto y pasarle un generador de filas en caso de que querramos tener
        // lineas de formato dinamico
        for (T particle : particles) {
            final Color color = particle.getColor();
            final String line = String.format("%s %s %s %s %s\n", particle.getX(), particle.getY(), color.getRed(), color.getGreen(), color.getBlue());

            writer.write(line);
        }

        writer.close();
    }

    public static <T extends Particle> void write(final List<T> particles, final String filename)
            throws IOException {
        write(particles, filename, 0);
    }
}
