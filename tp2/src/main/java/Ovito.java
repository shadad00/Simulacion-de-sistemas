import java.io.IOException;
import java.util.HashSet;

public class Ovito {

    public static final int MAX_FRAMES = 500;
    private static final int RENDER_FRAME_INTERVAL = 1;
    private static final int LOG_FRAME_INTERVAL = 5;

    public static void main(String[] args) throws IOException {
        Grid grid = new Grid(2000, 20, 0.05);

        for (int i = 0; i < MAX_FRAMES; i++) {
            if (i % LOG_FRAME_INTERVAL == 0) {
                System.out.println("Simulating frame " + i);
            }

            if (i % RENDER_FRAME_INTERVAL == 0) {
                // TODO: hay que crear el parent folder
                Frame.writeHexagonalCellVelocities(grid, i, "res/results/basic_test/hex");
            }

            grid = grid.getNextGrid();
        }
    }
}
