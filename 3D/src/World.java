import javafx.geometry.Point3D;
import javafx.scene.shape.Sphere;
import java.util.ArrayList;
import java.util.HashMap;

public class World {

    private Sphere sphere;
    private double chamberSize;
    private HashMap<Character, Point3D> entries;
    private ArrayList<Point3D> coverage;

    public World(int radius, double chamberSize) {
        this.sphere = new Sphere(radius);
        this.chamberSize = chamberSize;
        coverage = new ArrayList<>();
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
        return coverage.size() == 10;
    }

    public boolean isAllCleaningZonesCovered(char currentCleaningZone) {
        return currentCleaningZone == 'D' && isCleaningZoneCovered('D');
    }
}
