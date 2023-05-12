package table;

import lombok.Getter;
import lombok.Setter;
import utils.Pair;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

import static java.lang.Math.pow;

@Getter
@Setter
public class CommonBall extends Ball implements Comparable<CommonBall> {
    private static final double k = Math.pow(10, 2); // TODO: revisar si las unidades estan bien N/m
    private final int ballNumber;
    private final double[] multipliers = {3. / 20, 251. / 360, 1., 11. / 18, 1. / 6, 1. / 60};
    private double[][] position_derivatives;
    private double[][] predictions = null;

    private Pair positionPrediction = Pair.of(0.,0.) ;

    protected double dt;

    protected int[] factorials = { factorial(0), factorial(1), factorial(2),
        factorial(3),factorial(4),factorial(5) };

    protected double[] dt_k;

    public CommonBall(CommonBall other){
        super();
        this.position = new Pair(other.position.getX(), other.getPosition().getY());
        this.velocity = new Pair(other.velocity.getX(), other.velocity.getY());
        this.acceleration = new Pair(other.acceleration.getX(), other.acceleration.getY());
        this.force = new Pair(other.force.getX(), other.force.getY());
        this.mass = other.mass;
        this.radius = other.radius;
        this.ballNumber = other.ballNumber;
        this.position_derivatives = new double[2][];
        for (int i = 0; i < other.getPosition_derivatives().length; i++)
            this.position_derivatives[i] = Arrays.copyOf(other.getPosition_derivatives()[i],other.getPosition_derivatives()[i].length);
    }
    
    public CommonBall(final int ballNumber, Pair position,
                      Pair velocity,Pair acceleration ,Pair force,
                      final Double mass, final Double radius) {
        super(position, velocity, acceleration, force, mass, radius);
        this.ballNumber = ballNumber;
        this.position_derivatives = new double[2][];
        position_derivatives[0] = new double[]
                {position.getX(), velocity.getX(),acceleration.getX(),0.,0.,0.};
        position_derivatives[1] = new double[]
                {position.getY(), velocity.getY(),acceleration.getY(),0.,0.,0.};

    }

    public void setDt(double dt){
        this.dt = dt;
        dt_k = new double[6];
        for (int k = 0; k < dt_k.length; k++) {
            dt_k[k] = Math.pow(dt, k);
        }

    }


    public void updateWithPrediction() {

        double[][] derivative_predictions = new double[2][6];
        for (int i = 0; i < position_derivatives.length; i++) {
            for (int j = 0; j < position_derivatives[i].length; j++) {
                derivative_predictions[i][j] = taylorEvaluation(
                        Arrays.copyOfRange(position_derivatives[i], j,
                                position_derivatives[i].length), dt);
            }
        }

        predictions = Arrays.copyOf(derivative_predictions, derivative_predictions.length);
        // Esto es solo para calcular la fuerza en base a las posiciones predichas,
        // luego cuando se corrige, se setea la posicion final
        setPosition(Pair.of(predictions[0][0], predictions[1][0]));
    }


    public void sumForces(final Set<CommonBall> otherBalls, final double tableWidth, final double tableHeight) {
       this.force = new Pair(0,0);

        for (CommonBall otherBall : otherBalls) {
            Pair forceBetweenBalls = forceBetween(otherBall);
            if (!forceBetweenBalls.equals(Pair.ZERO)){
                this.force.add(forceBetweenBalls);
            }
        }

        this.force.add(forceBetweenLeftWall());
        this.force.add(forceBetweenBottomWall());
        this.force.add(forceBetweenRightWall(tableWidth));
        this.force.add(forceBetweenTopWall(tableHeight));

    }

    public void correctPrediction() {
        Pair newForce = this.getForce();
        double drx2 = (newForce.getX() / mass)  - predictions[0][2];
        double R2x = drx2 * dt_k[2] / 2;
        double dry2 = (newForce.getY() / mass)  - predictions[1][2];
        double R2y = dry2 * dt_k[2] / 2;
        double[] rectifier = {R2x, R2y};

        double[][] newPositions = new double[2][6];
        for (int i = 0; i < predictions.length; i++) {
            for (int j = 0; j < predictions[i].length; j++) {
                int jFact = factorials[j];
                newPositions[i][j] = predictions[i][j] + multipliers[j] * rectifier[i] * jFact / dt_k[j];
            }
        }

        position_derivatives[0] = Arrays.copyOf(newPositions[0], newPositions[0].length);
        position_derivatives[1] = Arrays.copyOf(newPositions[1], newPositions[1].length);

        setPosition(new Pair(position_derivatives[0][0],position_derivatives[1][0]));
        setVelocity(new Pair(position_derivatives[0][1],position_derivatives[1][1]));
        setAcceleration(new Pair(position_derivatives[0][2], position_derivatives[1][2]));
    }



    public boolean isOverlapping(Ball otherBall){
        double xDiff = otherBall.getPosition().getX() - getPosition().getX();
        double yDiff = otherBall.getPosition().getY() - getPosition().getY();
        double dist = Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));
        double sumRadius = getRadius() + otherBall.getRadius();
        return ! (dist > sumRadius) ;
    }

    public Pair forceBetween(CommonBall otherBall) {
        double xDiff = otherBall.getPosition().getX() - getPosition().getX();
        double yDiff = otherBall.getPosition().getY() - getPosition().getY();
        double dist = Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));
        double sumRadius = getRadius() + otherBall.getRadius();

        // Si no estan en colision, la fuerza entre ellos es nula -> <0, 0>
        if (dist > sumRadius)
            return Pair.ZERO;

        // Ignoramos el versor normal r^ ya que vamos a proyectar sobre x e y directamente
        double Fn = k * (Math.abs(dist) - sumRadius);
        double Fx = Fn * xDiff / dist;
        double Fy = Fn * yDiff / dist;

        return Pair.of(Fx, Fy);
    }

    public Pair forceBetweenRightWall(double wallX) {
        if (position.getX() + getRadius() <= wallX)
            return Pair.ZERO;

        double xDiff = Math.abs(position.getX() + getRadius() - wallX);
        double F = - k * Math.abs(xDiff);

        return Pair.of(F, 0);
    }

    public Pair forceBetweenLeftWall() {
        if (position.getX() - getRadius() >= 0)
            return Pair.ZERO;

        double xDiff = Math.abs(position.getX() - getRadius());
        double F = k * Math.abs(xDiff);

        return Pair.of(F, 0);
    }

    public Pair forceBetweenTopWall(double wallY) {
        if (position.getY() + getRadius() <= wallY)
            return Pair.ZERO;

        double yDiff = Math.abs(position.getY() + getRadius() - wallY);
        double F = - k * Math.abs(yDiff);

        return Pair.of(0, F);
    }

    public Pair forceBetweenBottomWall() {
        if (position.getY() - getRadius() >= 0)
            return Pair.ZERO;

        double yDiff = Math.abs(position.getY() - getRadius());
        double F = k * Math.abs(yDiff);

        return Pair.of(0, F);
    }

    @Override
    public boolean isPocket() {
        return false;
    }

    public int getBallNumber() {
        return ballNumber;
    }

    @Override
    public String toString() {
        return "simulation.CommonBall{" +
                "ballNumber=" + ballNumber +
                ", position=" + position +
                ", velocity=" + velocity +
                '}';
    }


    public static CommonBall buildWhiteBall(Pair position, Pair velocity, final Double mass,
                                            final Double radius, double deltaTime) {
        CommonBall commonBall = new CommonBall(0, position, velocity,
                new Pair(0., 0.), new Pair(0., 0.),
                mass, radius);
        commonBall.setDt(deltaTime);
        return commonBall;
    }

    public static CommonBall buildColoredBall(final int ballNumber, Pair position,
                                              final Double mass, final Double radius, double deltaTime) {
        CommonBall commonBall = new CommonBall(ballNumber, position, new Pair(0., 0.),
                new Pair(0., 0.), new Pair(0., 0.),
                mass, radius);
        commonBall.setDt(deltaTime);
        return commonBall;
    }

    private double taylorEvaluation(double[] derivatives, double dt){
        double value = 0;
        for (int i = 0; i < derivatives.length; i++) {
            value += derivatives[i] * (this.dt_k[i] / factorials[i]);
        }
        return value;
    }

    private static int factorial(int n) {
        int resultado = 1;
        for (int i = 1; i <= n; i++) {
            resultado *= i;
        }
        return resultado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommonBall that = (CommonBall) o;
        return ballNumber == that.ballNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ballNumber);
    }

    @Override
    public int compareTo(CommonBall o) {
        return Integer.compare(this.ballNumber, o.ballNumber);
    }

    public Double getRadius() {
        return radius;
    }
}
