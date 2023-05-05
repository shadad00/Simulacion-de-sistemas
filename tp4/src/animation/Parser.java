package animation;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import table.CommonBall;
import table.Table;
import utils.Pair;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Parser implements Iterable<Table>{

    private final CSVReader csvReader;
    private final double width = 224;
    private final double height = 112;
    private int currentIteration = 0;
    private final boolean pockets;


    public Table getNextTable() throws IOException, CsvValidationException {
        List<ParsedLine> parsedLineList = new ArrayList<>();

        while (csvReader.peek() != null && csvReader.peek()[0].compareTo(String.valueOf(currentIteration)) == 0) {
            parsedLineList.add(new ParsedLine(csvReader.readNext()));
        }
        this.currentIteration++;

        ParsedLine any = parsedLineList.stream().findAny()
                .orElseThrow(RuntimeException::new);

        Set<CommonBall> ballSet = new HashSet<>();
        for(ParsedLine line : parsedLineList){
           ballSet.add(new CommonBall(line.ballId, new Pair(line.xPosition,line.yPosition)
                   ,new Pair(line.xVelocity,line.yVelocity),
                   new Pair(line.xForce / line.ballMass,line.yForce / line.ballMass),
                   new Pair(line.xForce,line.yForce), line.ballMass,
                   line.ballRadius));
        }

        Table newTable = new Table(ballSet, width, height, any.time, any.iteration);
        if(this.pockets)
            newTable.positionPockets();
        return newTable;
    }


    public Parser(final String filename, boolean pockets) {
        try {
            System.out.println(filename);
            FileReader fileReader = new FileReader(filename);
            this.csvReader = new CSVReader(fileReader);
            this.pockets = pockets;
            csvReader.readNext();
        }catch (CsvValidationException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterator<Table> iterator() {
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                try {
                    return csvReader.peek() != null;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public Table next() {
                try {
                    return getNextTable();
                } catch (IOException | CsvValidationException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }


    static class ParsedLine{
        private int iteration;
        private double time;
        private int ballId;
        private double xPosition;
        private double yPosition;
        private double xVelocity;
        private double yVelocity;
        private double ballRadius;
        private double ballMass;
        private double xForce ;
        private double yForce ;

        public ParsedLine(String[] tokens){
            parseTokens(tokens);
        }

        private void parseTokens(String[] tokens) {
            this.iteration = Integer.parseInt(tokens[0]);
            this.time = Double.parseDouble(tokens[1]);
            this.ballId = Integer.parseInt(tokens[2]);
            this.xPosition = Double.parseDouble(tokens[3]);
            this.yPosition = Double.parseDouble(tokens[4]);
            this.xVelocity = Double.parseDouble(tokens[5]);
            this.yVelocity = Double.parseDouble(tokens[6]);
            this.ballRadius = Double.parseDouble(tokens[7]);
            this.ballMass = Double.parseDouble(tokens[8]);
            this.xForce = Double.parseDouble(tokens[9]);
            this.yForce = Double.parseDouble(tokens[10]);
        }

    }




}
