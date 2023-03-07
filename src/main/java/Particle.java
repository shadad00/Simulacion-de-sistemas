import java.util.Random;

public class Particle {
    private int cellId;
    private double x;
    private double y;
    private double radius;
    private double cutOffRadius;

    public Particle(int cellId,double x, double y, double radius, double cutOffRadius) {
        this.cellId = cellId;
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.cutOffRadius = cutOffRadius;
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

    public Particle(int cellId, double cutOffRadius){
        this(cellId,0,10,0,cutOffRadius);
    }

    public double distanceTo(Particle other) {
        return Math.sqrt(
                (Math.pow(this.x - other.x,2) + Math.pow(this.y - other.y, 2))
        );
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Particle other = (Particle) o;
        return other.getCellId() == this.cellId; //todo: if same position ???
    }
}
