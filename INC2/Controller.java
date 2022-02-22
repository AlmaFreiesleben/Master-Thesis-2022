public class Controller {

    private boolean is_left_fixed;
    
    public Controller(boolean is_left_fixed) {
        this.is_left_fixed = is_left_fixed;
    }

    public boolean checkCollision() { return false; }

    public void robotStep() {
        
    }

    public void randomWalk() {
        if (is_left_fixed) {
            // Compute the joint to send to simulator
            robot_step();

            // Check for collision, if a collision has occured choose another action
            while (collision_detection()) {
                robot_step();
            }
            // Change chamber
            is_left_fixed = false;        
        } else {
            robot_step();
    
            while (collision_detection()) {
                robot_step();
            }

            is_left_fixed = true;
        }
    }
}
