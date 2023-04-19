package simulation;

import simulation.collisions.Collision;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Table implements Iterable<Table> {
    private static final float BALL_DIAMETER = 5.7f;
    private static final float POCKET_DIAMETER = BALL_DIAMETER * 2;
    private static final float BALL_MASS = 165.0f;
    private static final float LOWER_EPSILON = 0.02f;
    private static final float UPPER_EPSILON = 0.03f;
    public static final float WHITE_BALL_INITIAL_X = 56.f;
    public static final float WHITE_BALL_INITIAL_X_VEL = 200.f;
    public static final float WHITE_BALL_INITIAL_Y_VEL = 0.f;
    public static final float TRIANGLE_X_START = 168.56f;
    public static final float TRIANGLE_Y_START = 56.f;

    private int iteration = 0;

    private final Set<CommonBall> balls;
    private final Set<PocketBall> pocketBalls;
    private List<Collisionable<Float>> collisionables;
    private PriorityQueue<Collision<Float>> collisions;
    private float simulationTime;
    private final float width;
    private final float height;
    private Collision<Float> prevCollision = null;
    private float initWhiteBallY;

    public List<Collisionable<Float>> getCollisionables() {
        return collisionables;
    }

    public PriorityQueue<Collision<Float>> getCollisions() {
        return collisions;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public Collision<Float> getPrevCollision() {
        return prevCollision;
    }

    public float getInitWhiteBallY() {
        return initWhiteBallY;
    }

    public Table(final Set<CommonBall> balls, final float width, final float height, final float time, final int iteration){
        this.height = height;
        this.width = width;
        this.balls = balls;
        this.simulationTime = time;
        this.iteration = iteration;
        this.pocketBalls = new HashSet<>();
        this.collisions = new PriorityQueue<>();
        this.collisionables = new ArrayList<>();
        this.collisionables.addAll(balls);
        positionPockets();
    }

    public Table(Table other){
        this.initWhiteBallY = other.initWhiteBallY;
         this.iteration = other.iteration;
         this.balls = new HashSet<>();
         for (CommonBall ball : other.balls)
             this.balls.add(new CommonBall(ball));
         this.pocketBalls = new HashSet<>(other.pocketBalls);
         this.collisionables = new ArrayList<>(other.collisionables);
         this.collisions = new PriorityQueue<>(other.collisions);
         this.simulationTime = other.simulationTime;
         this.width = other.width;
         this.height = other.height;
         this.prevCollision = other.prevCollision;
    }



    public Table(final float whiteBallY, final float width, final float height) {
        this.simulationTime = 0.0f;
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

    @Override
    public Iterator<Table> iterator() {
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return !balls.isEmpty();
            }

            @Override
            public Table next() {
                incrementIteration();
                return getNextTable();
            }
        };
    }

    private void incrementIteration(){
        this.iteration++;
    }

    public int getIteration() {
        return iteration;
    }

    public float getSimulationTime() {
        return simulationTime;
    }

    public Set<CommonBall> getBalls() {
        return balls;
    }

    public Table getNextTable() {
        updateCollisions(prevCollision);
        Collision<Float> nextCollision = nextCollision();
        moveUntilCollision(nextCollision);
        collide(nextCollision);
        prevCollision = nextCollision;
        return new Table(this.width, this.height, this.simulationTime,
                this.balls, this.pocketBalls, this.collisionables, this.collisions, this.iteration);
    }

    public Table(float width,
                   float height,
                   float simulationTime,
                   Set<CommonBall> commonBalls,
                   Set<PocketBall> pocketBalls,
                   List<Collisionable<Float>> collisionables,
                   PriorityQueue<Collision<Float>> collisionQueue,
                   int iteration
                   ) {
        this.width = width;
        this.height = height;
        this.simulationTime = simulationTime;
        this.balls = commonBalls;
        this.pocketBalls = pocketBalls;
        this.collisionables = collisionables;
        this.collisions = collisionQueue;
        this.iteration = iteration;
    }


    /**
     * Calcula las proximas colisiones y devuelve la mas proxima.
     *
     * @param previousCollision indica la colision previa, para saber que bolas participaron y actualizar colisiones
     *                          solo para ellas. Si es nulo toma todas las bolas de la mesa.
     *
     */
    public void updateCollisions(Collision<Float> previousCollision) {
        final Set<CommonBall> ballsToCollide = previousCollision == null ? balls : previousCollision.getCollisionBalls();

        for (final CommonBall ball : ballsToCollide) {
            if (!balls.contains(ball))
                continue;

            addBallsCollisions(ball, ballsToCollide);
            addPocketCollisions(ball);
            addWallCollisions(ball);
        }
    }

    public Collision<Float> nextCollision() {
        Collision<Float> collision;

        do {
            collision = collisions.poll();
        } while (collision == null || collision.isInvalidated());

        return collision;
    }

    public void collide(Collision<Float> collision) {
        collision.collide();

        if (collision.isWithPocket()) {
            final CommonBall ball = collision.getBall();
            balls.remove(ball);
            collisionables.remove(ball);
        }
    }

    private void addBallsCollisions(final CommonBall ball, final Set<CommonBall> prevCollisionedBalls) {
        for (final CommonBall otherBall : balls) {
            // Queremos evitar la doble colision p.ej (ball 1, ball getCollisionTime15) y despues (ball 15, ball 1)
            // Y tambien "auto" colisiones p.ej. (ball 1, ball 1)
            if (prevCollisionedBalls.contains(otherBall) && ball.getBallNumber() >= otherBall.getBallNumber())
                continue;

            final Collision<Float> collision = ball.getCollision(otherBall, simulationTime);
            if (collision != null) {
                collisions.add(collision);
            }
        }
    }

    private void addPocketCollisions(CommonBall ball) {
        for (final PocketBall pocket : pocketBalls) {
            final Collision<Float> collision = ball.getCollision(pocket, simulationTime);
            if (collision != null) {
                collisions.add(collision);
            }
        }
    }

    public void moveUntilCollision(Collision<Float> collision) {
        final float deltaTime = collision.getCollisionTime() - simulationTime;
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
        final Set<Collision<Float>> wallCollisions = new HashSet<>();

        float verticalWallCollisionTime;
        float horizontalWallCollisionTime;

        if (ball.getVelocity().getX() >= 0) {
            verticalWallCollisionTime = (this.width - ball.getPosition().getX() - ball.getRadius()) / ball.getVelocity().getX();
        } else {
            verticalWallCollisionTime = (ball.getRadius() - ball.getPosition().getX()) / ball.getVelocity().getX();
        }

        if (Float.isFinite(verticalWallCollisionTime)) {
            if (verticalWallCollisionTime < 0) {
                System.out.println("Bad delta");
                throw new RuntimeException();
            }

            final Collision<Float> verticalWallCollision = Collision.withVerticalWall(simulationTime + verticalWallCollisionTime, ball);

            wallCollisions.add(verticalWallCollision);
        }

        if (ball.getVelocity().getY() >= 0) {
            horizontalWallCollisionTime = (this.height - ball.getPosition().getY() - ball.getRadius()) / ball.getVelocity().getY();
        } else {
            horizontalWallCollisionTime = (ball.getRadius() - ball.getPosition().getY()) / ball.getVelocity().getY();
        }

        if (Float.isFinite(horizontalWallCollisionTime)) {
            if (horizontalWallCollisionTime < 0) {
                System.out.println("Horizontal wall bad");
                throw new RuntimeException();
            }

            final Collision<Float> horizontalWallCollision = Collision.withHorizontalWall(simulationTime + horizontalWallCollisionTime, ball);

            wallCollisions.add(horizontalWallCollision);
        }

        this.collisions.addAll(wallCollisions);
    }

    private void positionWhiteBall(float whiteBallY) {
        Pair<Float> whiteBallPosition = new Pair<>(WHITE_BALL_INITIAL_X, whiteBallY);
        Pair<Float> whiteBallVelocity = new Pair<>(WHITE_BALL_INITIAL_X_VEL, WHITE_BALL_INITIAL_Y_VEL);
        CommonBall whiteBall = CommonBall.buildWhiteBall(whiteBallPosition, whiteBallVelocity, BALL_MASS, BALL_DIAMETER / 2);
        balls.add(whiteBall);
        collisionables.add(whiteBall);
    }

    private void positionColorBalls() {
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        int ballNumber = 1;
        float rb = BALL_DIAMETER / 2;
        float rbe = rb + UPPER_EPSILON / 2;
        double h  = Math.sqrt(3 * Math.pow(rb, 2) + 3 * rb * UPPER_EPSILON + 5. / 4 * Math.pow(UPPER_EPSILON, 2));

        for (int i = 0; i < 5; i++) {
            float xRow = (float) (TRIANGLE_X_START + h * i - (UPPER_EPSILON - rnd.nextDouble(LOWER_EPSILON, UPPER_EPSILON)));
            float yStart = TRIANGLE_Y_START - ( rbe * i );
            for (int j = 0; j <= i; j++) {
                float y = (float) (yStart + (BALL_DIAMETER + UPPER_EPSILON / 2) * j + (UPPER_EPSILON - rnd.nextDouble(LOWER_EPSILON, UPPER_EPSILON)));

                CommonBall colorBall = CommonBall.buildColoredBall(ballNumber++,
                        new Pair<>(xRow, y),
                        BALL_MASS,
                        BALL_DIAMETER / 2);

                balls.add(colorBall);
                collisionables.add(colorBall);
            }
        }

        checkNoBallOverlap();
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
            float y = i * 112.f;
            for (int j = 0; j < 3; j++) {
                float x = j * (224.0f / 2);
                PocketBall pocketBall = new PocketBall(new Pair<>(x, y), new Pair<>(0.f, 0.f),
                        0.f, POCKET_DIAMETER / 2);
                pocketBalls.add(pocketBall);
            }
        }
    }

    public Set<PocketBall> getPocketBalls() {
        return pocketBalls;
    }


    public void printTable() {
        System.out.println("t=" + simulationTime);

        for (final CommonBall ball : balls) {
            System.out.println(ball);
        }
    }

    @Override
    public String toString() {
        return "Table{" +
                "iteration=" + iteration +
                ", balls=" + balls +
                ", pocketBalls=" + pocketBalls +
                ", collisionables=" + collisionables +
                ", collisions=" + collisions +
                ", simulationTime=" + simulationTime +
                ", width=" + width +
                ", height=" + height +
                ", prevCollision=" + prevCollision +
                ", initWhiteBallY=" + initWhiteBallY +
                '}';
    }
}
