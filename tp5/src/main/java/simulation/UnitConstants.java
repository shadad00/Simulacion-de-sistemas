package simulation;

public class UnitConstants {
//    public static final int[] FREQUENCIES = {5, 10, 15, 20, 30, 50};
    public static final int[] FREQUENCIES = {5};

    /* Dimensions */
    public static final double BALL_MASS = 0.001; // kg
    public static final double BALL_LOWER_RADIUS = 0.0085; // m
    public static final double BALL_UPPER_RADIUS = 0.0115; // m
    public static final double SILO_WIDTH = 0.2; // m
    public static final double SILO_HEIGHT = 0.7; // m
    public static final double GAP = 0.03; // m
    public static final double SILO_VIBRATION_AMPLITUDE = 0.0015; // m
    public static final double REINSERT_LOWER_BOUND = 0.4; // m
    public static final double REINSERT_UPPER_BOUND = 0.7; // m

    /* Forces */
    public static final double K_n = 250; // N/m
    public static final double K_t = 2 * K_n; // N/m
    public static final double G = -9.81; // m/seg²
}
