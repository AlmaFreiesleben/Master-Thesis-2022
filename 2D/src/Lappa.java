import coppelia.FloatWA;
import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.List;

public class Lappa {

    private final Simulator sim;
    private final World world;
    private final Chamber redChamber;
    private final Chamber greenChamber;
    private boolean isRedFixed;
    private float accMotorMovement;
    private float absoluteMotorMovement;
    private final double radius = 0.8;
    private final double arenaH;
    private final double arenaW;
    private Point2D currentFixedPosition;

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
        absoluteMotorMovement = 0;
        arenaH = world.getWorldH();
        arenaW = world.getWorldW();
        currentFixedPosition = new Point2D(0,0);
    }

    public boolean step(float angle) {
        boolean isFalling = false;
        if (isRedFixed) {
            redChamber.fixChamberToFloor();
            isFalling = moveChamber(greenChamber, redChamber, angle, false);
            redChamber.freeChamberFromFloor();
        } else {
            greenChamber.fixChamberToFloor();
            isFalling = moveChamber(redChamber, greenChamber, angle, false);
            greenChamber.freeChamberFromFloor();
        }
        return isFalling;
    }

    public void stepWithoutFallingDetection(float angle) {
        if (isRedFixed) {
            redChamber.fixChamberToFloor();
            moveChamberWithoutFallingDetection(redChamber, angle, false);
            redChamber.freeChamberFromFloor();
        } else {
            greenChamber.fixChamberToFloor();
            moveChamberWithoutFallingDetection(greenChamber, angle, false);
            greenChamber.freeChamberFromFloor();
        }
    }

    public void simpleStep(float angle) {
        Chamber nonMoving = (isRedFixed) ? redChamber : greenChamber;

        nonMoving.fixChamberToFloor();
        nonMoving.relativeRotateChamber(-angle);
        nonMoving.freeChamberFromFloor();
    }

    public boolean stepWithoutSim(float angle) {
        boolean isFalling = false;

        if (isRedFixed) {
            isFalling = moveChamber(greenChamber, redChamber, angle, true);
        } else {
            isFalling = moveChamber(redChamber, greenChamber, angle, true);
        }
        return isFalling;
    }

    public void stepWithoutSimWithoutFallingDetection(float angle) {
        if (isRedFixed) {
            moveChamberWithoutFallingDetection(redChamber, angle, true);
        } else {
            moveChamberWithoutFallingDetection(greenChamber, angle, true);
        }
    }

    public void moveChamberWithoutFallingDetection(Chamber fixed, float angle, boolean isRecordingResults) {
        Point2D nextPoint = getTargetPoint(fixed, angle, isRecordingResults);

        if (!isRecordingResults) fixed.relativeRotateChamberOneMove(angle);
        List<Point2D> coveredPoints = getSampleOfCoveredPointsOnArc(fixed, angle, isRecordingResults);
        world.updateCoverage(coveredPoints);
        isRedFixed = !isRedFixed;
        accMotorMovement += angle;
        absoluteMotorMovement += Math.abs(angle);
        currentFixedPosition = nextPoint;
    }

    private boolean moveChamber(Chamber moving, Chamber fixed, float angle, boolean isRecordingResults) {
        Point2D nextPoint = getTargetPoint(fixed, angle, isRecordingResults);
        boolean isFalling = isFallingOfArena(nextPoint);

        if (!isFalling) {
            if (!isRecordingResults) fixed.relativeRotateChamberOneMove(angle);
            List<Point2D> coveredPoints = getSampleOfCoveredPointsOnArc(fixed, angle, isRecordingResults);
            world.updateCoverage(coveredPoints);
            isRedFixed = !isRedFixed;
            accMotorMovement += angle;
            absoluteMotorMovement += Math.abs(angle);

            // TODO remove test print
            if (!isRecordingResults) {
                FloatWA pos = sim.getPositionOfHandle(moving.getJoint());
                if (Math.abs(test_x - pos.getArray()[0]) > 0.1 || Math.abs(test_y - pos.getArray()[1]) > 0.1) {
                    System.out.println(cnt);
                    System.out.println("predicted x: " + test_x + " predicted y: " + test_y);
                    System.out.println("actual x: " + pos.getArray()[0] + " actual y: " + pos.getArray()[1]);
                }
                cnt++;
            }
            currentFixedPosition = nextPoint;
        }
        return isFalling;
    }

    private boolean isFallingOfArena(Point2D nextPoint) {
        return Math.abs(nextPoint.getX()) > arenaW/2 || Math.abs(nextPoint.getY()) > arenaH/2;
    }

    private Point2D getFixedCenter(Chamber fixed, boolean isRecordingResults) {
        double fixedX = 0;
        double fixedY = 0;

        if (!isRecordingResults) {
            var pos = sim.getPositionOfHandle(fixed.getJoint());
            fixedX = pos.getArray()[0];
            fixedY = pos.getArray()[1];
        } else {
            fixedX = currentFixedPosition.getX();
            fixedY = currentFixedPosition.getY();
        }

        return new Point2D(fixedX, fixedY);
    }

    private Point2D getPointOnPerimeter(float angle, double fixedX, double fixedY, boolean isGettingTargetPoint) {
        float predictedNextAngle = predictNextChamberPos(angle);

        if (isRedFixed) {
            double x = fixedX + radius * Math.cos(Math.toRadians(predictedNextAngle));
            double y = fixedY + radius * Math.sin(Math.toRadians(predictedNextAngle));

            if (isGettingTargetPoint) {
                test_x = x;
                test_y = y;
            }

            return new Point2D(x, y);

        } else {
            double x = fixedX + radius * -Math.cos(Math.toRadians(predictedNextAngle));
            double y = fixedY + radius * -Math.sin(Math.toRadians(predictedNextAngle));

            if (isGettingTargetPoint) {
                test_x = x;
                test_y = y;
            }

            return new Point2D(x, y);
        }
    }

    private float predictNextChamberPos(float angle) {
        return angle + accMotorMovement % 360;
    }

    private List<Point2D> getSampleOfCoveredPointsOnArc(Chamber fixed, float angle, boolean isRecordingResults) {
        ArrayList<Point2D> coveredPoints = new ArrayList<>();

        Point2D fixedCenter = getFixedCenter(fixed, isRecordingResults);
        double fixedX = fixedCenter.getX();
        double fixedY = fixedCenter.getY();

        int stepOnArc = 5;
        int numPointsOnArc = Math.abs(Math.round(angle/stepOnArc));
        float angleFraction = angle;
        for (int i = 0; i < numPointsOnArc; i++) {
            Point2D p = getPointOnPerimeter(angleFraction, fixedX, fixedY, false);
            coveredPoints.add(p);
            angleFraction = (angle > 0) ? angleFraction-stepOnArc : angleFraction+stepOnArc;
        }

        return coveredPoints;
    }

    private Point2D getTargetPoint(Chamber fixed, float angle, boolean isRecordingResults) {
        Point2D fixedCenter = getFixedCenter(fixed, isRecordingResults);
        double fixedX = fixedCenter.getX();
        double fixedY = fixedCenter.getY();

        return getPointOnPerimeter(angle, fixedX, fixedY, true);
    }

    public Chamber getGreenChamber() { return greenChamber; }

    public Point2D getCurrentFixedPosition() { return currentFixedPosition; }

    public boolean getIsRedFixed() {
        return isRedFixed;
    }

    public float getAbsoluteMotorMovement() { return absoluteMotorMovement; }

    public void preloadAbsoluteMotorMovement() { absoluteMotorMovement = 0; }
}
