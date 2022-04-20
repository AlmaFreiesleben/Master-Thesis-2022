import javafx.geometry.Point3D;
import javafx.scene.shape.Sphere;
import java.util.ArrayList;
import java.util.HashMap;

public class World {

    //private Sphere sphere;
    private double chamberSize;
    private HashMap<Character, Point3D> entries;
    private HashMap<Character, ArrayList<Point3D>> coverage;

    public World(double radius, double chamberSize) {
        //this.sphere = new Sphere(radius);
        this.chamberSize = chamberSize;
        initEntriesAndCoverage();
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

    public boolean isCleaningZoneCovered(char zone) {
        ArrayList<Point3D> zoneCoverage = coverage.get(zone);
        return zoneCoverage.size() >= 10;
    }

    public boolean isAllCleaningZonesCovered(char currentCleaningZone) {
        return currentCleaningZone == 'D' && isCleaningZoneCovered('D');
    }
}
