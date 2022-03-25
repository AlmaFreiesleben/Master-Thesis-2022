import coppelia.remoteApi;
import coppelia.FloatWA;
import coppelia.FloatW;
import java.util.Random;

public class Controller {

    private int clientID; 
    private remoteApi sim;
    private int floor;
    private int middle_joint;
    private Chamber rightChamber;
    private Chamber leftChamber;

    public Controller(int clienID, remoteApi sim, int[] handles) {
        this.clientID = clienID;
        this.sim = sim;
        this.floor = handles[0];
        this.middle_joint = handles[1];
        this.rightChamber = new Chamber(handles[2], handles[3], handles[4], handles[5]);
        this.leftChamber = new Chamber(handles[6], handles[7], handles[8], handles[9]);
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
        sim.simxGetJointPosition(clientID, nonMovingChamber.getJoint2(), jointPos, sim.simx_opmode_blocking);
        float degreeOfMovement = jointPos.getValue() + increment;
        sim.simxSetJointTargetPosition(clientID, nonMovingChamber.getJoint2(), degreeOfMovement, sim.simx_opmode_blocking);
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}