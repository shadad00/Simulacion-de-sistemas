import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


public class CollisionTest {

    SolidCell solidCell;
    CommonCell commonCell;

    static Velocity v_upright;
    static Velocity v_right;
    static Velocity v_downright;
    static Velocity v_downleft;
    static Velocity v_left;
    static Velocity v_upleft;

    static Particle p_upright;
    static Particle p_right;
    static Particle p_downright;
    static Particle p_downleft;
    static Particle p_left;
    static Particle p_upleft;

    @BeforeAll
    static void setup() {
        System.out.println("@BeforeAll");

        System.out.println("Creating particles...");
        p_upright = new Particle(Velocity.UP_RIGHT);
        p_right = new Particle(Velocity.RIGHT);
        p_downright = new Particle(Velocity.DOWN_RIGHT);
        p_downleft = new Particle(Velocity.DOWN_LEFT);
        p_left = new Particle(Velocity.LEFT);
        p_upleft = new Particle(Velocity.UP_LEFT);

        v_upright = Velocity.UP_RIGHT;
        v_right = Velocity.RIGHT;
        v_downright = Velocity.DOWN_RIGHT;
        v_downleft = Velocity.DOWN_LEFT;
        v_left = Velocity.LEFT;
        v_upleft = Velocity.UP_LEFT;
    }
    @BeforeEach
    void init() {
        System.out.println("@BeforeEach");

        System.out.println("Creating instances of cells...");
        solidCell = new SolidCell();
        commonCell = new CommonCell();

    }


    @Test
    void doubleCollision() {
        Velocity[] velocitiesValues = Velocity.values();
        int vsize = velocitiesValues.length;
        for (int i = 0; i < 3; i++) {
            int j = i + 3;

            commonCell.addParticle(new Particle(velocitiesValues[i]));
            commonCell.addParticle(new Particle(velocitiesValues[j]));

            Cell newCell = commonCell.collide();
            Set<Velocity> newVelocities = commonCell.getVelocities();

            if (i == 0) {
                assertTrue(
                        (newVelocities.contains(velocitiesValues[(i+1) % vsize]) &&
                                newVelocities.contains(velocitiesValues[(j+1) % vsize]) ) ||
                                (newVelocities.contains(velocitiesValues[vsize - 1]) &&
                                        newVelocities.contains(velocitiesValues[(j-1) % vsize]))

                );
            } else {
                assertTrue(
                        (newVelocities.contains(velocitiesValues[(i+1) % vsize]) &&
                                newVelocities.contains(velocitiesValues[(j+1) % vsize]) ) ||
                                (newVelocities.contains(velocitiesValues[(i-1) % vsize]) &&
                                        newVelocities.contains(velocitiesValues[(j-1) % vsize]))

                );
            }
            commonCell.particleSet.clear();
        }
    }

    @Test
    void tripleCollision() {

    }

    @Test
    void cuadrupleCollision () {

    }

    @Test
    void quintupleCollision() {
        commonCell.addAllParticles(List.of(new Particle[]{p_left, p_right, p_upleft, p_upright, p_downright}));
        Cell newCell = commonCell.collide();
        assertTrue(newCell.getParticles().containsAll(List.of(new Particle[]{p_left, p_right, p_upleft, p_upright, p_downright})));
    }

    @Test
    void sextupleCollision() {
        commonCell.addAllParticles(List.of(new Particle[]{p_left, p_right, p_upleft, p_upright, p_downright, p_downleft}));
        Cell newCell = commonCell.collide();
        assertTrue(newCell.getParticles().containsAll(List.of(new Particle[]{p_left, p_right, p_upleft, p_upright, p_downright, p_downleft})));
    }

    @DisplayName("Testing solid collisions")
    @Test
    void solidCollision() {
        for(Velocity cvel : Velocity.values()) {
            Cell solidCell = new SolidCell();
            Velocity vel = cvel;
            Particle particle = new Particle(vel);
            solidCell.addParticle(particle);
            Cell newcell = solidCell.collide();
            Velocity newvel = newcell.getParticles().stream().findFirst().get().getVelocity();
            switch (vel) {
                case LEFT: assertEquals(newvel, Velocity.RIGHT);
                    break;
                case RIGHT: assertEquals(newvel, Velocity.LEFT);
                    break;
                case DOWN_LEFT: assertEquals(newvel, Velocity.UP_RIGHT);
                    break;
                case DOWN_RIGHT: assertEquals(newvel, Velocity.UP_LEFT);
                    break;
                case UP_LEFT: assertEquals(newvel, Velocity.DOWN_RIGHT);
                    break;
                case UP_RIGHT: assertEquals(newvel, Velocity.DOWN_LEFT);
                    break;
            }
        }
    }

}
