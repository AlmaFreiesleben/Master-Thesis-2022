import java.util.ArrayList;
import java.util.Random;

public class RandomWalkController extends Controller {

    public RandomWalkController(Lappa lappa, World world) {
        super(lappa, world);
    }

    public void clean() {
        randomWalk(true, 15);
        lappa.moveToNextHullSide(true);
        randomWalk(false, 40);
    }

    private void randomWalk(boolean isPosHullSide, int perc) {
        //while (!world.isCovered(isPosHullSide)) {
        while (world.getCoveragePercentage() < perc) {
            float motor = new Random().nextInt(361) - 180;
            lappa.step(motor, isPosHullSide);
            motor = new Random().nextInt(361) - 180;
            lappa.step(motor, isPosHullSide);
        }
    }

    /*public void randomWalkRecordResult() {
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

                if (percent > 80) {
                    coverageResults.add(convertListToArray(coveragePercentage));
                    timeResults.add(convertListToArray(time));
                    writeToFiles();
                }
            }

            float motor = new Random().nextInt(361) - 180;
            lappa.step(motor);
            motor = new Random().nextInt(361) - 180;
            lappa.step(motor);

            numSteps += 2;
        }

        System.out.println("World is covered");
        coverageResults.add(convertListToArray(coveragePercentage));
        timeResults.add(convertListToArray(time));
    }*/
}