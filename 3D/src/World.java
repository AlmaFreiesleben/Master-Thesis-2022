import javafx.geometry.Point3D;
import java.util.ArrayList;
import java.util.HashMap;

public class World {

    private double chamberSize;
    private ArrayList<Point3D> pointsToCover;
    private ArrayList<Point3D> unCoveredPoints;
    private HashMap<Character, Point3D> entries;
    private HashMap<Character, ArrayList<Point3D>> coverage;

    public World(double radius, double chamberSize) {
        this.chamberSize = chamberSize;
        samplePointsToCover(radius);
        System.out.println(unCoveredPoints.size());
    }

    private void samplePointsToCover(double radius) {
        pointsToCover = new ArrayList<>();
        unCoveredPoints = new ArrayList<>();

        double x, y, z;
        int samples = 200;
        double phi = Math.PI * (3. - Math.sqrt(5.));

        for (int i = 0; i < samples ; i++) {
            y = 1 - (i / (float) (samples - 1)) * 2;
            double r = Math.sqrt(1 - y * y);

            double theta = phi * i;

            x = Math.cos(theta) * r;
            z = Math.sin(theta) * r;

            x *= radius;
            y *= radius;
            z *= radius;

            if (z >= 0) {
                pointsToCover.add(new Point3D(x, y, z));
                unCoveredPoints.add(new Point3D(x, y, z));
            }
        }
    }

    private void initEntriesAndCoverage() {
        coverage = new HashMap<>();
        coverage.put('A', new ArrayList<>());
        coverage.put('B', new ArrayList<>());
        coverage.put('C', new ArrayList<>());
        coverage.put('D', new ArrayList<>());

        entries = new HashMap<>();
        entries.put('A', new Point3D(0, 2.5,0));
        entries.put('B', new Point3D(2.5,0,0));
        entries.put('C', new Point3D(0,-2.5,0));
        entries.put('D', new Point3D(-2.5,0,0));
    }

    public Point3D getEntry(char cleaningZone) {
        return entries.get(cleaningZone);
    }

    public boolean isCleaningZoneCovered(char zone) { // TODO cover sphere with hexagons
        ArrayList<Point3D> zoneCoverage = coverage.get(zone);
        return zoneCoverage.size() >= 10;
    }

    public void updateCoverage(char currentCleaningZone, Point3D p) {
        coverage.get(currentCleaningZone).add(p);
    }

    public boolean isAllCleaningZonesCovered(char currentCleaningZone) {
        return currentCleaningZone == 'D' && isCleaningZoneCovered('D');
    }
}
