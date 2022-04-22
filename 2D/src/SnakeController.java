import java.util.ArrayList;

public class SnakeController extends Controller{

    private float startPosX;
    private boolean isRecordingResults = false;

    public SnakeController(Lappa lappa, World world) {
        super(lappa, world);
        this.startPosX = lappa.getGreenChamber().getXOfChamber();
    }

    public void snakeWalk() {
        float motor = 75;
        float turnMotor = 180;
        float turn = 0;
        int numSteps = 0;

        while (!isCovered()) {
            for (int i = 0; i < 7; i++) {
                lappa.stepWithChamberControl(motor, false);
                numSteps++;
                lappa.stepWithChamberControl(-motor, true);
                numSteps++;
            }
            if (turn % 2 == 0) lappa.stepWithChamberControl(turnMotor, true);
            else lappa.stepWithChamberControl(-turnMotor, false);
            numSteps++;
            turn++;
        }
        System.out.println("World is covered");
        System.out.println("number of steps: " + numSteps);
    }

    private boolean isCovered() {
        System.out.println("start: " + startPosX);
        float currPosX = lappa.getGreenChamber().getXOfChamber();
        if (isRecordingResults) currPosX = (float) lappa.getCurrentFixedPosition().getX();
        System.out.println("curr: " + currPosX);
        return Math.abs(startPosX-currPosX) > world.getWorldW() * 2;
    }
}