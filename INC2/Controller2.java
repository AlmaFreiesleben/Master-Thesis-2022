import coppelia.remoteApi;
import coppelia.FloatWA;
import coppelia.FloatW;
import java.util.Random;

public class Controller2 {

    private int clientID; 
    private remoteApi sim;
    private int floor;
    private Chamber leftChamber;
    private Chamber rightChamber;
    
    public Controller2(int clienID, remoteApi sim, int[] handles) {
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
            robotStep(leftChamber);
            sleep(1000);
            freeChamberFromFloor(leftChamber);

            sleep(1000);

            fixChamberToFloor(rightChamber);
            sleep(1000);
            robotStep(rightChamber);
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

    private void robotStep(Chamber chamber) {
        FloatW jointPos = new FloatW(0);
        float increment = (float) (Math.toRadians(new Random().nextInt(361)));
        float degreeOfMovement = jointPos.getValue() + increment;

        sim.simxGetJointPosition(clientID, chamber.getJoint(), jointPos, sim.simx_opmode_blocking);
        sim.simxSetJointTargetPosition(clientID, chamber.getJoint(), degreeOfMovement, sim.simx_opmode_blocking);
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}