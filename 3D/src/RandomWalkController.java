import javafx.geometry.Point3D;
import java.util.ArrayList;
import java.util.Random;

public class RandomWalkController extends Controller {

    public RandomWalkController(Lappa lappa, World world) {
        super(lappa, world);
    }

    public void clean() {
        randomWalk(true);
        lappa.moveToNextHullSide(true, new Point3D(0,0,0));
        randomWalk(false);
    }

    private void randomWalk(boolean isPosHullSide) {
        while (!world.isCovered(isPosHullSide)) {
            float motor = new Random().nextInt(361) - 180;
            lappa.step(motor, isPosHullSide);
            motor = new Random().nextInt(361) - 180;
            lappa.step(motor, isPosHullSide);

            double percent = Math.rint(world.getCoveragePercentage());
            if (percent % 5 == 0) {
                Point3D p = world.getUnCoveredPoint(isPosHullSide);
                lappa.moveToNextHullSide(isPosHullSide, p);
            }
        }
    }

    public void randomWalkRecordResult() {
        ArrayList<String> coveragePercentage = new ArrayList<>();
        ArrayList<String> time = new ArrayList<>();
        int numSteps = 0;

        while (!world.isCovered(true)) {

            double roundedPercent = Math.rint(world.getCoveragePercentage());
            if (roundedPercent % 2 == 0) {
                double percent = world.getCoveragePercentage();
                float absAngle = lappa.getAbsoluteMotorMovement();
                float t = convertAbsAngleToTimeInMinutes(absAngle, numSteps);
                coveragePercentage.add(Double.toString(percent));
                time.add(Float.toString(t));
            }

            float motor = new Random().nextInt(361) - 180;
            lappa.step(motor, true);
            motor = new Random().nextInt(361) - 180;
            lappa.step(motor, true);

            numSteps += 2;
        }

        numSteps += lappa.moveToNextHullSide(true, new Point3D(0,0,0));

        while (!world.isCovered(false)) {

            double roundedPercent = Math.rint(world.getCoveragePercentage());
            if (roundedPercent % 2 == 0) {
                double percent = world.getCoveragePercentage();
                float absAngle = lappa.getAbsoluteMotorMovement();
                float t = convertAbsAngleToTimeInMinutes(absAngle, numSteps);
                coveragePercentage.add(Double.toString(percent));
                time.add(Float.toString(t));
            }

            float motor = new Random().nextInt(361) - 180;
            lappa.step(motor, false);
            motor = new Random().nextInt(361) - 180;
            lappa.step(motor, false);

            numSteps += 2;
        }

        double percent = world.getCoveragePercentage();
        float absAngle = lappa.getAbsoluteMotorMovement();
        float t = convertAbsAngleToTimeInMinutes(absAngle, numSteps);
        coveragePercentage.add(Double.toString(percent));
        time.add(Float.toString(t));

        System.out.println("World is covered");
        coverageResults.add(convertListToArray(coveragePercentage));
        timeResults.add(convertListToArray(time));
    }
}