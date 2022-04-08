import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class RandomWalkController {

    private final Lappa lappa;
    private final World world;

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
        ArrayList<String> angle = new ArrayList<>();
        int numSteps = 0;

        while (!world.isCovered()) {

            if (numSteps % 10 == 0) {
                double percent = 100 - world.getCoveragePercentage();
                float absAngle = lappa.getAbsoluteMotorMovement();
                coveragePercentage.add(Double.toString(percent));
                angle.add(Float.toString(absAngle));
                System.out.println("Percentage covered: " + percent);
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
        writeResultsToCSV(convertListToArray(coveragePercentage), "coverage_percentage.csv");
        writeResultsToCSV(convertListToArray(angle), "angle.csv");
    }

    private void writeResultsToCSV(String[] data, String path) {
        File file = new File(path);
        try {
            FileWriter output = new FileWriter(file);
            CSVWriter writer = new CSVWriter(output);
            writer.writeNext(data, false);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String[] convertListToArray(ArrayList<String> data) {
        String[] arr = new String[data.size()];
        return data.toArray(arr);
    }
}