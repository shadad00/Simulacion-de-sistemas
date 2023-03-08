import java.util.List;

public class PeriodicGrid extends NoPeriodicGrid implements Grid{
    public PeriodicGrid(double length, int cellQuantity, double cutoffRadius) {
        super(length, cellQuantity, cutoffRadius);
    }

    @Override
    protected void initializeCell(int dim){
        super.initializeCell(dim + 2); //adding extra rows and columns
    }

    @Override
    protected void saveParticle(Particle particle){
        int particleCol = getColFromParticle(particle) + 1;
        int particleRow = getRowFromParticle(particle) + 1 ;
        createVirtualParticle(particleRow,particleCol,particle);
        cells[particleCol][particleRow].addParticle(particle);
    }


    private void createVirtualParticle(int row , int col, Particle particle){
        int newRow=-1, newCol=-1;
        Particle diagonalVirtualParticle = particle.getVirtualParticle();

        if(row == 1 || row == this.cellQuantity){
            Particle rowVirtualParticle = particle.getVirtualParticle();
            if(row == 1){
                rowVirtualParticle.setY(rowVirtualParticle.getY() - this.length);
                newRow = this.cellQuantity + 1 ;
            }else{
                rowVirtualParticle.setY(rowVirtualParticle.getY() + this.length);
                newRow = 0 ;
            }
            cells[newRow][col].addParticle(rowVirtualParticle);
            diagonalVirtualParticle.setY(rowVirtualParticle.getY());
        }
        if(col == 1 || col == this.cellQuantity){
            Particle columnVirtualParticle = particle.getVirtualParticle();
            if( col == 1 ){
                columnVirtualParticle.setX(columnVirtualParticle.getX() + this.length);
                newCol = this.cellQuantity + 1 ;
            }
            else{
                columnVirtualParticle.setX(columnVirtualParticle.getX() - this.length);
                newCol = 0;
            }
            cells[row][newCol].addParticle(columnVirtualParticle);
            diagonalVirtualParticle.setX(columnVirtualParticle.getX());
        }

        if (newRow >= 0 && newCol >= 0)
            cells[newRow][newCol].addParticle(diagonalVirtualParticle);
    }




}
