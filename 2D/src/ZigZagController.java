import java.util.ArrayList;
import java.util.Random;

public class ZigZagController extends Controller {

    public ZigZagController(Lappa lappa, World world) {
        super(lappa, world);
    }

    public void zigZagWalk() {
        boolean isFalling = false;
        while (!world.isCovered()) {
            float motor = 75;
            if (isFalling) motor = new Random().nextInt(361) - 180;

            if (lappa.getIsRedFixed()) {
                isFalling = lappa.step(motor);
            } else {
                isFalling = lappa.step(-motor);
            }
        }
    }

    public void zigZagRecordResult() {
        ArrayList<String> coveragePercentage = new ArrayList<>();
        ArrayList<String> time = new ArrayList<>();
        int numSteps = 0;
        boolean isFalling = false;

        while (!world.isCovered()) {

            if (numSteps % 10 == 0) {
                double percent = world.getCoveragePercentage();
                float absAngle = lappa.getAbsoluteMotorMovement();
                float t = convertAbsAngleToTimeInMinutes(absAngle, numSteps);
                coveragePercentage.add(Double.toString(percent));
                time.add(Float.toString(t));
            }

            float motor = 95;
            if (isFalling) motor = new Random().nextInt(361) - 180;

            if (lappa.getIsRedFixed()) {
                isFalling = lappa.stepWithoutSim(motor);
            } else {
                isFalling = lappa.stepWithoutSim(-motor);
            }

            numSteps++;
        }

        System.out.println("World is covered");
        coverageResults.add(convertListToArray(coveragePercentage));
        timeResults.add(convertListToArray(time));
    }
}
