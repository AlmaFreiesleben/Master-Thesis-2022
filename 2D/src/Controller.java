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
    private double H = 4;
    private double W = 4;
    boolean[][] coverage;
    
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
            sleep(1000);
            robotStep(rightChamber, leftChamber);
            sleep(1000);
            freeChamberFromFloor(leftChamber);

            sleep(1000);

            fixChamberToFloor(rightChamber);
            sleep(1000);
            robotStep(leftChamber, rightChamber);
            sleep(1000);
            freeChamberFromFloor(rightChamber);

            sleep(1000);
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

    private void robotStep(Chamber movingChamber, Chamber nonMovingChamber) {
        FloatW jointPos = new FloatW(0);
        //int deg = new Random().nextInt(361);
        int deg = new Random().nextInt(180);
        float increment = (float) (Math.toRadians(deg));
        sim.simxGetJointPosition(clientID, nonMovingChamber.getJoint(), jointPos, sim.simx_opmode_blocking);
        float degreeOfMovement = increment + jointPos.getValue();

        boolean hasCollided = checkCollision(nonMovingChamber, movingChamber, degreeOfMovement);

        if (!hasCollided) {
            sim.simxSetJointTargetPosition(clientID, nonMovingChamber.getJoint(), degreeOfMovement, sim.simx_opmode_blocking);
            sleep(500);
            updateCoverage(movingChamber);
        }
    }

    private boolean checkCollision(Chamber nonMovingChamber, Chamber moving, float degreeOfMovement) {
        var pos = getPositionOfHandle(nonMovingChamber.getDummy1());
        double fixedX = pos.getArray()[0];
        double fixedY = pos.getArray()[1];

        double x = fixedX + radius * Math.cos(degreeOfMovement);
        double y = fixedY + radius * Math.sin(degreeOfMovement);

        return x > W/2 || y > H/2 || x < -(W/2) || y < -(H/2); 
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