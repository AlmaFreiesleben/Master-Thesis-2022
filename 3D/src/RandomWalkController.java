import java.util.ArrayList;
import java.util.Random;

public class RandomWalkController extends Controller {

    public RandomWalkController(Lappa lappa, World world) {
        super(lappa, world);
    }

    public void randomWalk() {
        while (!world.isCovered()) {
            float motor = new Random().nextInt(361) - 180;
            lappa.step(motor);
            motor = new Random().nextInt(361) - 180;
            lappa.step(motor);
        }
    }

    public void randomWalkRecordResult() {
        ArrayList<String> coveragePercentage = new ArrayList<>();
        ArrayList<String> time = new ArrayList<>();
        int numSteps = 0;

        while (!world.isCovered()) {

            if (numSteps % 10 == 0) {
                double percent = world.getCoveragePercentage();
                float absAngle = lappa.getAbsoluteMotorMovement();
                float t = convertAbsAngleToTimeInMinutes(absAngle, numSteps);
                coveragePercentage.add(Double.toString(percent));
                time.add(Float.toString(t));
            }

            float motor = new Random().nextInt(361) - 180;
            lappa.step(motor);
            motor = new Random().nextInt(361) - 180;
            lappa.step(motor);

            numSteps++;
        }

        System.out.println("World is covered");
        coverageResults.add(convertListToArray(coveragePercentage));
        timeResults.add(convertListToArray(time));
    }
}