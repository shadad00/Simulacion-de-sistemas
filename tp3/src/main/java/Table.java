import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Table {
    private static final double BALL_DIAMETER = 5.7;
    private static final double POCKET_DIAMETER = BALL_DIAMETER * 2;
    private static final double BALL_MASS = 165.0;
    private static final double LOWER_EPSILON = 0.02;
    private static final double UPPER_EPSILON = 0.03;

    private HashSet<CommonBall> balls;
    private HashSet<PocketBall> pocketBalls;
    private List<Collisionable> collisionables;
    private PriorityQueue<Collision> collisions;
    private double simulationTime;
    private double width;
    private double height;

    public Table(final double whiteBallY, final double width, final double height) {
        this.simulationTime = 0.0;
        this.width = width;
        this.height = height;
        this.collisions = new PriorityQueue<>();

        positionWhiteBall(whiteBallY);
        positionColorBalls();
        positionPockets();
    }

    /**
        Calcula las proximas colisiones
        @param
        previouslyCollisionedBalls  indica las instancias de Ball que participaron de la ultima colision para agilizar el calculo de colisiones.
        Si es vacio, se utiliza todas las balls (potencialmente para la primera iteracion)
     **/
    public Collision nextCollision(HashSet<CommonBall> previouslyCollisionedBalls) {
        if (previouslyCollisionedBalls == null)
            previouslyCollisionedBalls = balls;

        for (final CommonBall ball : previouslyCollisionedBalls) {
            for (Collisionable collisionable : collisionables) {
                final Optional<Collision> collision = ball.getCollision(collisionable);
                collision.ifPresent(collisions::add);
            }

            // Tenemos dos opciones, o calculamos ad-hoc la colision con paredes o generamos instancias de collisionable
            // para paredes y ya estaria cubierto con el for de arriba
            Set<Collision> wallCollisions = getWallCollisions(ball);
            collisions.addAll(wallCollisions);
        }

        final Collision incomingCollision = collisions.poll();
        final double deltaTime = incomingCollision.getCollisionTime() - simulationTime;

        moveBalls(deltaTime);
        incomingCollision.apply();

        return incomingCollision;
    }

    private Set<Collision> getWallCollisions(final CommonBall ball) {
        final Set<Collision> collisions = new HashSet<>();

        double verticalWallCollisionTime;
        double horizontalWallCollisionTime;

        if (ball.getVelocity().getX() > 0) {
            verticalWallCollisionTime = (this.width - ball.getPosition().getX()) / ball.getVelocity().getX();
        } else {
            verticalWallCollisionTime = ball.getPosition().getX() / ball.getVelocity().getX();
        }

        final Collision verticalWallCollision =
                new Collision<>(verticalWallCollisionTime, ball, true, false);

        if (ball.getVelocity().getY() > 0) {
            horizontalWallCollisionTime = (this.height - ball.getPosition().getY()) / ball.getVelocity().getY();
        } else {
            horizontalWallCollisionTime = ball.getPosition().getY() / ball.getVelocity().getY();
        }

        final Collision horizontalWallCollision =
                new Collision<>(horizontalWallCollisionTime, ball, false, true);

        collisions.add(verticalWallCollision);
        collisions.add(horizontalWallCollision);

        return collisions;
    }

    private void positionWhiteBall(double whiteBallY) {
        Pair<Double> whiteBallPosition = new Pair<>(56., whiteBallY);
        Pair<Double> whiteBallVelocity = new Pair<>(200., 0.);
        CommonBall whiteBall = new CommonBall(whiteBallPosition, whiteBallVelocity, BALL_MASS, BALL_DIAMETER);
        balls.add(whiteBall);
        collisionables.add(whiteBall);
    }

    private void positionColorBalls() {
        // TODO
        ThreadLocalRandom rng = ThreadLocalRandom.current();
        double xBase = 168.56;
        double yBase = 56.0;
        for (int i = 0; i < 5; i++) { // para un poco estamos haciendolo con el licio //ok
            double x = xBase + i * BALL_DIAMETER + rng.nextDouble(LOWER_EPSILON, UPPER_EPSILON);
            double yStart = yBase - (i * BALL_DIAMETER + i * UPPER_EPSILON);
            for (int j = 0; j <= i; j++) {
                double y = yStart + j * BALL_DIAMETER + rng.nextDouble(LOWER_EPSILON, UPPER_EPSILON);

                CommonBall colorBall = new CommonBall(new Pair<>(x, y), new Pair<>(0., 0.), BALL_MASS, BALL_DIAMETER);

                balls.add(colorBall);
                collisionables.add(colorBall);
            }
        }
    }

    private void positionPockets() {
        // TODO
        for (int i = 0; i < 2; i++) {
            double x = i * 112.;
            for (int j = 0; j < 3; j++) {
                double y = j * (224.0 / 3);
                PocketBall pocketBall = new PocketBall(new Pair<>(x, y), new Pair<>(0., 0.),
                        0., POCKET_DIAMETER);
                pocketBalls.add(pocketBall);
            }
        }
    }

    private void moveBalls(final double deltaTime) {
        for (final CommonBall ball : balls) {
            ball.updatePosition(deltaTime);
        }
    }

}
