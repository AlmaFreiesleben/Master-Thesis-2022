import javafx.geometry.Point3D;

public class Lappa {
    private final Simulator sim;
    private final World world;
    private final Chamber redChamber;
    private final Chamber greenChamber;
    private boolean isRedFixed;
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
    }

    public void step(float angle) {
        if (isRedFixed) {
            stepChamber(redChamber, angle);
        } else {
            stepChamber(greenChamber, angle);
        }
    }

    public void stepChamber(Chamber c, float angle) {
        c.fixChamberToFloor();
        c.relativeRotateChamber(angle);
        if (!isValid()) {
            c.relativeRotateChamber(-angle);
            isRedFixed = !isRedFixed;
        }
        c.freeChamberFromFloor();
        //world.updateCoverage(c.whatCleaningZone(), c.getCurrentPosition());
        isRedFixed = !isRedFixed;
    }

    public char getCurrentCleaningZone() { return currentCleaningZone; }

    private void robotStep(double angle, boolean isRedMoving) {
        Chamber toBeMoved = (isRedMoving) ? redChamber : greenChamber;
        Chamber toBeFixed = getOppositeChamber(toBeMoved);

        if ((isRedFixed && toBeMoved == redChamber) || (!isRedFixed && toBeMoved == greenChamber)) {
            toBeFixed.relativeRotateChamber((float) angle);
        } else {
            toBeFixed.fixChamberToFloor();
            toBeMoved.freeChamberFromFloor();
            toBeFixed.relativeRotateChamber((float) angle);
        }
    }

    private Chamber getOppositeChamber(Chamber c) {
        return (c == redChamber) ? greenChamber : redChamber;
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
                robotStep(angle, true);

                angle = currentPositionRedChamber.angle(entryToNextCleaningZone, currentPositionGreenChamber);
                robotStep(angle, false);
            }
        } else {
            while (greenChamber.whatCleaningZone() != currentCleaningZone || redChamber.whatCleaningZone() != currentCleaningZone) {
                double angle = currentPositionRedChamber.angle(entryToNextCleaningZone, currentPositionGreenChamber);
                robotStep(angle, false);

                angle = currentPositionGreenChamber.angle(entryToNextCleaningZone, currentPositionRedChamber);
                robotStep(angle, true);
            }
        }
    }

    private boolean isValid() {
        Chamber moving = (isRedFixed) ? greenChamber : redChamber;
        Point3D p = moving.getCurrentPosition();
        return p.getZ() >= 0;
    }

    private boolean isValidStep() {
        Chamber moving = (isRedFixed) ? greenChamber : redChamber;
        char cleaningZoneOfNewPosition = moving.whatCleaningZone();
        return currentCleaningZone == cleaningZoneOfNewPosition;
    }
}

