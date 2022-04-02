import coppelia.remoteApi;
import coppelia.FloatWA;
import coppelia.FloatW;
import java.util.*;
import static java.lang.Math.*;

public class Controller {

    private int clientID;
    private double radius = 0.8;
    private remoteApi sim;
    private int floor;
    private Chamber leftChamber;
    private Chamber rightChamber;
    private double H = 3;
    private double W = 3;
    boolean[][] coverage;
    double test_x = 0;
    double test_y = 0;
    int cnt = 0;
    
    public Controller(int clientID, remoteApi sim, int[] handles) {
        this.clientID = clientID;
        this.sim = sim;
        this.floor = handles[0];
        this.leftChamber = new Chamber(handles[4], handles[1], handles[2]);
        this.rightChamber = new Chamber(handles[6], handles[8], handles[9]);
        coverage = new boolean[10][10];
        updateCoverage(leftChamber);
        updateCoverage(rightChamber);
    }

    public void randomWalk() {

        while (!isCovered()) {
            fixChamberToFloor(leftChamber);
            sleep(500);
            robotStep(rightChamber, leftChamber);
            sleep(500);
            freeChamberFromFloor(leftChamber);
            sleep(500);

            fixChamberToFloor(rightChamber);
            sleep(500);
            robotStep(leftChamber, rightChamber);
            sleep(500);
            freeChamberFromFloor(rightChamber);
            sleep(500);
        }
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

    private void robotStep(Chamber movingChamber, Chamber fixedChamber) {
        int deg = new Random().nextInt(361);
        float increment = (float) (Math.toRadians(deg));

        FloatW jointPos = new FloatW(0);
        sim.simxGetJointPosition(clientID, fixedChamber.getJoint(), jointPos, sim.simx_opmode_blocking);
        float movement = increment + jointPos.getValue();

        movement = (movement > 360) ? movement - 360 : movement;

        boolean hasCollided = checkCollision(fixedChamber, movement);

        if (!hasCollided) {
            sim.simxSetJointTargetPosition(clientID, fixedChamber.getJoint(), movement, sim.simx_opmode_blocking);
            sleep(500);
            FloatWA pos = getPositionOfHandle(movingChamber.getDummy1());
            if (Math.abs(test_x - pos.getArray()[0]) > 0.001 || Math.abs(test_y - pos.getArray()[1]) > 0.001) {
                System.out.println(cnt);
                System.out.println("predic x: " + test_x + " predic y: " + test_y);
                System.out.println("actual x: " + pos.getArray()[0] + " predic y: " + pos.getArray()[1]);
            }
            updateCoverage(movingChamber);
            cnt++;
        }
    }

    private boolean checkCollision(Chamber fixedChamber, float movement) {
        var pos = getPositionOfHandle(fixedChamber.getDummy1());
        float fixedX = pos.getArray()[0];
        float fixedY = pos.getArray()[1];

        double x = fixedX + radius * Math.cos(movement);
        double y = fixedY + radius * Math.sin(movement);
        test_x = x;
        test_y = y;

        return Math.abs(x) > W/2 || Math.abs(y) > H/2;
    }

    private FloatWA getPositionOfHandle(int handle) {
        FloatWA position = new FloatWA(3);
        sim.simxGetObjectPosition(clientID, handle, -1, position, sim.simx_opmode_blocking);
        return position;
    }

    private boolean isCovered() {
        HashSet<Boolean> s = new HashSet<>();

        for (boolean[] c : coverage) {
            for (boolean b : c) {
                s.add(b);
            }
        }

        return (s.size() == 1);
    }

    private void printCoverage() {
        for (boolean[] row : coverage) System.out.println(Arrays.toString(row));
    }

    private void updateCoverage(Chamber chamber) {
        FloatWA position = new FloatWA(3);
        sim.simxGetObjectPosition(clientID, chamber.getDummy1(), -1, position, sim.simx_opmode_blocking);
        int currX = (round(position.getArray()[0]) + 4) % 9;
        int currY = (round(position.getArray()[1]) + 4) % 9;
        coverage[currX][currY] = true;
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}