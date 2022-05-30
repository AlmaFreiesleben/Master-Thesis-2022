import javafx.geometry.Point3D;
import java.util.ArrayList;

public class Chamber {
    private final int joint;
    private final int dummy1;
    private final int dummy2;
    private final Simulator sim;
    private float motorOdometry;
    private ArrayList<Point3D> pointsOnArc;
    private float absMotorOdometry;

    public Chamber(int joint, int dummy1, int dummy2, Simulator sim) {
        this.joint = joint;
        this.dummy1 = dummy1;
        this.dummy2 = dummy2;
        this.sim = sim;
        motorOdometry = 0;
        pointsOnArc = new ArrayList<>();
        absMotorOdometry = 0;
    }

    public Point3D getCurrentRoundedPosition() { return sim.getPositionOfObject(dummy1); }

    public Point3D getCurrentPosition() { return sim.getPrecisePositionOfObject(dummy1); }

    public void updateMotorOdometry(float angle) {
        motorOdometry += angle;
        absMotorOdometry += Math.abs(angle);
    }

    public ArrayList<Point3D> getPointsOnArc() { return pointsOnArc; }

    public void relativeRotateChamber(float angle) {
        pointsOnArc.clear();

        int fraction = (Math.abs(angle) >= 20) ? Math.round(Math.abs(angle)/20) : 1;

        for (int i = 0; i < fraction; i++) {
            updateMotorOdometry(angle/fraction);
            sim.move(joint, motorOdometry);
            Point3D p = getCurrentRoundedPosition();
            pointsOnArc.add(p);
        }
    }

    public void freeChamberFromFloor() {
        sim.freeChamberFromFloor(dummy1, dummy2);
    }

    public void fixChamberToFloor() {
        sim.fixChamberToFloor(dummy2);
    }

    public float getAbsMotorOdometry() {
        return absMotorOdometry;
    }
}