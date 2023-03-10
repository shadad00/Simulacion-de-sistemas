import java.util.Objects;

public class Particle {

    private static int particleId = 0;
    private final int cellId;
    private double x;
    private double y;
    private final double radius;
    private final double cutOffRadius;

    public Particle(double radius, double cutOffRadius){
        this.cellId = particleId++;
        this.radius = radius ;
        this.cutOffRadius = cutOffRadius;
    }

    public Particle( double x, double y, double radius, double cutOffRadius) {
        this.cellId = particleId++;
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.cutOffRadius = cutOffRadius;
    }
    private Particle( int cellId, double x, double y, double radius, double cutOffRadius) {
        this.cellId = cellId;
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.cutOffRadius = cutOffRadius;
    }



    public Particle getVirtualParticle(){
        return new Particle(this.getCellId(),this.getX(), this.getY(),this.getRadius(),this.getCutOffRadius());
    }



    public int getCellId() {
        return cellId;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getRadius() {
        return radius;
    }


    public double distanceTo(Particle other) {
        double distance = Math.sqrt(
                (Math.pow(this.x - other.x,2) + Math.pow(this.y - other.y, 2))) - radius - other.radius;
        return distance >= 0 ? distance : 0 ;
    }

    public double getCutOffRadius() {
        return cutOffRadius;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Particle particle = (Particle) o;
        return cellId == particle.cellId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cellId);
    }

    @Override
    public String toString() {
        return "Particle{" +
                "cellId=" + cellId +
                ", x=" + x +
                ", y=" + y +
                '}';
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
}
