import simulation.Table;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        final Table table = new Table(56.2, 224, 112);

        table.moveUntilAllPocketed();
    }
}
