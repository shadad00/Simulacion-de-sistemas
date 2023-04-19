package animation;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import simulation.CommonBall;
import simulation.Pair;
import simulation.Table;

public class Parser implements Iterable<Table>{

    private final CSVReader csvReader;
    private final float width = 224;
    private final float height = 112;
    private int currentIteration = 0;

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
           ballSet.add(new CommonBall(line.ballId, new Pair<>(line.xPosition,line.yPosition)
                   ,new Pair<>(line.xVelocity,line.yVelocity), line.ballMass,
                   line.ballRadius, line.collisionCount));
        }

        return new Table(ballSet, width, height, any.time, any.iteration);
    }


    public Parser(final String filename) {
        try {
            System.out.println(filename);
            FileReader fileReader = new FileReader(filename);
            this.csvReader = new CSVReader(fileReader);
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
        private float time;
        private int ballId;
        private float xPosition;
        private float yPosition;
        private float xVelocity;
        private float yVelocity;
        private float ballRadius;
        private float ballMass;
        private int collisionCount;

        public ParsedLine(String[] tokens){
            parseTokens(tokens);
        }

        private void parseTokens(String[] tokens) {
            this.iteration = Integer.parseInt(tokens[0]);
            this.time = Float.parseFloat(tokens[1]);
            this.ballId = Integer.parseInt(tokens[2]);
            this.xPosition = Float.parseFloat(tokens[3]);
            this.yPosition = Float.parseFloat(tokens[4]);
            this.xVelocity = Float.parseFloat(tokens[5]);
            this.yVelocity = Float.parseFloat(tokens[6]);
            this.ballRadius = Float.parseFloat(tokens[7]);
            this.ballMass = Float.parseFloat(tokens[8]);
            this.collisionCount = Integer.parseInt(tokens[9]);
        }

    }




}
