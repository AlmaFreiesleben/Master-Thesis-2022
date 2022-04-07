import javafx.geometry.Point2D;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.stream.Collectors;

public class World {

    private double H;
    private double W;
    private double chamberDiameter;
    private HashSet<Point2D> pointsToCover = new HashSet<>();

    public World(double H, double W, double chamberDiameter) {
        this.H = H;
        this.W = W;
        this.chamberDiameter = chamberDiameter;
        createSamplingOfPoints();
    }

    public double getWorldH() {
        return H;
    }

    public double getWorldW() {
        return W;
    }

    public boolean isCovered() {
        return pointsToCover.isEmpty();
    }

    public void printCoverage() {
        System.out.println("size: " + pointsToCover.size());
    }

    private double normalizeTo1Decimal(double coord) {
        return new BigDecimal(coord).setScale(1, RoundingMode.HALF_UP).doubleValue();
    }

    public void updateCoverage(Point2D p) {
        double currentShortestDistance = Integer.MAX_VALUE;
        Point2D currentNearestPoint = p;
        for (Point2D notCoveredPoint : pointsToCover) {
            double distTo = p.distance(notCoveredPoint);
            if (distTo < currentShortestDistance) {
                currentShortestDistance = distTo;
                currentNearestPoint = notCoveredPoint;
            }
        }
        pointsToCover.remove(currentNearestPoint);
    }

    private void createSamplingOfPoints() {
        int width = (int) Math.floor(W/chamberDiameter);
        int equalWidth = (width % 2 == 0) ? width : width + 1;
        int quadrantWidth = equalWidth / 2;
        double x = 0;
        for (int i = 0; i < quadrantWidth; i++) {
            double y = 0;
            for (int j = 0; j < quadrantWidth*2; j++) {
                double loc_x = 0;
                if (j % 2 == 0) loc_x = x;
                else loc_x = x + 0.2;

                loc_x = normalizeTo1Decimal(loc_x);
                y = normalizeTo1Decimal(y);

                if (loc_x == 0 && y == 0) {
                    pointsToCover.add(new Point2D(loc_x, y));
                } else if (y == 0) {
                    pointsToCover.add(new Point2D(loc_x, y));    // 1. quadrant
                    pointsToCover.add(new Point2D(-loc_x, y));   // 2. quadrant
                } else if (loc_x == 0) {
                    pointsToCover.add(new Point2D(loc_x, y));    // 1. quadrant
                    pointsToCover.add(new Point2D(loc_x, -y));   // 4. quadrant
                } else {
                    pointsToCover.add(new Point2D(loc_x, y));    // 1. quadrant
                    pointsToCover.add(new Point2D(-loc_x, y));   // 2. quadrant
                    pointsToCover.add(new Point2D(-loc_x, -y));  // 3. quadrant
                    pointsToCover.add(new Point2D(loc_x, -y));   // 4. quadrant
                }
                y += 0.2;
            }
            x += 0.4;
        }
    }
}
