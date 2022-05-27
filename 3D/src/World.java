import javafx.geometry.Point3D;
import java.util.ArrayList;
import java.util.Random;

public class World {

    private ArrayList<Point3D> pointsToCover;
    private ArrayList<Point3D> unCoveredPointsPos;
    private ArrayList<Point3D> unCoveredPointsNeg;
    private double chamberDiameter;

    public World(double radius, double chamberDiameter) {
        samplePointsToCover();
        this.chamberDiameter = chamberDiameter;
    }

    /*public boolean isCovered(boolean isPosHullSide) {
        return (isPosHullSide) ? unCoveredPointsPos.isEmpty() : unCoveredPointsNeg.isEmpty();
    }*/

    public boolean isCovered(boolean isPosHullSide) {
        return (isPosHullSide) ? unCoveredPointsPos.size() <= 8 : unCoveredPointsNeg.size() <= 12;
    }

    public void updateCoverage(ArrayList<Point3D> points, boolean isPosHullSide) {
        for (Point3D p : points) {
            updateCoverage(p, isPosHullSide);
        }
    }

    private void updateCoverage(Point3D p, boolean isPosHullSide) {
        ArrayList<Point3D> unCoveredPoints = (isPosHullSide) ? unCoveredPointsPos : unCoveredPointsNeg;
        double currentShortestDistance = Integer.MAX_VALUE;
        Point3D currentNearestPoint = p;
        for (Point3D notCoveredPoint : unCoveredPoints) {
            double distTo = p.distance(notCoveredPoint);
            if (distTo < currentShortestDistance) {
                currentShortestDistance = distTo;
                currentNearestPoint = notCoveredPoint;
            }
        }
        if (currentShortestDistance <= chamberDiameter) unCoveredPoints.remove(currentNearestPoint);
        System.out.println(getCoveragePercentage());
    }

    public Point3D getUnCoveredPoint(boolean isPosHullSide) {
        ArrayList<Point3D> unCoveredPoints = (isPosHullSide) ? unCoveredPointsPos : unCoveredPointsNeg;
        int index = new Random().nextInt(unCoveredPoints.size());
        return unCoveredPoints.get(index);
    }

    public double getCoveragePercentage() {
        double restToCover = unCoveredPointsPos.size() + unCoveredPointsNeg.size();
        return 100 - ((restToCover / pointsToCover.size()) * 100);
    }

    private void samplePointsToCover() {
        pointsToCover = new ArrayList<>();
        unCoveredPointsPos = new ArrayList<>();
        unCoveredPointsNeg = new ArrayList<>();

        double x, y, z;
        int N = 900;
        double L = Math.sqrt(N*Math.PI);

        for (int k = 1; k <= N ; k++) {
            double h_k = 1-((float)(2*k-1)/N);
            double phi = Math.acos(h_k);
            double theta = L*phi;

            // Convert from spherical to cartesian points
            x = 2.5 * Math.sin(phi) * Math.cos(theta);
            y = 2.5 * Math.sin(phi) * Math.sin(theta);
            z = 2.5 * Math.cos(phi);

            if (z > 1) {
                pointsToCover.add(new Point3D(x, y, z));
                if (x > 0) {
                    unCoveredPointsPos.add(new Point3D(x, y, z));
                }
                if (x < 0) {
                    unCoveredPointsNeg.add(new Point3D(x, y, z));
                }
            }
        }
        System.out.println("size of world: " + pointsToCover.size());
        System.out.println("pos: " + unCoveredPointsPos.size());
        System.out.println("neg: " + unCoveredPointsNeg.size());
    }
}
