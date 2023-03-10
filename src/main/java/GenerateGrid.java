import animation.Frame;
import simulation.Grid;
import simulation.Particle;
import simulation.ParticleAndDistance;
import simulation.PeriodicGridDuplicateBorders;
import utils.Parser;
import utils.generators.ControlSquareGrid;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class GenerateGrid {
    public static void main(String[] args) throws IOException {
        ControlSquareGrid.generateGrid(20, 5, 1);
    }
}
