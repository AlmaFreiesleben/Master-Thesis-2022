import coppelia.remoteApi;
import coppelia.FloatWA;
import coppelia.FloatW;
import java.util.*;

public class Controller {

    private int clientID;
    private double radius = 0.8;
    private remoteApi sim;
    private int floor;
    private Chamber leftChamber;
    private Chamber rightChamber;
    private double H = 3;
    private double W = 3;
    private boolean isLeftFixed;

    // TEST VARIABLES REMOVE!!! TODO
    double test_x = 0;
    double test_y = 0;
    int cnt = 0;
    float prevA = 0;

    public Controller(int clientID, remoteApi sim, int[] handles) {
        this.clientID = clientID;
        this.sim = sim;
        this.floor = handles[0];
        this.leftChamber = new Chamber(handles[4], handles[1], handles[2], -1);
        this.rightChamber = new Chamber(handles[6], handles[8], handles[9], 1);
        isLeftFixed = true;
    }

    public void randomWalk() {
        int prevA = 0;

        while (true) { //TODO: while !isCovered()
            int A = new Random().nextInt(361);

            if (isLeftFixed) {
                step(rightChamber, leftChamber, A);
            } else {
                step(leftChamber, rightChamber, A);
            }

            prevA = A;
        }
    }

    public void test() {
        step(rightChamber, leftChamber, 20);
        step(leftChamber, rightChamber, -40);
    }

    private void step(Chamber moving, Chamber fixed, int M) {
        fixChamberToFloor(fixed);
        sleep(500);
        moveChamber(fixed, moving, M, prevA);
        sleep(500);
        freeChamberFromFloor(fixed);
        sleep(500);
    }

    private void moveChamber(Chamber fixed, Chamber moving, float M, float A) {
        boolean hasCollided = checkCollision(fixed, M, A);

        if (!hasCollided) {
            var t = sim.simxSetJointTargetPosition(clientID, fixed.getJoint(), (float) Math.toRadians(fixed.getMotorDir() * M), sim.simx_opmode_blocking);
            sleep(500);
            FloatWA pos = getPositionOfHandle(moving.getJoint());
            sleep(500);
            if (Math.abs(test_x - pos.getArray()[0]) > 0.001 || Math.abs(test_y - pos.getArray()[1]) > 0.001) {
                System.out.println(cnt);
                System.out.println("predicted x: " + test_x + " predicted y: " + test_y);
                System.out.println("actual x: " + pos.getArray()[0] + " actual y: " + pos.getArray()[1]);
            }
            cnt++;
            isLeftFixed = !isLeftFixed;
        }
    }

    private boolean checkCollision(Chamber fixed, float M, float A) {
        var pos = getPositionOfHandle(fixed.getJoint());
        float fixedX = pos.getArray()[0];
        float fixedY = pos.getArray()[1];

        float predictedA = predictA(M, A);

        double x = fixedX + radius * Math.cos(Math.toRadians(predictedA));
        double y = fixedY + radius * Math.sin(Math.toRadians(predictedA));

        test_x = x;
        test_y = y;

        return Math.abs(x) > W/2 || Math.abs(y) > H/2;
    }

    private float predictA(float M, float A) {
        float predictedA = -A + M;
        if (predictedA > 360) return A - 360;
        if (predictedA < 0) return A + 360;
        return predictedA;
    }

    private void fixChamberToFloor(Chamber chamber) {
        sim.simxSetObjectParent(clientID, chamber.getDummy2(), floor, true, sim.simx_opmode_blocking);
    }

    private void freeChamberFromFloor(Chamber chamber) {
        sim.simxSetObjectParent(clientID, chamber.getDummy2(), chamber.getDummy1(), true, sim.simx_opmode_blocking);

        FloatWA position = new FloatWA(3);
        sim.simxGetObjectPosition(clientID, chamber.getDummy1(), -1, position, sim.simx_opmode_blocking);

        sim.simxSetObjectPosition(clientID, chamber.getDummy1(), -1, position, sim.simx_opmode_blocking);
    }

    private FloatWA getPositionOfHandle(int handle) {
        FloatWA position = new FloatWA(3);
        sim.simxGetObjectPosition(clientID, handle, -1, position, sim.simx_opmode_blocking);
        return position;
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}