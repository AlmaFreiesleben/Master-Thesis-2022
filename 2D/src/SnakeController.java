import java.util.ArrayList;

public class SnakeController extends Controller{

    public SnakeController(Lappa lappa, World world) {
        super(lappa, world);
        world.createSamplingOfPointsPositiveQuadrant();
    }

    public void snakeWalk() {
        float motor = 75;
        float turnMotor = 180;
        float turn = 0;

        while (!world.isCovered()) {
            for (int i = 0; i < (world.getWorldH() * 2); i++) {
                lappa.stepWithoutFallingDetection(motor);
                lappa.stepWithoutFallingDetection(-motor);
            }

            if (turn % 2 == 0) {
                lappa.stepWithoutFallingDetection(0);
                lappa.stepWithoutFallingDetection(turnMotor);
            } else {
                lappa.stepWithoutFallingDetection(-turnMotor);
                lappa.stepWithoutFallingDetection(0);
            }
            turn++;
        }
        System.out.println("World is covered");
    }

    public void snakeWalkRecordResults() {
        ArrayList<String> coveragePercentage = new ArrayList<>();
        ArrayList<String> time = new ArrayList<>();
        float motor = 75;
        float turnMotor = 180;
        float turn = 0;
        int numSteps = 0;

        while (world.getCoveragePercentage() < 97) {

            double percent = world.getCoveragePercentage();
            float absAngle = lappa.getAbsoluteMotorMovement();
            float t = convertAbsAngleToTimeInMinutes(absAngle, numSteps);
            coveragePercentage.add(Double.toString(percent));
            time.add(Float.toString(t));

            for (int i = 0; i < (world.getWorldH() * 2); i++) {
                lappa.stepWithoutSimWithoutFallingDetection(motor);
                numSteps++;
                lappa.stepWithoutSimWithoutFallingDetection(-motor);
                numSteps++;
            }

            if (turn % 2 == 0) {
                lappa.stepWithoutSimWithoutFallingDetection(0);
                lappa.stepWithoutSimWithoutFallingDetection(turnMotor);
            } else {
                lappa.stepWithoutSimWithoutFallingDetection(-turnMotor);
                lappa.stepWithoutSimWithoutFallingDetection(0);
            }
            numSteps++;
            turn++;
        }
        System.out.println("World is covered");
        coverageResults.add(convertListToArray(coveragePercentage));
        timeResults.add(convertListToArray(time));
    }
}