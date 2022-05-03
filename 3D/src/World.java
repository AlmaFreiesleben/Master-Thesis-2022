import javafx.geometry.Point3D;
import java.util.ArrayList;

public class World {

    private ArrayList<Point3D> pointsToCover;
    private ArrayList<Point3D> unCoveredPoints;
    private double chamberDiameter;

    public World(double radius, double chamberDiameter) {
        samplePointsToCover(radius);
        this.chamberDiameter = chamberDiameter;
    }

    public boolean isCovered() {
        return unCoveredPoints.isEmpty();
    }

    public void updateCoverage(Point3D p) {
        double currentShortestDistance = Integer.MAX_VALUE;
        Point3D currentNearestPoint = p;
        for (Point3D notCoveredPoint : unCoveredPoints) {
            double distTo = p.distance(notCoveredPoint);
            if (distTo < currentShortestDistance) {
                currentShortestDistance = distTo;
                currentNearestPoint = notCoveredPoint;
            }
        }
        if (currentShortestDistance < chamberDiameter) unCoveredPoints.remove(currentNearestPoint);
        System.out.println(getCoveragePercentage());
    }

    public double getCoveragePercentage() {
        double restToCover = unCoveredPoints.size();
        return 100 - ((restToCover / pointsToCover.size()) * 100);
    }

    private void samplePointsToCover(double radius) {
        pointsToCover = new ArrayList<>();
        unCoveredPoints = new ArrayList<>();

        double x, y, z;
        int samples = 600;
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
}
