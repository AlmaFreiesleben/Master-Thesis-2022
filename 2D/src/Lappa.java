import coppelia.FloatWA;
import javafx.geometry.Point2D;

public class Lappa {

    private final Simulator sim;
    private final World world;
    private final Chamber redChamber;
    private final Chamber greenChamber;
    private boolean isRedFixed;
    private float accMotorMovement;
    private double radius = 0.8;
    private double arenaH;
    private double arenaW;

    // TODO TEST VARIABLES REMOVE!!!
    double test_x = 0;
    double test_y = 0;
    int cnt = 1;

    public Lappa(Simulator sim, World world) {
        this.sim = sim;
        this.world = world;
        this.redChamber = sim.getRedChamber();
        this.greenChamber = sim.getGreenChamber();
        isRedFixed = true;
        accMotorMovement = 0;
        arenaH = world.getWorldH();
        arenaW = world.getWorldW();
        world.updateCoverage(0,0);
        world.updateCoverage(0.8, 0);
    }

    public void step(float angle) {
        int floor = sim.getFloor();
        if (isRedFixed) {
            redChamber.fixChamberToFloor(floor);
            moveChamber(greenChamber, redChamber, angle);
            redChamber.freeChamberFromFloor();
        } else {
            greenChamber.fixChamberToFloor(floor);
            moveChamber(redChamber, greenChamber, angle);
            greenChamber.freeChamberFromFloor();
        }
    }

    public void simpleStep(float angle) {
        int floor = sim.getFloor();
        if (isRedFixed) {
            redChamber.fixChamberToFloor(floor);
            redChamber.relativeRotateChamber(-angle);
            redChamber.freeChamberFromFloor();
        } else {
            greenChamber.fixChamberToFloor(floor);
            greenChamber.relativeRotateChamber(-angle);
            greenChamber.freeChamberFromFloor();
        }
    }

    public boolean getIsRedFixed() {
        return isRedFixed;
    }

    private void moveChamber(Chamber moving, Chamber fixed, float angle) {
        Point2D nextPoint = getNextPoint(fixed, angle);
        boolean isFalling = isFallingOfArena(nextPoint);

        if (!isFalling) {
            fixed.relativeRotateChamber(angle);
            isRedFixed = !isRedFixed;
            accMotorMovement += angle;
            world.updateCoverage(nextPoint.getX(), nextPoint.getY());

            // TODO remove test print
            FloatWA pos = sim.getPositionOfHandle(moving.getJoint());
            if (Math.abs(test_x - pos.getArray()[0]) > 0.1 || Math.abs(test_y - pos.getArray()[1]) > 0.1) {
                System.out.println(cnt);
                System.out.println("predicted x: " + test_x + " predicted y: " + test_y);
                System.out.println("actual x: " + pos.getArray()[0] + " actual y: " + pos.getArray()[1]);
            }
            cnt++;
        }
    }

    private boolean isFallingOfArena(Point2D nextPoint) {
        return Math.abs(nextPoint.getX()) > arenaW /2 || Math.abs(nextPoint.getY()) > arenaH /2;
    }

    private Point2D getNextPoint(Chamber fixed, float angle) {
        var pos = sim.getPositionOfHandle(fixed.getJoint());
        float fixedX = pos.getArray()[0];
        float fixedY = pos.getArray()[1];

        float predictedNextAngle = predictNextChamberPos(angle);

        if (isRedFixed) {
            double x = fixedX + radius * Math.cos(Math.toRadians(predictedNextAngle));
            double y = fixedY + radius * Math.sin(Math.toRadians(predictedNextAngle));

            test_x = x;
            test_y = y;

            return new Point2D(x, y);

        } else {
            double x = fixedX + radius * -Math.cos(Math.toRadians(predictedNextAngle));
            double y = fixedY + radius * -Math.sin(Math.toRadians(predictedNextAngle));

            test_x = x;
            test_y = y;

            return new Point2D(x, y);
        }
    }

    private float predictNextChamberPos(float angle) {
        return angle + accMotorMovement % 360;
    }
}
