import javafx.geometry.Point2D;
import java.util.HashSet;

public class World {

    private double H;
    private double W;
    HashSet<Point2D> pointsToCover = new HashSet<>();

    public World(double H, double W) {
        this.H = H;
        this.W = W;
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

    public void updateCoverage(double x, double y) {
        // TODO normalize to 1 decimal
        Point2D point = new Point2D(x,y);
        if (pointsToCover.contains(point)) pointsToCover.remove(point);
    }

    private void createSamplingOfPoints() {
        int width = (int) Math.floor(W/0.4); // 0.4 is the size of a chamber of lappa.
        int quadrantWidth = (int) Math.floor(width / 2);
        double x = 0;
        for (int i = 0; i < quadrantWidth; i++) {
            double y = 0;
            for (int j = 0; j < quadrantWidth*2; j++) {
                double loc_x = 0;
                if (j % 2 == 0) loc_x = x;
                else loc_x += 0.2;

                if (loc_x == 0 && y == 0) {
                    pointsToCover.add(new Point2D(loc_x, y));
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
