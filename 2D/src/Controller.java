import coppelia.remoteApi;
import coppelia.FloatWA;

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
    private float prevMotor = 0;

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

    public void randomWalk() {
        while (true) { //TODO: while !isCovered()
            int M = new Random().nextInt(361) - 180;

            if (isLeftFixed) {
                step(rightChamber, leftChamber, M);
            } else {
                step(leftChamber, rightChamber, M);
            }
        }
    }

    public void test() {
        step(rightChamber, leftChamber, -20);
        step(leftChamber, rightChamber, 40);
        step(rightChamber, leftChamber, 40);
    }

    private void step(Chamber moving, Chamber fixed, float motor) {
        fixChamberToFloor(fixed);
        sleep(500);
        moveChamber(moving, fixed, -motor);
        sleep(500);
        freeChamberFromFloor(fixed);
        sleep(500);
    }

    private void moveChamber(Chamber moving, Chamber fixed, float motor) {
        float predictedNextMove = predictNextMovingChamberPos(moving, fixed, motor);
        boolean hasCollided = checkCollision(fixed, predictedNextMove);

        if (!hasCollided) {
            sim.simxSetJointTargetPosition(clientID, fixed.getJoint(), (float) Math.toRadians(-motor), sim.simx_opmode_blocking);
            sleep(500);
            FloatWA pos = getPositionOfHandle(moving.getJoint());
            sleep(500);
            isLeftFixed = !isLeftFixed;
            prevMotor = motor;

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

    private float predictNextMovingChamberPos(Chamber moving, Chamber fixed, float motor) {
        float fixedX = getXofChamber(fixed);
        float movingX = getXofChamber(moving);

        float predictedNextAngle = 0;
        float absoluteAngle = transformBackToAbsoluteCoordinateSystem(motor);

        if (fixedX >= movingX) predictedNextAngle = (motor > 0) ? 180 + absoluteAngle : 180 - Math.abs(absoluteAngle);
        if (fixedX < movingX) predictedNextAngle = (motor > 0) ? absoluteAngle : 360 - Math.abs(absoluteAngle);

        return normalizeAngle(predictedNextAngle);
    }

    private float transformBackToAbsoluteCoordinateSystem(float motor) {
        return normalizeMotor(motor + prevMotor);
    }

    private float normalizeMotor(float angle) {
        if (angle > 180) return angle - 180;
        if (angle < -180) return angle + 180;
        return angle;
    }

    private float normalizeAngle(float angle) {
        if (angle > 360) return angle - 360;
        if (angle < -360) return angle + 360;
        return angle;
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

    private float getXofChamber(Chamber c) {
        FloatWA pos = getPositionOfHandle(c.getJoint());
        return pos.getArray()[0];
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}