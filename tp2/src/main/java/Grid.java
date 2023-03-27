import java.util.List;
import java.util.Random;

public class Grid {
    private final Cell[][] cells;
    private final static int dim = 200;
    private int cellGap = 50;
    private double epsilonEq = 0.05;
    private int totalParticles;


    public Grid(List<Parser.ParsedLine> parsedLineList, int cellGap, int totalParticles){
        this.cells = new Cell[dim][dim];
        initializeCell();
        this.totalParticles = totalParticles ;
        this.cellGap = cellGap;
        for (Parser.ParsedLine pl : parsedLineList) {
            this.cells[pl.getRow()][pl.getCol()].addAllParticles(pl.getParticleSet());
        }
    }

    public int getTotalParticles() {
        return totalParticles;
    }

    public Grid(Cell[][] cells, int totalParticles, int cellGap, double epsilonEq){
        this.cells = cells;
        this.totalParticles = totalParticles;
        this.cellGap = cellGap;
        this.epsilonEq = epsilonEq;
    }

    public Grid(int particleQuantity, int cellGap,double epsilonEq) {
        this.cells = new Cell[dim][dim];
        this.cellGap = cellGap;
        this.epsilonEq = epsilonEq;
        initializeCell();
        this.totalParticles = particleQuantity;
        Random rng = new Random();


        for (int i = 0; i < particleQuantity; i++) {
            int row, col;
            do {
                row = rng.nextInt(dim-2) + 1;
                col = rng.nextInt((dim/2) - 2) + 1;
            } while (!cells[row][col].isEmpty() && !cells[row][col].isSolid());
            this.cells[row][col].addParticle(new Particle(getRandomVelocity()));
        }
    }

    public boolean isEquilibrated() {
        final double eq = getLeftParticles() / (double) totalParticles;
        return (eq > (0.5 - epsilonEq) && eq < (0.5 + epsilonEq));
    }

    public Grid getNextGrid(){
        collide();
        return propagate();
    }


    public Cell[][] getCells() {
        return cells;
    }

    public Grid propagate(){
        Cell[][] newCell = new Cell[cells.length][cells.length];
        for (int i = 0; i < dim; i++)
            for (int j = 0; j < dim; j++)
                newCell[i][j] = getCell(i,j);
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells.length; j++) {
                for (Particle particle : cells[i][j].particleSet) {
                    int newRow = getRowFromVelocity(i,particle.getVelocity());
                    int newCol = getColFromVelocity(i,j,particle.getVelocity());
                    if( (0 <= newCol && newCol < cells.length) && (0 <= newRow && newRow < cells.length) )
                        newCell[newRow][newCol].addParticle(particle);

                }
            }
        }
        return new Grid(newCell, totalParticles, cellGap,epsilonEq);
    }

    public void collide(){
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells.length; j++) {
                if(!cells[i][j].isEmpty())
                    cells[i][j] = cells[i][j].collide();
            }
        }
    }

    public static final double HEXAGONAL_GRID_VERTICAL_SPACING = Math.sqrt(3) / 2;

    public void hexagonalIteration(HexagonalConsumer consumer) {
        for (int row = 0; row < cells.length; row++) {
            for (int col = 0; col < cells.length; col++) {

                final Cell cell = cells[row][col];
                final boolean isEvenRow = (row % 2 == 0);
                final double xCell = col + (isEvenRow ? 0 : 0.5);
                final double yCell = row * HEXAGONAL_GRID_VERTICAL_SPACING;

                consumer.forEachCell(cell, xCell, yCell);
            }
        }
    }

    private int getRowFromVelocity(int row, Velocity velocity){
            if(velocity.equals(Velocity.RIGHT) || velocity.equals(Velocity.LEFT))
                return row;
            else if(velocity.equals(Velocity.DOWN_LEFT) || velocity.equals(Velocity.DOWN_RIGHT))
                return row + 1 ;
            else return row - 1;
    }

    private int getColFromVelocity(int row, int col, Velocity velocity){
        boolean par = row % 2 == 0;
        if (par) {
            switch (velocity) {
                case UP_RIGHT:
                case DOWN_RIGHT:
                    return col;
                case UP_LEFT:
                case DOWN_LEFT:
                case LEFT:
                    return col - 1;
                case RIGHT:
                    return col + 1;
            }
        } else {
            switch (velocity) {
                case UP_RIGHT:
                case DOWN_RIGHT:
                case RIGHT:
                    return col + 1;
                case UP_LEFT:
                case DOWN_LEFT:
                    return col;
                case LEFT:
                    return col - 1;
            }
        }
        throw new RuntimeException("Invalid combination");
    }

    private Velocity getRandomVelocity(){
        Velocity[] velocities = Velocity.values();
        int index = new Random().nextInt(velocities.length);
        return velocities[index];
    }

    public int getLeftParticles() {
        int left = 0;
        for (Cell[] cell : cells) {
            for (int j = 0; j < (dim / 2); j++) {
                left += cell[j].particleQuantity();
            }
        }
        return left;
    }



    private Cell getCell(int row, int col ){
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
