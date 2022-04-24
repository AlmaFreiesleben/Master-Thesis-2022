public class SnakeController extends Controller{

    private float startPosX;

    public SnakeController(Lappa lappa, World world) {
        super(lappa, world);
        this.startPosX = lappa.getGreenChamber().getXOfChamber();
        world.createSamplingOfPointsPositiveQuadrant();
    }

    public void snakeWalk() {
        float motor = 75;
        float turnMotor = 180;
        float turn = 0;
        int numSteps = 0;

        while (!world.isCovered()) {
            for (int i = 0; i < (world.getWorldH() * 2 - 1); i++) {
                lappa.stepWithoutFallingDetection(motor);
                numSteps++;
                lappa.stepWithoutFallingDetection(-motor);
                numSteps++;
            }

            if (turn % 2 == 0) {
                lappa.stepWithoutFallingDetection(0);
                lappa.stepWithoutFallingDetection(turnMotor);
            } else {
                lappa.stepWithoutFallingDetection(-turnMotor);
                lappa.stepWithoutFallingDetection(0);
            }
            numSteps++;
            turn++;
        }
        System.out.println("World is covered");
        System.out.println(numSteps);
    }
 }