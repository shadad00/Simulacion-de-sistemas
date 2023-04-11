package simulation;

public interface Collisionable<T extends Number> {
    Pair<T> getPosition();
    void updatePosition(T time);
    Pair<T> getVelocity();
    int getTotalCollisions();
    T getRadius();
    boolean isPocket();
}
