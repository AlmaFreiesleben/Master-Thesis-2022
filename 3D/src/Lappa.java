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

    public int moveToNextHullSide(boolean isPosHullSide, Point3D entryToNextCleaningZone) {
        Point3D initialPositionRedChamber = redChamber.getCurrentPosition();
        Point3D initialPositionGreenChamber = greenChamber.getCurrentPosition();
        int numStep = 0;

        if (initialPositionRedChamber.getY() > initialPositionGreenChamber.getY()) {
            float angle = (float) initialPositionRedChamber.angle(entryToNextCleaningZone, initialPositionGreenChamber);
            stepChamber(redChamber, -angle, isPosHullSide);
            numStep++;

            if (entryToNextCleaningZone.equals(new Point3D(0,0,0))) {
                while (redChamber.getCurrentPosition().getX() > 0 && greenChamber.getCurrentPosition().getX() > 0) {
                    stepChamber(greenChamber, 180, isPosHullSide);
                    stepChamber(redChamber, -180, isPosHullSide);
                    numStep += 2;
                }
            } else {
                stepChamber(greenChamber, 180, isPosHullSide);
                stepChamber(redChamber, -180, isPosHullSide);
                stepChamber(greenChamber, 180, isPosHullSide);
                numStep += 2;
            }
        } else {
            float angle = (float) initialPositionRedChamber.angle(entryToNextCleaningZone, initialPositionGreenChamber);
            stepChamber(redChamber, angle, isPosHullSide);
            numStep++;

            if (entryToNextCleaningZone.equals(new Point3D(0,0,0))) {
                while (redChamber.getCurrentPosition().getX() > 0 && greenChamber.getCurrentPosition().getX() > 0) {
                    stepChamber(greenChamber, -180, isPosHullSide);
                    stepChamber(redChamber, 180, isPosHullSide);
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

    private Point3D getPositionOfMovingChamber() {
        Chamber moving = (isRedFixed) ? greenChamber : redChamber;
        return moving.getCurrentPosition();
    }

    private boolean isValid(boolean isPosHullSide) {
        Point3D p = getPositionOfMovingChamber();
        if (isPosHullSide) return p.getZ() >= 0.5 && p.getX() >= 0;
        else return p.getZ() >= 0.5 && p.getX() < 0;
    }
}

