import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class RandomWalkController {

    private final Lappa lappa;
    private final World world;

    // Used to record results
    private List<String[]> randomWalkCoverageResults = new ArrayList<>();
    private List<String[]> randomWalkTimeResults = new ArrayList<>();

    public RandomWalkController(Lappa lappa, World world) {
        this.lappa = lappa;
        this.world = world;
    }

    public void randomWalk() {
        while (!world.isCovered()) {
            float motor = new Random().nextInt(361) - 180;

            if (lappa.getIsRedFixed()) {
                lappa.step(motor);
            } else {
                lappa.step(motor);
            }
        }
        System.out.println("World is covered");
    }

    public void randomWalkRecordResult() {
        ArrayList<String> coveragePercentage = new ArrayList<>();
        ArrayList<String> time = new ArrayList<>();
        int numSteps = 0;

        while (!world.isCovered()) {

            if (numSteps % 10 == 0) {
                double percent = world.getCoveragePercentage();
                float absAngle = lappa.getAbsoluteMotorMovement();
                float t = convertAbsAngleToTimeInMinutes(absAngle, numSteps);
                coveragePercentage.add(Double.toString(percent));
                time.add(Float.toString(t));
            }

            float motor = new Random().nextInt(361) - 180;

            if (lappa.getIsRedFixed()) {
                lappa.stepWithoutSim(motor);
            } else {
                lappa.stepWithoutSim(motor);
            }

            numSteps++;
        }

        System.out.println("World is covered");
        randomWalkCoverageResults.add(convertListToArray(coveragePercentage));
        randomWalkTimeResults.add(convertListToArray(time));
    }

    public void writeToFiles() {
        writeResultsToCSV(randomWalkCoverageResults, "coverage_percentage.csv");
        writeResultsToCSV(randomWalkTimeResults, "time.csv");
    }

    private void writeResultsToCSV(List<String[]> data, String path) {
        File file = new File(path);
        try {
            FileWriter output = new FileWriter(file);
            CSVWriter writer = new CSVWriter(output);
            writer.writeAll(data, false);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String[] convertListToArray(ArrayList<String> data) {
        String[] arr = new String[data.size()];
        return data.toArray(arr);
    }

    private float convertAbsAngleToTimeInMinutes(float absAngle, int numSteps) {
        return (numSteps * 3 + absAngle * 1/2) / 60;
    }
}