import java.security.InvalidParameterException;
import java.util.List;
import java.util.function.Consumer;

public enum Velocity {
    UP_RIGHT(1/2.0,1/2.0),
    RIGHT (1,0),
    DOWN_RIGHT(1/2.0,-1/2.0),
    DOWN_LEFT(-1/2.0,-1/2.0),
    LEFT (-1,0),
    UP_LEFT(-1/2.0,1/2.0);

    private double xComponent;
    private double yComponent;

    public double getxComponent() {
        return xComponent;
    }

    public double getyComponent() {
        return yComponent;
    }

    Velocity(double xComponent, double yComponent) {
        this.xComponent = xComponent;
        this.yComponent = yComponent;
    }

    public Velocity rotateClockwise(int steps) {
        if(steps <= 0 || (steps != 1 && steps != 2 && steps != 3))
            throw new InvalidParameterException("Invalid step provided");
        int valuesLength = Velocity.values().length;
        int currentIndex = this.ordinal();
        int newIndex = (currentIndex + steps) % valuesLength;
        return Velocity.values()[newIndex];
    }

    public Velocity getOppositeVelocity(){
        return rotateClockwise(3);
    }


    static double xMomentum(List<Velocity> velocityList){
        double suma = 0;
        for (Velocity velocity : velocityList) {
            suma += velocity.xComponent;
        }
        return suma;
    }

    static double yMomentum(List<Velocity> velocityList){
        double suma = 0;
        for (Velocity velocity : velocityList) {
            suma += velocity.yComponent;
        }
        return suma;
    }

}
