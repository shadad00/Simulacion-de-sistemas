import java.util.HashSet;
import java.util.Random;

public class Grid {
    private final Cell[][] cells;
    private final static int dim = 200;
    private int cellGap;

    private int leftParticles = 0;
    private final int totalParticles;

    public Grid(Cell[][] cells, int totalParticles, int cellGap){
        this.cells = cells;
        this.totalParticles = totalParticles;
        this.cellGap = cellGap;
    }

    public Grid(int particleQuantity, int cellGap) {
        this.cells = new Cell[dim][dim];
        this.cellGap = cellGap;
        initializeCell();
        this.totalParticles = particleQuantity;
        Random rng = new Random();
        for (int i = 0; i < particleQuantity; i++) {
            int row, col;
            do {
                row = rng.nextInt(dim-1) + 1 ;
                col = rng.nextInt((((dim-1)/2)-1)) + 1 ;
            } while (!cells[row][col].isEmpty());
            this.cells[row][col].addParticle(new Particle(getRandomVelocity()));

        }
    }

    public Grid getNextGrid(){
        collide();
        return propagate();
    }

    public Grid propagate(){
        Cell[][] newCell = new Cell[cells.length][cells.length];
        for (int i = 0; i < dim; i++)
            for (int j = 0; j < dim; j++)
                newCell[i][j] = getCell(i,j);
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells.length; j++) {
                for (Particle particle : cells[i][j].particleSet) {
                    int newRow = getRowFromVelocity(i,j,particle.getVelocity());
                    int newCol = getColFromVelocity(i,j,particle.getVelocity());
                    if( (0 <= newCol && newCol< cells.length)
                            && (0 <= newRow && newRow< cells.length) ){
                        newCell[newRow][newCol].addParticle(particle);
                        if(newCol < dim / 2)
                            this.leftParticles++;
                    }
                }
            }
        }
        return new Grid(newCell, totalParticles, cellGap);
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
        if( velocity.equals(Velocity.RIGHT) ||
                (!par && velocity.equals(Velocity.DOWN_RIGHT)) ||
                (!par && (velocity.equals(Velocity.UP_RIGHT))))
            return col + 1;
        else if(velocity.equals(Velocity.LEFT) ||
                (par && velocity.equals(Velocity.DOWN_LEFT)) ||
                (par && velocity.equals(Velocity.UP_LEFT)))
            return col - 1;
        else if( (par && velocity.equals(Velocity.UP_RIGHT)) ||
                (!par && velocity.equals(Velocity.DOWN_LEFT)) ||
                (par && velocity.equals(Velocity.DOWN_RIGHT) ||
                        (!par && velocity.equals(Velocity.UP_LEFT))))
            return col ;
        throw new RuntimeException("Invalid combination");
    }

    private Velocity getRandomVelocity(){
        Velocity[] velocities = Velocity.values();
        int index = new Random().nextInt(velocities.length);
        return velocities[index];
    }

    public int getLeftParticles() {
        int left = 0;
        for (int i = 0; i < this.cells.length; i++) {
            for (int j = 0; j < this.cells.length; j++) {
                if(j < dim / 2)
                    left += this.cells[i][j].particleQuantity();
            }

        }
        return left;
//        return this.leftParticles;
    }

    public int getRightParticles(){
        return totalParticles - getLeftParticles();
    }


    private Cell getCell(int row ,int col ){
        int limit = (this.cells.length - cellGap) / 2;
        if(( row == 0 || col == 0 || row == dim - 1 || col == dim-1))
            return new SolidCell();
        else if ( (col == dim / 2) && (!( row > limit && row < dim-limit)))
            return new SolidCell();
        else
            return new CommonCell();
    }

    private void initializeCell(){
        for (int i = 0; i < this.cells.length; i++)
            for (int j = 0; j < this.cells.length; j++)
                this.cells[i][j] = getCell(i,j);
    }


}
