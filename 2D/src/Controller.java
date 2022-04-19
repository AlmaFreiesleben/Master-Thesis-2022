import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    protected final Lappa lappa;
    protected final World world;

    // Used to record results
    protected List<String[]> coverageResults;
    protected List<String[]> timeResults;

    public Controller(Lappa lappa, World world) {
        this.lappa = lappa;
        this.world = world;
        coverageResults = new ArrayList<>();
        timeResults = new ArrayList<>();
    }

    public void writeToFiles() {
        writeResultsToCSV(coverageResults, "coverage_percentage.csv");
        writeResultsToCSV(timeResults, "time.csv");
    }

    protected void writeResultsToCSV(List<String[]> data, String path) {
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

    protected String[] convertListToArray(ArrayList<String> data) {
        String[] arr = new String[data.size()];
        return data.toArray(arr);
    }

    protected float convertAbsAngleToTimeInMinutes(float absAngle, int numSteps) {
        return (numSteps * 3 + absAngle * 1/2) / 60;
    }
}
