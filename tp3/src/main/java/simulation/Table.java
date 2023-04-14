package simulation;

import animation.OvitoWriter;
import simulation.collisions.Collision;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Table {
    private static final double BALL_DIAMETER = 5.7;
    private static final double POCKET_DIAMETER = BALL_DIAMETER * 2;
    private static final double BALL_MASS = 165.0;
    private static final double LOWER_EPSILON = 0.02;
    private static final double UPPER_EPSILON = 0.03;
    public static final double WHITE_BALL_INITIAL_X = 56.;
    public static final double WHITE_BALL_INITIAL_X_VEL = 200.;
    public static final double WHITE_BALL_INITIAL_Y_VEL = 0.;
    public static final double TRIANGLE_X_START = 168.56;
    public static final double TRIANGLE_Y_START = 56.;

    private final HashSet<CommonBall> balls;
    private final HashSet<PocketBall> pocketBalls;
    private final List<Collisionable<Double>> collisionables;
    private final PriorityQueue<Collision<Double>> collisions;
    private double simulationTime;
    private double width;
    private double height;
    private double initWhiteBallY;

    public Table(final double whiteBallY, final double width, final double height) {
        this.simulationTime = 0.0;
        this.width = width;
        this.height = height;
        this.collisions = new PriorityQueue<>();
        this.balls = new HashSet<>();
        this.pocketBalls = new HashSet<>();
        this.collisionables = new ArrayList<>();
        this.initWhiteBallY = whiteBallY;

        positionWhiteBall(whiteBallY);
        positionColorBalls();
        positionPockets();
    }

    public void moveUntilAllPocketed(int simulationNumber) throws IOException {
        OvitoWriter writer = new OvitoWriter();
        writer.openFile(String.format("prueba_%.4f_%s", this.initWhiteBallY, simulationNumber));

        try {
            writer.writeFrame(0, balls, pocketBalls, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int frame = 0;
        Collision<Double> prevCollision = null;
        while (!balls.isEmpty()) {
            updateCollisions(prevCollision);

            Collision<Double> nextCollision = nextCollision();

            moveUntilCollision(nextCollision);
            collide(nextCollision);

            try {
                writer.writeFrame(simulationTime, balls, pocketBalls, nextCollision);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            prevCollision = nextCollision;
            frame++;
        }

        writer.closeFile();
    }

    /**
     * Calcula las proximas colisiones y devuelve la mas proxima.
     *
     * @param previousCollision indica la colision previa, para saber que bolas participaron y actualizar colisiones
     *                          solo para ellas. Si es nulo toma todas las bolas de la mesa.
     * @return
     */
    public void updateCollisions(Collision<Double> previousCollision) {
        final Set<CommonBall> ballsToCollide = previousCollision == null ? balls : previousCollision.getCollisionBalls();

        for (final CommonBall ball : ballsToCollide) {
            if (!balls.contains(ball))
                continue;

            addBallsCollisions(ball, ballsToCollide);
            addPocketCollisions(ball);
            addWallCollisions(ball);
        }
    }

    public Collision<Double> nextCollision() {
        Collision<Double> collision;

        do {
            collision = collisions.poll();
        } while (collision == null || collision.isInvalidated());

        return collision;
    }

    public void collide(Collision<Double> collision) {
        collision.collide();

        if (collision.isWithPocket()) {
            final CommonBall ball = collision.getBall();
            balls.remove(ball);
        }
    }

    private void addBallsCollisions(final CommonBall ball, final Set<CommonBall> prevCollisionedBalls) {
        for (final CommonBall otherBall : balls) {
            // Queremos evitar la doble colision p.ej (ball 1, ball getCollisionTime15) y despues (ball 15, ball 1)
            // Y tambien "auto" colisiones p.ej. (ball 1, ball 1)
            if (prevCollisionedBalls.contains(otherBall) && ball.getBallNumber() >= otherBall.getBallNumber())
                continue;

            final Collision<Double> collision = ball.getCollision(otherBall, simulationTime);
            if (collision != null) {
                collisions.add(collision);
            }
        }
    }

    private void addPocketCollisions(CommonBall ball) {
        for (final PocketBall pocket : pocketBalls) {
            final Collision<Double> collision = ball.getCollision(pocket, simulationTime);
            if (collision != null) {
                collisions.add(collision);
            }
        }
    }

    public void moveUntilCollision(Collision<Double> collision) {
        final Double deltaTime = collision.getCollisionTime() - simulationTime;
        if (deltaTime < 0) {
            throw new RuntimeException();
        }

        for (final CommonBall ball : balls) {
            ball.updatePosition(deltaTime);
        }

        simulationTime = collision.getCollisionTime();
    }

    private void addWallCollisions(final CommonBall ball) {
        // Tenemos dos opciones, o calculamos ad-hoc la colision con paredes o generamos instancias de collisionable
        // para paredes y ya estaria cubierto con el for de arriba
        final Set<Collision<Double>> wallCollisions = new HashSet<>();

        double verticalWallCollisionTime;
        double horizontalWallCollisionTime;

        if (ball.getVelocity().getX() >= 0) {
            verticalWallCollisionTime = (this.width - ball.getPosition().getX() - ball.getRadius()) / ball.getVelocity().getX();
        } else {
            verticalWallCollisionTime = (ball.getRadius() - ball.getPosition().getX()) / ball.getVelocity().getX();
        }

        if (Double.isFinite(verticalWallCollisionTime)) {
            if (verticalWallCollisionTime < 0) {
                System.out.println("Bad delta");
                throw new RuntimeException();
            }

            final Collision<Double> verticalWallCollision = Collision.withVerticalWall(simulationTime + verticalWallCollisionTime, ball);

            wallCollisions.add(verticalWallCollision);
        }

        if (ball.getVelocity().getY() >= 0) {
            horizontalWallCollisionTime = (this.height - ball.getPosition().getY() - ball.getRadius()) / ball.getVelocity().getY();
        } else {
            horizontalWallCollisionTime = (ball.getRadius() - ball.getPosition().getY()) / ball.getVelocity().getY();
        }

        if (Double.isFinite(horizontalWallCollisionTime)) {
            if (horizontalWallCollisionTime < 0) {
                System.out.println("Horizontal wall bad");
                throw new RuntimeException();
            }

            final Collision<Double> horizontalWallCollision = Collision.withHorizontalWall(simulationTime + horizontalWallCollisionTime, ball);

            wallCollisions.add(horizontalWallCollision);
        }

        this.collisions.addAll(wallCollisions);
    }

    private void positionWhiteBall(double whiteBallY) {
        Pair<Double> whiteBallPosition = new Pair<>(WHITE_BALL_INITIAL_X, whiteBallY);
        Pair<Double> whiteBallVelocity = new Pair<>(WHITE_BALL_INITIAL_X_VEL, WHITE_BALL_INITIAL_Y_VEL);
        CommonBall whiteBall = CommonBall.buildWhiteBall(whiteBallPosition, whiteBallVelocity, BALL_MASS, BALL_DIAMETER / 2);
        balls.add(whiteBall);
        collisionables.add(whiteBall);
    }

    private void positionColorBalls() {
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        int ballNumber = 1;
        double db = BALL_DIAMETER;
        double rb = BALL_DIAMETER / 2;
        double rbe = rb + UPPER_EPSILON / 2;
//        double h = Math.sqrt(3) * rb;
        double h  = Math.sqrt(3 * Math.pow(rb, 2) + 3 * rb * UPPER_EPSILON + 5. / 4 * Math.pow(UPPER_EPSILON, 2));

        for (int i = 0; i < 5; i++) {
            double xRow = TRIANGLE_X_START + h * i - (UPPER_EPSILON - rnd.nextDouble(LOWER_EPSILON, UPPER_EPSILON));
            double yStart = TRIANGLE_Y_START - ( rbe * i );
            for (int j = 0; j <= i; j++) {
                double y = yStart + (db + UPPER_EPSILON / 2) * j + (UPPER_EPSILON - rnd.nextDouble(LOWER_EPSILON, UPPER_EPSILON));

                CommonBall colorBall = CommonBall.buildColoredBall(ballNumber++,
                        new Pair<>(xRow, y),
                        BALL_MASS,
                        BALL_DIAMETER / 2);

                balls.add(colorBall);
                collisionables.add(colorBall);
            }
        }

        checkNoBallOverlap();


//        for (int i = 1; i <= 15; i++) {
//            CommonBall colorBall = CommonBall.buildColoredBall(
//                    i,
//                    new Pair<>(
//                            168. + (BALL_DIAMETER + 5) * (i % 4),
//                            56. + (BALL_DIAMETER + 5) * (i / 4)),
//                    BALL_MASS,
//                    BALL_DIAMETER / 2
//            );
//
//            balls.add(colorBall);
//            collisionables.add(colorBall);
//        }
    }

    private void checkNoBallOverlap() {
        for (CommonBall ball : balls) {
            for (CommonBall otherBall : balls) {
                if (ball.equals(otherBall))
                    continue;

                if (ball.distanceTo(otherBall) < 0) {
                    printTable();
                    throw new RuntimeException(String.format("Ball overlap between %s and %s", ball.getBallNumber(), otherBall.getBallNumber()));
                }
            }
        }
    }

    private void positionPockets() {
        for (int i = 0; i <= 1; i++) {
            double y = i * 112.;
            for (int j = 0; j < 3; j++) {
                double x = j * (224.0 / 2);
                PocketBall pocketBall = new PocketBall(new Pair<>(x, y), new Pair<>(0., 0.),
                        0., POCKET_DIAMETER / 2);
                pocketBalls.add(pocketBall);
            }
        }
    }

    public void printTable() {
        System.out.println("t=" + simulationTime);

        for (final CommonBall ball : balls) {
            System.out.println(ball);
        }
    }
}
