import coppelia.remoteApi;
import coppelia.FloatWA;

import java.util.*;

public class Controller {

    private int clientID;
    private remoteApi sim;
    private int floor;
    private Chamber redChamber;
    private Chamber greenChamber;
    private boolean isRedFixed;
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
        this.redChamber = new Chamber(handles[4], handles[1], handles[2]);
        this.greenChamber = new Chamber(handles[6], handles[8], handles[9]);
        isRedFixed = true;
    }

    public void randomWalk() {
        while (true) { //TODO: while !isCovered()
            float motor = new Random().nextInt(361) - 180;

            if (isRedFixed) {
                step(greenChamber, redChamber, motor);
            } else {
                step(redChamber, greenChamber, motor);
            }
        }
    }

    public void test_chamber_moving_back_and_forth() {
        step(greenChamber, redChamber, -20);
        step(redChamber, greenChamber, 40);
        step(greenChamber, redChamber, 40);
    }

    public void test_chamber_reaching_motor_overflow_minus_1() {
        step(greenChamber, redChamber, -20);
        step(redChamber, greenChamber, -170);
    }

    public void test_chamber_reaching_motor_overflow_minus_2() {
        step(greenChamber, redChamber, -170);
        step(redChamber, greenChamber, -20);
    }

    public void test_chamber_reaching_motor_overflow_minus_long() {
        step(greenChamber, redChamber, -170);
        step(redChamber, greenChamber, -20);
        step(greenChamber, redChamber, -20);
        step(redChamber, greenChamber, -170);
    }

    public void test_chamber_reaching_motor_overflow_plus() {
        step(greenChamber, redChamber, 20);
        step(redChamber, greenChamber, 170);
    }

    public void test_movement_of_chamber() {
        step(greenChamber, redChamber, 90);
        step(greenChamber, redChamber, 90 + 90);
        step(greenChamber, redChamber, 90 + 90 + 90);
        step(greenChamber, redChamber, 90 + 90 + 90 + 90);
        step(greenChamber, redChamber, 90 + 90 + 90 + 90 + 90);
        step(greenChamber, redChamber, 90 + 90 + 90 + 90 + 90 - 90);
    }

    public void test_motor_overflow() {
        step(greenChamber, redChamber, -105);
        step(redChamber, greenChamber, -90);
        step(greenChamber, redChamber, -82 + - 105);
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
            isRedFixed = !isRedFixed;
            prevMotor = motor;

            if (Math.abs(test_x - pos.getArray()[0]) > 0.1 || Math.abs(test_y - pos.getArray()[1]) > 0.1) {
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
        float fixedX = getXOfChamber(fixed);
        float movingX = getXOfChamber(moving);

        float predictedNextAngle = 0;
        float absoluteAngle = transformBackToAbsoluteCoordinateSystem(motor);

        if (fixedX >= movingX) predictedNextAngle = (motor > 0) ? 180 + absoluteAngle : 180 - Math.abs(absoluteAngle);
        if (fixedX < movingX) predictedNextAngle = (motor > 0) ? absoluteAngle : 360 - Math.abs(absoluteAngle);

        return normalize(fixedX, movingX, motor, absoluteAngle, predictedNextAngle);
    }

    private float transformBackToAbsoluteCoordinateSystem(float motor) {
        return motor + prevMotor;
    }

    private float normalizeAngle(float angle) {
        if (angle >= 360) return angle - 360;
        if (angle <= -360) return angle + 360;
        if (angle > 180 && angle < 270) return angle - 180;
        return angle;
    }

    private float normalize(float fixedX, float movingX, float motor, float absAngle, float predAngle) {
        if (fixedX >= movingX) {
            if (motor > 0) { // 3 QUADRANT
                if (absAngle > 180) return absAngle - 180;
            }
            if (motor < 0) { // 2 QUADRANT
                if (absAngle < -180) return 360 - (Math.abs(absAngle) - 180);
            }
        }
        if (fixedX < movingX) {
            if (motor > 0) { // 1 QUADRANT
                if (absAngle > 180) return absAngle - 180;
            }
            if (motor < 0) { // 4 QUADRANT
                if (absAngle < -180) return 360 - Math.abs(absAngle);
            }
        }
        return normalizeAngle(predAngle);
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

    private float getXOfChamber(Chamber c) {
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