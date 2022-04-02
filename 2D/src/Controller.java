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
    boolean isFirst = true;
    
    public Controller(int clientID, remoteApi sim, int[] handles) {
        this.clientID = clientID;
        this.sim = sim;
        this.floor = handles[0];
        this.leftChamber = new Chamber(handles[4], handles[1], handles[2]);
        this.rightChamber = new Chamber(handles[6], handles[8], handles[9]);
        isLeftFixed = true;
    }

    public void randomWalk() {
        int A = 0;

        while (true) { //TODO: while !isCovered()
            boolean dir = new Random().nextBoolean();
            int degreeIncrement = (dir) ? new Random().nextInt(181) : new Random().nextInt(-181);

            if (isLeftFixed) {
                step(rightChamber, leftChamber, degreeIncrement, A);
            } else {
                step(leftChamber, rightChamber, degreeIncrement, A);
            }

            A = degreeIncrement;
        }
    }

    public void test() {
        step(rightChamber, leftChamber, -20, 0);
        isFirst = false;
        step(leftChamber, rightChamber, 40, -20);
    }

    private void step(Chamber moving, Chamber fixed, int degree, int lastMove) {
        fixChamberToFloor(fixed);
        sleep(500);
        moveChamber(fixed, moving, degree, lastMove);
        sleep(500);
        freeChamberFromFloor(fixed);
        sleep(500);
    }

    private void moveChamber(Chamber fixed, Chamber moving, int degree, int A) {
        FloatW jointPos = new FloatW(0);
        sim.simxGetJointPosition(clientID, fixed.getJoint(), jointPos, sim.simx_opmode_blocking);
        float B = (float) (Math.toRadians(degree)) + jointPos.getValue();

        B = (B > 360) ? B - 360 : B;

        boolean hasCollided = checkCollision(fixed, B, A);

        if (!hasCollided) {
            sim.simxSetJointTargetPosition(clientID, fixed.getJoint(), B, sim.simx_opmode_blocking);
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

    private boolean checkCollision(Chamber fixed, float B, float A) {
        var pos = getPositionOfHandle(fixed.getJoint());
        float fixedX = pos.getArray()[0];
        float fixedY = pos.getArray()[1];

        float C = (isFirst) ? B : 180 - (B - Math.abs(A));

        double x = fixedX + radius * Math.cos(C);
        double y = fixedY + radius * Math.sin(C);

        test_x = x;
        test_y = y;

        return Math.abs(x) > W/2 || Math.abs(y) > H/2;
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