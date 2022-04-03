import coppelia.remoteApi;
import coppelia.FloatWA;
import coppelia.FloatW;
import java.util.*;

public class Controller {

    private int clientID;
    private remoteApi sim;
    private int floor;
    private Chamber leftChamber;
    private Chamber rightChamber;
    private boolean isLeftFixed;
    private double radius = 0.8;
    private double H = 3;
    private double W = 3;
    private float prevA = 0;

    // TEST VARIABLES REMOVE!!! TODO
    double test_x = 0;
    double test_y = 0;
    int cnt = 0;

    public Controller(int clientID, remoteApi sim, int[] handles) {
        this.clientID = clientID;
        this.sim = sim;
        this.floor = handles[0];
        this.leftChamber = new Chamber(handles[4], handles[1], handles[2]);
        this.rightChamber = new Chamber(handles[6], handles[8], handles[9]);
        isLeftFixed = true;
    }

    /*public void randomWalk() {
        while (true) { //TODO: while !isCovered()
            int M = new Random().nextInt(361) - 180;

            if (isLeftFixed) {
                step(rightChamber, leftChamber, M);
            } else {
                step(leftChamber, rightChamber, M);
            }
        }
    }*/

    public void test() {
        step(rightChamber, leftChamber, -20);
        step(leftChamber, rightChamber, 40);
        step(rightChamber, leftChamber, -60);
    }

    public void test2() {
        fixChamberToFloor(leftChamber);
        sleep(500);
        for (int m = 0 ; m >= -180; m-=20) {
            sim.simxSetJointTargetPosition(clientID, leftChamber.getJoint(), (float) Math.toRadians(m), sim.simx_opmode_blocking);
            sleep(500);
        }
    }

    public void test3() {
        fixChamberToFloor(leftChamber);
        sleep(500);
        float M = (float) Math.toRadians(-20);
        sim.simxSetJointTargetPosition(clientID, leftChamber.getJoint(), M, sim.simx_opmode_blocking);
        sleep(500);
        freeChamberFromFloor(leftChamber);
        sleep(500);

        fixChamberToFloor(rightChamber);
        sleep(500);
        float newM = (float) Math.toRadians(40);
        sim.simxSetJointTargetPosition(clientID, rightChamber.getJoint(), newM, sim.simx_opmode_blocking);
        sleep(500);
    }

    private void step(Chamber moving, Chamber fixed, int M) {
        fixChamberToFloor(fixed);
        sleep(500);
        moveChamber(moving, fixed, M);
        sleep(500);
        freeChamberFromFloor(fixed);
        sleep(500);
    }

    private void moveChamber(Chamber moving, Chamber fixed, float M) {
        float predictedA = predictA(fixed, M);
        boolean hasCollided = checkCollision(fixed, predictedA);

        if (!hasCollided) {
            sim.simxSetJointTargetPosition(clientID, fixed.getJoint(), (float) Math.toRadians(M), sim.simx_opmode_blocking);
            sleep(500);
            FloatWA pos = getPositionOfHandle(moving.getJoint());
            sleep(500);
            isLeftFixed = !isLeftFixed;
            prevA = M;

            if (Math.abs(test_x - pos.getArray()[0]) > 0.01 || Math.abs(test_y - pos.getArray()[1]) > 0.01) {
                System.out.println(cnt);
                System.out.println("predicted x: " + test_x + " predicted y: " + test_y);
                System.out.println("actual x: " + pos.getArray()[0] + " actual y: " + pos.getArray()[1]);
            }
            cnt++;
        }
    }

    private boolean checkCollision(Chamber fixed, float predictedA) {
        var pos = getPositionOfHandle(fixed.getJoint());
        float fixedX = pos.getArray()[0];
        float fixedY = pos.getArray()[1];

        double x = fixedX + radius * Math.cos(Math.toRadians(predictedA));
        double y = fixedY + radius * Math.sin(Math.toRadians(predictedA));

        test_x = x;
        test_y = y;

        return Math.abs(x) > W/2 || Math.abs(y) > H/2;
    }

    private float predictA(Chamber fixed, float M) {
        if (fixed.equals(leftChamber) && M < 0) return (prevA < 0) ? (M * -1) + prevA : (M * -1) - prevA;
        //if (fixed.equals(leftChamber) && M >= 0)  TODO
        //if (fixed.equals(rightChamber) && M < 0)  TODO
        if (fixed.equals(rightChamber) && M >= 0) return (prevA < 0) ? 180 - (M + prevA) : 180 - (M - prevA);
        return 0;
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