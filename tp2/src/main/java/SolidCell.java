import java.util.HashSet;
import java.util.Set;

public class SolidCell extends Cell {

    public SolidCell(Set<Particle> particles) {
        super(particles);
    }

    @Override
    public boolean isSolid() {
        return true;
    }

    @Override
    public Cell collide() {
        return new SolidCell(rotateParticles(3));
    }

}
