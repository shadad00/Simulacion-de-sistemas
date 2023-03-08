public class Particle {

    private static int particleId = 0;
    private final int cellId;
    private double x;
    private double y;
    private final double radius;
    private final double cutOffRadius;


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
        return Math.sqrt(
                (Math.pow(this.x - other.x,2) + Math.pow(this.y - other.y, 2))
        );
    }

    public double getCutOffRadius() {
        return cutOffRadius;
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
