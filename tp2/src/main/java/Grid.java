public class Grid {
    private final Cell[][] cells;

    public Grid(Cell[][] cells) {
        this.cells = cells;
    }

    public Grid getNextGrid(){
        collide();
        return propagate();
    }

    public Grid propagate(){
        Cell[][] newCell = new Cell[cells.length][cells.length];
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells.length; j++) {
                for (Particle particle : cells[i][j].particleSet) {
                    int newRow = getRowFromVelocity(i,j,particle.getVelocity());
                    int newCol = getColFromVelocity(i,j,particle.getVelocity());
                    if( (0 <= newCol && newCol<= cells.length)
                            && (0 <= newRow && newRow<= cells.length) )
                       newCell[newRow][newCol].addParticle(particle);
                }
            }
        }
        return new Grid(newCell);

    }

    public void collide(){
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells.length; j++) {
                    cells[i][j] = cells[i][j].collide();
            }
        }
    }

    private int getRowFromVelocity(int row, int col, Velocity velocity){
            if(velocity.equals(Velocity.RIGHT) || velocity.equals(Velocity.LEFT))
                return row;
            else if(velocity.equals(Velocity.DOWN_LEFT) || velocity.equals(Velocity.DOWN_RIGHT))
                return row + 1 ;
            else return row - 1;
    }

    private int getColFromVelocity(int row, int col, Velocity velocity){
        boolean par = row % 2 == 0;
        if(velocity.equals(Velocity.RIGHT) )
            return col + 1;
        else if(velocity.equals(Velocity.LEFT) )
            return col - 1;
        else if( (par && velocity.equals(Velocity.UP_RIGHT)) || (!par && velocity.equals(Velocity.DOWN_LEFT)))
            return col ;
        else if((par && velocity.equals(Velocity.UP_LEFT)) || (!par && velocity.equals(Velocity.DOWN_RIGHT)))
            return col + 1;
        throw new RuntimeException("Invalid combination");
    }


}
