package simulation;

import simulation.collisions.Collision;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Table implements Iterable<Table> {
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

    private int iteration = 0;

    private final Set<CommonBall> balls;
    private Set<PocketBall> pocketBalls;
    private List<Collisionable<Double>> collisionables;
    private PriorityQueue<Collision<Double>> collisions;
    private double simulationTime;
    private final double width;
    private final double height;
    private Collision<Double> prevCollision = null;

    public Table(final Set<CommonBall> balls, final double width, final double height, final double time, final int iteration){
        this.height = height;
        this.width = width;
        this.balls = balls;
        this.simulationTime = time;
        this.iteration = iteration;
        positionPockets();
    }



    public Table(final double whiteBallY, final double width, final double height) {
        this.simulationTime = 0.0;
        this.width = width;
        this.height = height;
        this.collisions = new PriorityQueue<>();
        this.balls = new HashSet<>();
        this.pocketBalls = new HashSet<>();
        this.collisionables = new ArrayList<>();

        positionWhiteBall(whiteBallY);
        positionColorBalls();
        positionPockets();
    }

    @Override
    public Iterator<Table> iterator() {
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return !balls.isEmpty() && simulationTime >= 0;
            }

            @Override
            public Table next() {
                iteration++;
                return getNextTable();
            }
        };
    }

    public int getIteration() {
        return iteration;
    }

    public double getSimulationTime() {
        return simulationTime;
    }

    public Set<CommonBall> getBalls() {
        return balls;
    }

    public Table getNextTable() {
        updateCollisions(prevCollision);
        Collision<Double> nextCollision = nextCollision();
        moveUntilCollision(nextCollision);
        collide(nextCollision);
        prevCollision = nextCollision;
        return new Table(this.width, this.height, this.simulationTime,
                this.balls, this.pocketBalls, this.collisionables, this.collisions);
    }

    public Table(Double width,
                   Double height,
                   Double simulationTime,
                   Set<CommonBall> commonBalls,
                   Set<PocketBall> pocketBalls,
                   List<Collisionable<Double>> collisionables,
                   PriorityQueue<Collision<Double>> collisionQueue
                   ) {
        this.width = width;
        this.height = height;
        this.simulationTime = simulationTime;
        this.balls = commonBalls;
        this.pocketBalls = pocketBalls;
        this.collisionables = collisionables;
        this.collisions = collisionQueue;
    }

    public void moveUntilAllPocketed()  {
        int frame = 0;
        Collision<Double> prevCollision = null;
        while (!balls.isEmpty()) {
            updateCollisions(prevCollision);

            Collision<Double> nextCollision = nextCollision();

            moveUntilCollision(nextCollision);
            collide(nextCollision);


            prevCollision = nextCollision;
            frame++;
        }
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
        System.out.println(collision);

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
        double h = (Math.sqrt(3) * rb) + UPPER_EPSILON;

        for (int i = 0; i < 5; i++) {
            double xRow = TRIANGLE_X_START + (h - rnd.nextDouble(LOWER_EPSILON, UPPER_EPSILON)) * i;
            double yStart = TRIANGLE_Y_START - ( rb * i );
            for (int j = 0; j <= i; j++) {
                double y = yStart + (db + rnd.nextDouble(LOWER_EPSILON, UPPER_EPSILON)) * j;

                CommonBall colorBall = CommonBall.buildColoredBall(ballNumber++,
                        new Pair<>(xRow, y),
                        BALL_MASS,
                        BALL_DIAMETER / 2);

                balls.add(colorBall);
                collisionables.add(colorBall);
            }
        }


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
