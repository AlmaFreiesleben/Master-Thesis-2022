import javafx.geometry.Point3D;

import java.util.Random;

public class Lappa {
    private final Simulator sim;
    private final World world;
    private final Chamber redChamber;
    private final Chamber greenChamber;
    private boolean isRedFixed;
    private float accMotorMovement;
    private float absoluteMotorMovement;
    private final double radius = 0.8;

    // The four cleaning zones are hard-coded (A,B,C,D), the robot starts in A.
    // The "adjacency-graph" is hard-coded as well: A -> B -> C -> D.
    private char currentCleaningZone = 'A';

    public Lappa(Simulator sim, World world) {
        this.sim = sim;
        this.world = world;
        this.redChamber = sim.getRedChamber();
        this.greenChamber = sim.getGreenChamber();
        isRedFixed = true;
        accMotorMovement = 0;
        absoluteMotorMovement = 0;
    }

    public void step(float angle) {
        int floor = sim.getFloor();
        if (isRedFixed) {
            redChamber.fixChamberToFloor(floor);
            randomRobotStep();
            redChamber.freeChamberFromFloor();
        } else {
            greenChamber.fixChamberToFloor(floor);
            randomRobotStep();
            greenChamber.freeChamberFromFloor();
        }
    }

    public char getCurrentCleaningZone() { return currentCleaningZone; }

    private void randomRobotStep() {
        Chamber fixed = (isRedFixed) ? redChamber : greenChamber;
        float increment = (float) (Math.toRadians(new Random().nextInt(361)));
        if (isValidStep()) fixed.relativeRotateChamber(increment);
    }

    private void robotStep(double angle) {
        Chamber fixed = (isRedFixed) ? redChamber : greenChamber;
        fixed.relativeRotateChamber((float) angle);
    }

    public void moveToNextCleaningZone() {
        if      (currentCleaningZone == 'A') currentCleaningZone = 'B';
        else if (currentCleaningZone == 'B') currentCleaningZone = 'C';
        else if (currentCleaningZone == 'C') currentCleaningZone = 'D';
        else return;

        pathToNextCleaningZone();
    }

    private void pathToNextCleaningZone() {
        Point3D currentPositionRedChamber = redChamber.getCurrentPosition();
        Point3D currentPositionGreenChamber = greenChamber.getCurrentPosition();
        Point3D entryToNextCleaningZone = world.getEntry(currentCleaningZone);

        if (currentPositionRedChamber.distance(entryToNextCleaningZone) > currentPositionGreenChamber.distance(entryToNextCleaningZone)) {
            while (redChamber.whatCleaningZone() != currentCleaningZone || greenChamber.whatCleaningZone() != currentCleaningZone) {
                double angle = currentPositionGreenChamber.angle(entryToNextCleaningZone, currentPositionRedChamber);
                robotStep(angle);

                angle = currentPositionRedChamber.angle(entryToNextCleaningZone, currentPositionGreenChamber);
                robotStep(angle);
            }
        } else {
            while (greenChamber.whatCleaningZone() != currentCleaningZone || redChamber.whatCleaningZone() != currentCleaningZone) {
                double angle = currentPositionRedChamber.angle(entryToNextCleaningZone, currentPositionGreenChamber);
                robotStep(angle);

                angle = currentPositionGreenChamber.angle(entryToNextCleaningZone, currentPositionRedChamber);
                robotStep(angle);
            }
        }
    }

    private boolean isValidStep() {
        Chamber moving = (!isRedFixed) ? redChamber : greenChamber;
        char cleaningZoneOfNewPosition = moving.whatCleaningZone();
        return currentCleaningZone == cleaningZoneOfNewPosition;
    }
}

