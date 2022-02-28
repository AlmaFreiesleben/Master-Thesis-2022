import coppelia.remoteApi;

public class Controller {

    private boolean is_left_fixed;
    private remoteApi simulator;
    
    public Controller(boolean is_left_fixed, remoteApi simulator) {
        this.is_left_fixed = is_left_fixed;
        this.simulator = simulator;
    }

    public boolean checkCollision() { return false; }

    public void robotStep() {
        /*
            if (is_left_fixed) {
                sim.simxSetJointTargetPosition()
                fix right to surface.
            } else {
                sim.simxSetJointTargetPosition()
                fix left to surface.
            }

        */
    }

    public void randomWalk() {
        if (is_left_fixed) {
            // Compute the joint to send to simulator
            robotStep();

            // Check for collision, if a collision has occured choose another action
            while (checkCollision()) {
                robotStep();
            }
            // Change chamber
            is_left_fixed = false;        
        } else {
            robotStep();
    
            while (checkCollision()) {
                robotStep();
            }

            is_left_fixed = true;
        }
    }
}
