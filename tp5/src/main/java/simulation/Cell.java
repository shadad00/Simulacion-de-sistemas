package simulation;


import java.util.HashSet;
import java.util.Set;

public class Cell {

    private int row ;
    private int col;
    private Set<CommonBall> balls;

    public Cell(final int row, final int col) {
        this.row = row;
        this.col = col;
        this.balls = new HashSet<>();
    }

    public void addBall(final CommonBall ball){
        this.balls.add(ball);
    }

    public boolean isEmpty(){
        return balls.isEmpty();
    }

    public Set<CommonBall> getBalls() {
        return balls;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    @Override
    public String toString() {
        return "Cell{" +
                "row=" + row +
                ", col=" + col +
                '}';
    }
}
