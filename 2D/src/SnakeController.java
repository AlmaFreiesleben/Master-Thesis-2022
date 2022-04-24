public class SnakeController extends Controller{

    private float startPosX;

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
            for (int i = 0; i < (world.getWorldH() * 2 - 1); i++) {
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
        float currPosX = lappa.getGreenChamber().getXOfChamber();
        return Math.abs(startPosX-currPosX) > world.getWorldW() * 2;
    }
}