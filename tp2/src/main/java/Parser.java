import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Parser implements Iterable<Grid> {
    private int currentIteration = 0;
    private final static int MAX_ITERATION= 3000;
    private CSVReader csvReader;
    private final int cellGap;
    private final int totalParticles;

    public Grid getNextGrid() throws IOException, CsvValidationException{
        List<ParsedLine> parsedLineList = new ArrayList<>();

        while (csvReader.peek() != null && csvReader.peek()[0].compareTo(String.valueOf(currentIteration)) == 0) {
            parsedLineList.add(new ParsedLine(csvReader.readNext()));
        }

        this.currentIteration++;
        return new Grid(parsedLineList, cellGap, totalParticles);
    }

    public int getCurrentIteration(){
        return currentIteration;
    }


    public Parser(final String filename) {
        String[] fileData = filename.split("_");
        this.cellGap = Integer.parseInt(fileData[1].substring(1));
        this.totalParticles = Integer.parseInt(fileData[2].substring(1));
        try {
            FileReader fileReader = new FileReader(filename);
            this.csvReader = new CSVReader(fileReader);
            csvReader.readNext();
        } catch (FileNotFoundException nsf) {
            nsf.printStackTrace();
        } catch (CsvValidationException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterator<Grid> iterator() {
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                    return currentIteration < MAX_ITERATION;
            }

            @Override
            public Grid next() {
                try {
                    return getNextGrid();
                } catch (IOException | CsvValidationException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    static class ParsedLine {
        private final static int HEADER_LENGTH = 9;
        private int iter;
        private int row;
        private int col;
        private Set<Particle> particleSet;

        private void parseTokens(String[] tokens) {
            this.iter = Integer.parseInt(tokens[0]);
            this.row = Integer.parseInt(tokens[1]);
            this.col = Integer.parseInt(tokens[2]);
            this.particleSet = new HashSet<>();
            for (int i = 3; i < HEADER_LENGTH; i++) {
                if (tokens[i].compareTo("1") == 0) {
                    this.particleSet.add(new Particle(Velocity.values()[i-3]));
                }
            }
        }

        public ParsedLine(String [] tokens) {
            parseTokens(tokens);
        }

        public int getIter() {
            return iter;
        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }

        public Set<Particle> getParticleSet() {
            return particleSet;
        }
    }
}
