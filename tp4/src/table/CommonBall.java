package table;

import lombok.Getter;
import lombok.Setter;
import utils.Pair;

import java.util.Arrays;

import static java.lang.Math.pow;

@Getter
@Setter
public class CommonBall extends Ball implements Comparable<CommonBall> {
    private static final double k = Math.pow(10, 2); // TODO: revisar si las unidades estan bien N/m
    private final int ballNumber;
    private final double[] multipliers = {3. / 20, 251. / 360, 1, 11. / 18, 1. / 6, 1. / 60};
    private double[][] position_derivatives = null;


    public CommonBall(CommonBall other){
        super();
        this.position = new Pair(other.position.getX(), other.getPosition().getY());
        this.velocity = new Pair(other.velocity.getX(), other.velocity.getY());
        this.acceleration = new Pair(other.acceleration.getX(), other.acceleration.getY());
        this.force = new Pair(other.force.getX(), other.force.getY());
        this.mass = other.mass;
        this.radius = other.radius;
        this.ballNumber = other.ballNumber;
        this.position_derivatives = other.getPosition_derivatives();
    }
    
    public CommonBall(final int ballNumber, Pair position,
                      Pair velocity,Pair acceleration ,Pair force,
                      final Double mass, final Double radius) {
        super(position, velocity, acceleration, force,mass, radius);
        this.ballNumber = ballNumber;
        this.position_derivatives = new double[2][];
        position_derivatives[0] = new double[]
                {position.getX(), velocity.getX(),acceleration.getX(),0.,0.};
        position_derivatives[1] = new double[]
                {position.getY(), velocity.getY(),acceleration.getY(),0.,0.};

    }

    @Override
    public void updatePosition(Double dt) {
        double[][] derivative_predictions = new double[2][5];
        for (int i = 0; i < position_derivatives.length; i++) {
            for (int j = 0; j < position_derivatives[i].length; j++) {
                derivative_predictions[i][j] = taylorEvaluation(
                        Arrays.copyOfRange(position_derivatives[i],j,
                                position_derivatives[i].length),dt);
            }
        }

        double drx2 = (force.getX() / mass)  - derivative_predictions[0][2];
        double R2x = drx2 * pow(dt, 2) / 2;
        double dry2 = (force.getY() / mass)  - derivative_predictions[1][2];
        double R2y = dry2 * pow(dt, 2) / 2;
        double[] rectifier = {R2x, R2y};


        for (int i = 0; i < derivative_predictions.length; i++) {
            for (int j = 0; j < derivative_predictions[i].length; j++) {
                int jFact = factorial(j);
                derivative_predictions[i][j] += multipliers[j] * rectifier[i] * jFact / pow(dt,j);
            }
        }

        position_derivatives[0] = derivative_predictions[0];
        position_derivatives[1] = derivative_predictions[1];

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
        if (position.getX() + getRadius() < wallX)
            return Pair.ZERO;
        return Pair.of(-k, 0);
    }

    public Pair forceBetweenLeftWall() {
        if (position.getX() - getRadius() > 0)
            return Pair.ZERO;
        return Pair.of(k, 0);
    }

    public Pair forceBetweenTopWall(double wallY) {
        if (position.getY() + getRadius() < wallY)
            return Pair.ZERO;
        return Pair.of(0, -k);
    }

    public Pair forceBetweenBottomWall() {
        if ( position.getY() - getRadius() > 0 )
            return Pair.ZERO;
        return Pair.of(0, k);
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


    public static CommonBall buildWhiteBall(Pair position, Pair velocity, final Double mass, final Double radius) {
        return new CommonBall(0, position, velocity,
                new Pair(0., 0.),new Pair(0., 0.),
                mass, radius);
    }

    public static CommonBall buildColoredBall(final int ballNumber, Pair position, final Double mass, final Double radius) {
        return new CommonBall(ballNumber, position, new Pair(0., 0.),
                new Pair(0., 0.),new Pair(0., 0.),
                mass, radius);
    }

    private double taylorEvaluation(double[] derivatives, double dt){
        double value = 0;
        for (int i = 0; i < derivatives.length; i++) {
            value += derivatives[i] * (Math.pow(dt,i) / factorial(i));
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
    public int compareTo(CommonBall o) {
        return Integer.compare(this.ballNumber, o.ballNumber);
    }
}
