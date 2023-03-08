import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    private double spaceLong;
    private int particleNumber;
    private List<Particle> particleList;

    public Parser(String pathToStatic, String pathToDynamic) {
        parseStatic(pathToStatic);
        parseDynamic(pathToDynamic);
    }

    private void parseStatic(String pathToStatic){
        try (BufferedReader br = new BufferedReader(new FileReader(pathToStatic))) {
            this.particleNumber= Integer.parseInt(br.readLine().trim());
            this.spaceLong = Double.parseDouble(br.readLine().trim());
            this.particleList = new ArrayList<>(this.particleNumber);
            for (int i = 0; i < this.particleNumber; i++) {
                String[] line = br.readLine().trim().split(" ");
                Particle newParticle = new Particle(Double.parseDouble(line[0]),Double.parseDouble(line[4]));
                particleList.add(i,newParticle);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void parseDynamic(String pathToDynamic){
        try (BufferedReader br = new BufferedReader(new FileReader(pathToDynamic))) {
            for (int i = 0; i < this.particleNumber; i++) {
                String[] line = br.readLine().trim().split(" ");
                this.particleList.get(i).setX(Double.parseDouble(line[0]));
                this.particleList.get(i).setY(Double.parseDouble(line[3]));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public double getSpaceLong() {
        return spaceLong;
    }

    public void setSpaceLong(double spaceLong) {
        this.spaceLong = spaceLong;
    }

    public int getParticleNumber() {
        return particleNumber;
    }

    public void setParticleNumber(int particleNumber) {
        this.particleNumber = particleNumber;
    }

    public List<Particle> getParticleList() {
        return particleList;
    }

    public void setParticleList(List<Particle> particleList) {
        this.particleList = particleList;
    }
}
