import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CommonCell extends Cell{
    private int random;

    public CommonCell() {
        super();
        this.random = Math.random() >= 0.5 ? 0 : 1;
    }

    public CommonCell(Set<Particle> particles) {
        super(particles);
        this.random = Math.random() >= 0.5 ? 0 : 1;
    }

    @Override
    public Cell collide() {
        List<Velocity> velocityList = this.particleSet.stream().map(particle -> particle.getVelocity()).collect(Collectors.toList());
        double xMomentum = Velocity.xMomentum(velocityList);
        double yMomentum = Velocity.yMomentum(velocityList);
        int size = this.particleSet.size();
        if(xMomentum + yMomentum != 0 || size == 6)
            return new CommonCell(this.particleSet);
        int random = size == 3 ? 0 : this.random;
        return new CommonCell(rotateParticles(1 + random));
    }

}
