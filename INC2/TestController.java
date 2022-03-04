import coppelia.remoteApi;
import java.util.Random;

public class TestController {

    private int clientID; 
    private remoteApi sim;
    private int leftJoint;
    
    public TestController(int clienID, boolean is_left_fixed, remoteApi sim, int[] handles) {
        this.clientID = clienID;
        this.sim = sim;
        this.leftJoint = handles[3];
    }

    public void randomWalk(int step) {
        int degreeOfMovement = new Random().nextInt(361);
        //int degreeOfMovement = 180;
        sim.simxSetJointTargetPosition(clientID, leftJoint, degreeOfMovement, sim.simx_opmode_blocking);
    }  
}
