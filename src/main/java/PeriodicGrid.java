import java.util.List;

public class PeriodicGrid extends NoPeriodicGrid{
    public PeriodicGrid(double length, int cellQuantity, double cutoffRadius) {
        super(length, cellQuantity, cutoffRadius);
    }

    @Override
    protected void initializeCell(int dim){
        dim = dim + 2 ;
        super.initializeCell(dim);
    }

    @Override
    protected void saveParticle(Particle particle){
        int particleCol = getColFromParticle(particle) + 1;
        int particleRow = getRowFromParticle(particle) + 1 ;
        createVirtualParticle(particleRow,particleCol,particle);
        cells[particleCol][particleRow].addParticle(particle);
    }


    private void createVirtualParticle(int row , int col, Particle particle){
        Particle diagonalVirtualParticle = particle.getVirtualParticle();
        Particle columnVirtualParticle = particle.getVirtualParticle();
        Particle rowVirtualParticle = particle.getVirtualParticle();
        int newRow = row;
        int newCol = col;

        if(row == 1){ //from first to last row
            rowVirtualParticle.setY(rowVirtualParticle.getY() - this.length);
            newRow = this.cellQuantity + 1 ;
            cells[newRow][col].addParticle(rowVirtualParticle);
            diagonalVirtualParticle.setY(rowVirtualParticle.getY());
        }
        else if(row == this.cellQuantity) {//from last to first row
            rowVirtualParticle.setY(rowVirtualParticle.getY() + this.length);
            newRow = 0 ;
            cells[newRow][col].addParticle(rowVirtualParticle);
            diagonalVirtualParticle.setY(rowVirtualParticle.getY());
        }
        if( col == 1 ){
            columnVirtualParticle.setX(columnVirtualParticle.getX() + this.length);
            newCol = this.cellQuantity + 1 ;
            cells[row][newCol].addParticle(columnVirtualParticle);
            diagonalVirtualParticle.setX(columnVirtualParticle.getX());
        }
        else if (col == this.cellQuantity ){
            columnVirtualParticle.setX(columnVirtualParticle.getX() - this.length);
            newCol = 0;
            cells[row][newCol].addParticle(columnVirtualParticle);
            diagonalVirtualParticle.setX(columnVirtualParticle.getX());
        }
        if (!(particle.getX() == diagonalVirtualParticle.getX() && particle.getY() == diagonalVirtualParticle.getY()))
            cells[newRow][newCol].addParticle(diagonalVirtualParticle);
    }

}
