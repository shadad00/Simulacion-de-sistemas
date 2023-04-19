import simulation.Collisionable;
import simulation.CommonBall;
import simulation.PocketBall;
import simulation.Table;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class sameIC {

    private static final float TABLE_HEIGHT = 112.f;
    private static final float TABLE_WIDTH = 224.f;
    private static final float WHITE_BALL_Y = 46.00f;

    private static final String[] files={"Simulation1","Simulation2"};

    public static void main(String[] args) throws IOException {
        Table initialTable = new Table(WHITE_BALL_Y,TABLE_WIDTH,TABLE_HEIGHT);
        System.out.println(initialTable);
        for (String file : files) {
            new CsvGenerator(file,new Table(initialTable));
            System.out.println(initialTable);
        }
    }
}
