import javafx.geometry.Point3D;
import java.util.ArrayList;

public class Lappa {
    private final Simulator sim;
    private final World world;
    private final Chamber redChamber;
    private final Chamber greenChamber;
    private boolean isRedFixed;

    public Lappa(Simulator sim, World world) {
        this.sim = sim;
        this.world = world;
        this.redChamber = sim.getRedChamber();
        this.greenChamber = sim.getGreenChamber();
        isRedFixed = true;
    }

    public void step(float angle, boolean isPosHullSide) {
        if (isRedFixed) {
            stepChamber(redChamber, angle, isPosHullSide);
        } else {
            stepChamber(greenChamber, angle, isPosHullSide);
        }
    }

    private void stepChamber(Chamber c, float angle, boolean isPosHullSide) {
        c.fixChamberToFloor();
        c.relativeRotateChamber(angle);
        if (!isValid(isPosHullSide)) {
            ArrayList<Point3D> points = c.getPointsOnArc();
            world.updateCoverage(points, isPosHullSide);
            c.relativeRotateChamber(-angle);
        } else {
            ArrayList<Point3D> points = c.getPointsOnArc();
            world.updateCoverage(points, isPosHullSide);
        }
        c.freeChamberFromFloor();
        isRedFixed = !isRedFixed;
    }

    private void stepTowardsTarget(Chamber c, float angle, boolean isPosHullSide) {
        c.fixChamberToFloor();
        c.relativeRotateChamber(angle);
        ArrayList<Point3D> points = c.getPointsOnArc();
        world.updateCoverage(points, isPosHullSide);
        c.freeChamberFromFloor();
        isRedFixed = !isRedFixed;
    }

    public int moveToTargetPoint(boolean isPosHullSide, Point3D targetPoint) {
        Point3D initialPositionRedChamber = redChamber.getCurrentRoundedPosition();
        Point3D initialPositionGreenChamber = greenChamber.getCurrentRoundedPosition();
        int numStep = 0;

        if (initialPositionRedChamber.getY() > initialPositionGreenChamber.getY()) {
            float angle = (float) initialPositionRedChamber.angle(targetPoint, initialPositionGreenChamber);
            stepChamber(redChamber, -angle, isPosHullSide);
            numStep++;

            if (targetPoint.equals(new Point3D(0,0,2.5))) {
                while (redChamber.getCurrentPosition().getX() > 0 || greenChamber.getCurrentPosition().getX() > 0) {
                    stepTowardsTarget(greenChamber, 180, isPosHullSide);
                    stepTowardsTarget(redChamber, -180, isPosHullSide);
                    numStep += 2;
                }
            } else {
                stepChamber(greenChamber, 180, isPosHullSide);
                stepChamber(redChamber, -180, isPosHullSide);
                stepChamber(greenChamber, 180, isPosHullSide);
                numStep += 2;
            }
        } else {
            float angle = (float) initialPositionRedChamber.angle(targetPoint, initialPositionGreenChamber);
            stepChamber(redChamber, angle, isPosHullSide);
            numStep++;

            if (targetPoint.equals(new Point3D(0,0,2.5))) {
                while (redChamber.getCurrentPosition().getX() > 0 || greenChamber.getCurrentPosition().getX() > 0) {
                    stepTowardsTarget(greenChamber, -180, isPosHullSide);
                    stepTowardsTarget(redChamber, 180, isPosHullSide);
                    numStep += 2;
                }
            } else {
                stepChamber(greenChamber, -180, isPosHullSide);
                stepChamber(redChamber, 180, isPosHullSide);
                stepChamber(greenChamber, -180, isPosHullSide);
                numStep += 2;
            }
        }
        return numStep;
    }

    public float getAbsoluteMotorMovement() {
        return redChamber.getAbsMotorOdometry() + greenChamber.getAbsMotorOdometry();
    }

    public Point3D getPositionGreenChamber() {
        return greenChamber.getCurrentPosition();
    }

    public Point3D getPositionRedChamber() {
        return redChamber.getCurrentPosition();
    }

    private Point3D getPositionOfMovingChamber() {
        Chamber moving = (isRedFixed) ? greenChamber : redChamber;
        return moving.getCurrentRoundedPosition();
    }

    private boolean isValid(boolean isPosHullSide) {
        Point3D p = getPositionOfMovingChamber();
        if (isPosHullSide) return p.getZ() >= 0.5 && p.getX() >= 0;
        else return p.getZ() >= 0.5 && p.getX() < 0;
    }
}

