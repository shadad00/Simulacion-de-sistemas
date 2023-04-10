public interface Collisionable<T extends Number> {
    Pair<T> getPosition();
    void updatePosition(T time);
    Pair<T> getVelocity();
    T getRadius();
    void collide(Collisionable other);
    boolean isPocket();

    T getTimeToPossibleCollide(Collisionable other);
}
