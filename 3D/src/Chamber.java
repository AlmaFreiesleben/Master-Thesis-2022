import javafx.geometry.Point3D;

import java.util.ArrayList;

public class Chamber {
    private final int joint;
    private final int dummy1;
    private final int dummy2;
    private final Simulator sim;
    private float motorOdometry;
    private ArrayList<Point3D> pointsOnArc;

    public Chamber(int joint, int dummy1, int dummy2, Simulator sim) {
        this.joint = joint;
        this.dummy1 = dummy1;
        this.dummy2 = dummy2;
        this.sim = sim;
        motorOdometry = 0;
        pointsOnArc = new ArrayList<>();
    }

    public Point3D getCurrentPosition() { return sim.getPositionOfObject(dummy1); }

    public void updateMotorOdometry(float angle) {
        motorOdometry += angle;
    }

    public ArrayList<Point3D> getPointsOnArc() { return pointsOnArc; }

    public void relativeRotateChamber(float angle) {
        pointsOnArc.clear();

        if (angle > 180 || angle < -180) System.out.println("Unaxepted angle: " + angle);
        /*if (Math.abs(angle) == 180f) {
            updateMotorOdometry(angle/2);
            sim.move(joint, motorOdometry);
            updateMotorOdometry(angle/2);
            sim.move(joint, motorOdometry);
        } else {
            updateMotorOdometry(angle);
            sim.move(joint, motorOdometry);
        }*/

        int fraction = Math.round(angle/20);

        for (int i = 0; i < fraction; i++) {
            updateMotorOdometry(angle/fraction);
            sim.move(joint, motorOdometry);
            Point3D p = getCurrentPosition();
            pointsOnArc.add(p);
        }
    }

    public void freeChamberFromFloor() {
        sim.freeChamberFromFloor(dummy1, dummy2);
    }

    public void fixChamberToFloor() {
        sim.fixChamberToFloor(dummy2);
    }
}