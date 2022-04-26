import coppelia.FloatWA;
import coppelia.remoteApi;

public class Chamber {
    private final int joint;
    private final int dummy1;
    private final int dummy2;
    private final Simulator sim;
    private float motorOdometry;

    public Chamber(int joint, int dummy1, int dummy2, Simulator sim) {
        this.joint = joint;
        this.dummy1 = dummy1;
        this.dummy2 = dummy2;
        this.sim = sim;
        motorOdometry = 0;
    }

    public int getJoint() { return joint; }

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

    public void relativeRotateChamberOneMove(float angle) {
        if (Math.abs(angle) == 180f) {
            relativeRotateChamber(angle);
        } else {
            updateMotorOdometry(angle);
            sim.move(joint, motorOdometry);
        }
    }

    public float getXOfChamber() {
        FloatWA pos = sim.getPositionOfHandle(joint);
        return pos.getArray()[0];
    }

    public void freeChamberFromFloor() {
        sim.freeChamberFromFloor(dummy1, dummy2);
    }

    public void fixChamberToFloor() {
        sim.fixChamberToFloor(dummy2);
    }
}