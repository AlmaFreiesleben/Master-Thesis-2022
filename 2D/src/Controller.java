import coppelia.remoteApi;
import coppelia.FloatWA;
import coppelia.FloatW;
import java.util.Random;

public class Controller {

    private int clientID; 
    private remoteApi sim;
    private int floor;
    private Chamber leftChamber;
    private Chamber rightChamber;
    private double H = 4; // 100 cm is removed from floor size (5x5) to account for the chamber size.
    private double W = 4; // 100 cm is removed from floor size (5x5) to account for the chamber size.
    
    public Controller(int clienID, remoteApi sim, int[] handles) {
        this.clientID = clienID;
        this.sim = sim;
        this.floor = handles[0];
        this.leftChamber = new Chamber(handles[4], handles[1], handles[2]);
        this.rightChamber = new Chamber(handles[6], handles[8], handles[9]);
    }

    public void randomWalk() { 
        while (true) {
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
        float increment = (float) (Math.toRadians(new Random().nextInt(361)));
        sim.simxGetJointPosition(clientID, nonMovingChamber.getJoint(), jointPos, sim.simx_opmode_blocking);
        float degreeOfMovement = jointPos.getValue() + increment;

        boolean hasCollided = checkCollision(nonMovingChamber, degreeOfMovement);

        if (!hasCollided) {
            sim.simxSetJointTargetPosition(clientID, nonMovingChamber.getJoint(), degreeOfMovement, sim.simx_opmode_blocking);
        }
    }

    private boolean checkCollision(Chamber nonMovingChamber, float degreeOfMovement) {
        FloatWA position = new FloatWA(3);       
        sim.simxGetObjectPosition(clientID, nonMovingChamber.getDummy1(), -1, position, sim.simx_opmode_blocking);
        double currX = position.getArray()[0];
        double currY = position.getArray()[1];

        double x = currX + 1 * Math.cos(degreeOfMovement);
        double y = currY + 1 * Math.sin(degreeOfMovement);

        return x > W/2 || y > H/2 || x < -(W/2) || y < -(H/2); 
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}