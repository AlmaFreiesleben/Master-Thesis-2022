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
    private int leftDummy1;
    private int leftDummy2;
    private int leftJoint;
    private int rightJoint;
    private int rightDummy1;
    private int rightDummy2;
    private int leftJointOdometry = 0;
    private int rightJointOdometry = 0;
    
    public Controller(int clienID, boolean is_left_fixed, remoteApi sim, int[] handles) {
        this.clientID = clienID;
        this.is_left_fixed = is_left_fixed;
        this.sim = sim;
        this.floor = handles[0];
        this.leftDummy1 = handles[1];
        this.leftDummy2 = handles[2];
        this.leftJoint = handles[4];
        this.rightJoint = handles[6];
        this.rightDummy1 = handles[8];
        this.rightDummy2 = handles[9];
    }

    public void randomWalk(int steps) {
        for (int i = 0 ; i < steps ; i++) {
            randomWalk();
        }
    }
    
    private void randomWalk() { 
        if (is_left_fixed) {
            robotStep(leftJoint, leftDummy1, leftDummy2);

            /*while (checkCollision(leftDummy1)) {
                robotStep(leftJoint, leftDummy1, leftDummy2);
            }*/

            is_left_fixed = false;        
        } else {
            robotStep(rightJoint, rightDummy1, rightDummy2);
    
            /*while (checkCollision(rightDummy1)) {
                robotStep(rightJoint, rightDummy1, rightDummy2);
            }*/

            is_left_fixed = true;
        }
    }

    private boolean checkCollision(int dummy1) {
        FloatWA position = getPositionOfChamber(dummy1);
        double x = position.getArray()[0];
        double y = position.getArray()[1];
        return x > W/2 || y > H/2 || x < -(W/2) || y < -(H/2); 
    }

    private FloatWA getPositionOfChamber(int forceSensor) {
        FloatWA position = new FloatWA(3);
        
        sim.simxGetObjectPosition(clientID, forceSensor, -1, position, sim.simx_opmode_streaming);
        sleep(2000);

        return position;
    }

    private void fixChamberToFloor(int dummy2) {
        sim.simxSetObjectParent(clientID, dummy2, floor, true, sim.simx_opmode_blocking);
    }

    private void freeChamberFromFloor(int dummy1, int dummy2) {
        sim.simxSetObjectParent(clientID, dummy2, dummy1, true, sim.simx_opmode_blocking);

        FloatWA position = new FloatWA(3);
        sim.simxGetObjectPosition(clientID, dummy1, -1, position, sim.simx_opmode_blocking);

        sim.simxSetObjectPosition(clientID, dummy2, -1, position, sim.simx_opmode_blocking);
    }

    private float generateDegreeOfMovement() {
        float randRadian = (float) (Math.toRadians(new Random().nextInt(361)));
        float degreeOfMovement;

        if (is_left_fixed) {
            leftJointOdometry += randRadian % 360;
            degreeOfMovement = leftJointOdometry;
        } else {
            rightJointOdometry += randRadian % 360;
            degreeOfMovement = rightJointOdometry;
        }    
        
        boolean direction = new Random().nextBoolean();

        return (direction) ? degreeOfMovement : -degreeOfMovement;
    }

    private void robotStep(int joint, int dummy1, int dummy2) {
        float degreeOfMovement = generateDegreeOfMovement();

        fixChamberToFloor(dummy2);
        sleep(2000);

        sim.simxSetJointTargetPosition(clientID, joint, degreeOfMovement, sim.simx_opmode_blocking);
        sleep(2000);

        freeChamberFromFloor(dummy1, dummy2);
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
