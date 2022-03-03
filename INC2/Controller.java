import coppelia.remoteApi;
import java.util.Random;
import coppelia.FloatWA;

public class Controller {

    private int clientID; 
    private boolean is_left_fixed;
    private remoteApi sim;
    private double H = 4.8; // 20 cm is removed from floor size (5x5) to account for the chamber size.
    private double W = 4.8; // 20 cm is removed from floor size (5x5) to account for the chamber size.
    private int floor;
    private int leftForceSensor;
    private int leftChamber;
    private int leftJoint;
    private int cylinder;
    private int rightJoint;
    private int rightChamber;
    private int rightForceSensor;
    
    public Controller(int clienID, boolean is_left_fixed, remoteApi sim, int[] handles) {
        this.clientID = clienID;
        this.is_left_fixed = is_left_fixed;
        this.sim = sim;
        this.floor = handles[0];
        this.leftForceSensor = handles[1];
        this.leftJoint = handles[2];
        this.cylinder = handles[3];
        this.rightJoint = handles[4];
        this.rightChamber = handles[5];
        this.rightForceSensor = handles[6];
    }

    public void randomWalk(int steps) {
        for (int i = 0 ; i < steps ; i++) {
            randomWalk();
        }
    }
    
    private void randomWalk() {        
        if (is_left_fixed) {
            robotStep();

            while (checkCollision(rightForceSensor)) {
                robotStep();
            }
            is_left_fixed = false;        
        } else {
            robotStep();
    
            while (checkCollision(leftForceSensor)) {
                robotStep();
            }

            is_left_fixed = true;
        }
    }

    private boolean checkCollision(int forceSensor) {
        FloatWA position = getPositionOfChamber(forceSensor);
        double x = position.getArray()[0];
        double y = position.getArray()[1];
        double H = 4.8; 
        double W = 4.8;
        return x > W/2 || y > H/2 || x < -(W/2) || y < -(H/2); 
    }

    private FloatWA getPositionOfChamber(int forceSensor) {
        FloatWA position = new FloatWA(3);
        
        sim.simxGetObjectPosition(clientID, forceSensor, -1, position, sim.simx_opmode_streaming);
        sleep(2000);

        return position;
    }

    private void fixChamberToFloor() {
        if (is_left_fixed) {
            sim.simxSetObjectParent(clientID, leftForceSensor, -1, true, sim.simx_opmode_blocking);
            sim.simxSetObjectParent(clientID, leftChamber, leftJoint, true, sim.simx_opmode_blocking);
            sim.simxSetObjectParent(clientID, leftJoint, cylinder, true, sim.simx_opmode_blocking);
            sim.simxSetObjectParent(clientID, cylinder, rightJoint, true, sim.simx_opmode_blocking);
            sim.simxSetObjectParent(clientID, rightJoint, rightChamber, true, sim.simx_opmode_blocking);
            sim.simxSetObjectParent(clientID, rightChamber, rightForceSensor, true, sim.simx_opmode_blocking);
            sim.simxSetObjectParent(clientID, rightForceSensor, floor, true, sim.simx_opmode_blocking);
        } else {
            sim.simxSetObjectParent(clientID, rightForceSensor, -1, true, sim.simx_opmode_blocking);
            sim.simxSetObjectParent(clientID, rightChamber, rightJoint, true, sim.simx_opmode_blocking);
            sim.simxSetObjectParent(clientID, rightJoint, cylinder, true, sim.simx_opmode_blocking);
            sim.simxSetObjectParent(clientID, cylinder, leftJoint, true, sim.simx_opmode_blocking);
            sim.simxSetObjectParent(clientID, leftJoint, leftChamber, true, sim.simx_opmode_blocking);
            sim.simxSetObjectParent(clientID, leftChamber, leftForceSensor, true, sim.simx_opmode_blocking);
            sim.simxSetObjectParent(clientID, leftForceSensor, floor, true, sim.simx_opmode_blocking);
        }
    }

    private void robotStep() {
        int degreeOfMovement = new Random().nextInt(361);
        boolean direction = new Random().nextBoolean();
        degreeOfMovement = (direction) ? degreeOfMovement : -degreeOfMovement;

        if (is_left_fixed) {
            sim.simxSetJointTargetPosition(clientID, leftJoint, degreeOfMovement, sim.simx_opmode_blocking);
        } else {
            sim.simxSetJointTargetPosition(clientID, rightJoint, degreeOfMovement, sim.simx_opmode_blocking);
        } 
        sleep(2000);
        fixChamberToFloor();
        sleep(2000);
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}
