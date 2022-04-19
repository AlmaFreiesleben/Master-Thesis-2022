import javafx.geometry.Point3D;

public class Chamber {
    private final int joint;
    private final int dummy1;
    private final int dummy2;
    private final Simulator sim;
    private float motorOdometry;
    private Point3D currentPosition;

    public Chamber(int joint, int dummy1, int dummy2, Simulator sim) {
        this.joint = joint;
        this.dummy1 = dummy1;
        this.dummy2 = dummy2;
        this.sim = sim;
        motorOdometry = 0;
        currentPosition = sim.getPositionOfObject(dummy1);
    }

    public Point3D getCurrentPosition() { return currentPosition; }

    public void updateMotorOdometry(float angle) {
        motorOdometry += angle;
    }

    public void relativeRotateChamber(float angle) {
        if (angle > 180 || angle < -180) System.out.println("Unaxepted angle: " + angle);
        updateMotorOdometry(angle/2);
        sim.move(joint, motorOdometry);
        updateMotorOdometry(angle/2);
        sim.move(joint, motorOdometry);
    }

    public void freeChamberFromFloor() {
        sim.freeChamberFromFloor(dummy1, dummy2);
    }

    public void fixChamberToFloor(int floor) {
        sim.fixChamberToFloor(dummy2, floor);
    }

    public char whatCleaningZone() {
        Point3D pos = sim.getPositionOfObject(dummy1);
        double x = pos.getX();
        double y = pos.getY();
        double z = pos.getZ();

        if (z > 0) return '-';

        if       (x >= 0 && y > 0 || x == 0 && y == 0)  return 'A';
        else if  (x > 0 && y <= 0)                      return 'B';
        else if  (x <= 0 && y < 0)                      return 'C';
        else if  (x < 0 && y >= 0)                      return 'D';
        else                                            return '-';
    }
}