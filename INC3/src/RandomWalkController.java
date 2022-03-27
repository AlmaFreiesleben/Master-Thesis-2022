import coppelia.remoteApi;
import coppelia.FloatWA;
import coppelia.FloatW;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import javafx.geometry.Point3D;

public class RandomWalkController extends Controller {

    // The four cleaning zones are hard-coded (A,B,C,D), the robot starts in A.
    // The "adjacency-graph" is hard-coded as well: A -> B -> C -> D.
    private char currentCleaningZone = 'A';
    private HashMap<Character, Point3D> entries; // TODO: Make sure entries are correct
    private ArrayList<Point3D> coverage;
    private Point3D currentPositionLeftChamber;
    private Point3D currentPositionRightChamber;

    public RandomWalkController(int clientID, remoteApi sim, int[] handles) {
        super(clientID, sim, handles);
        coverage = new ArrayList<>();
        entries = new HashMap<>();
        entries.put('A', new Point3D(0, 2.5,0));
        entries.put('B', new Point3D(2.5,0,0));
        entries.put('C', new Point3D(0,-2.5,0));
        entries.put('D', new Point3D(-2.5,0,0));
        currentPositionLeftChamber = getPositionOfObject(leftChamber.getDummy1());
        currentPositionRightChamber = getPositionOfObject(rightChamber.getDummy1());
        coverage.add(currentPositionLeftChamber);
        coverage.add(currentPositionRightChamber);
    }

    public void clean() {
        while (!isAllCleaningZonesCovered()) {
            boolean isZoneCovered = randomWalk();
            if (isZoneCovered) {
                moveToNextCleaningZone();
            }
        }
    }

    private boolean randomWalk() {
        // TODO: IMPLEMENT ACTUAL WALK -> use whatCleaningZone()
        while (!isCleaningZoneCovered(currentCleaningZone)) {
            fixChamberToFloor(leftChamber);
            sleep(1000);
            robotStep(leftChamber);
            sleep(1000);
            freeChamberFromFloor(leftChamber);

            sleep(1000);

            fixChamberToFloor(rightChamber);
            sleep(1000);
            robotStep(rightChamber);
            sleep(1000);
            freeChamberFromFloor(rightChamber);

            sleep(1000);
        }
        return true;
    }

    private char whatCleaningZone(Point3D chamberPosition) {
        double x = chamberPosition.getX();
        double y = chamberPosition.getY();

        if      (x > 0 && y > 0) return 'A';
        else if (x > 0 && y < 0) return 'B';
        else if (x < 0 && y < 0) return 'C';
        else if (x < 0 && y > 0) return 'D';
        else                     return '-';
    }

    private void moveToNextCleaningZone() {
        if      (currentCleaningZone == 'A') currentCleaningZone = 'B';
        else if (currentCleaningZone == 'B') currentCleaningZone = 'C';
        else if (currentCleaningZone == 'C') currentCleaningZone = 'D';
        else return;

        pathToNextCleaningZone();
    }

    private void pathToNextCleaningZone() {
        // TODO: ACTUALLY MOVE FROM ONE ZONE TO NEXT:
        //  Compute path from left and right chamber to critical point in next zone
        Point3D entryToNextCleaningZone = entries.get(currentCleaningZone);
        if (currentPositionLeftChamber.distance(entryToNextCleaningZone) > currentPositionRightChamber.distance(entryToNextCleaningZone)) {
            // move right chamber first
        } else {
            // move left chamber first
        }
    }

    private boolean isCleaningZoneCovered(char zone) {
        return coverage.size() == 10;
    }

    private boolean isAllCleaningZonesCovered() {
        return currentCleaningZone == 'D' && isCleaningZoneCovered('D');
    }

    private void fixChamberToFloor(Chamber chamber) {
        sim.simxSetObjectParent(clientID, chamber.getDummy2(), floor, true, sim.simx_opmode_blocking);
    }

    private void freeChamberFromFloor(Chamber chamber) {
        sim.simxSetObjectParent(clientID, chamber.getDummy2(), chamber.getDummy1(), true, sim.simx_opmode_blocking);

        FloatWA position = new FloatWA(3);
        sim.simxGetObjectPosition(clientID, chamber.getDummy1(), -1, position, sim.simx_opmode_blocking);

        sim.simxSetObjectPosition(clientID, chamber.getDummy1(), -1, position, sim.simx_opmode_blocking);
    }

    private void robotStep(Chamber nonMovingChamber) {
        FloatW jointPos = new FloatW(0);
        float increment = (float) (Math.toRadians(new Random().nextInt(361)));
        sim.simxGetJointPosition(clientID, nonMovingChamber.getJoint2(), jointPos, sim.simx_opmode_blocking);
        float degreeOfMovement = jointPos.getValue() + increment;
        sim.simxSetJointTargetPosition(clientID, nonMovingChamber.getJoint2(), degreeOfMovement, sim.simx_opmode_blocking);
    }

    private Point3D getPositionOfObject(int objectHandle) {
        FloatWA position = new FloatWA(3);
        sim.simxGetObjectPosition(clientID, objectHandle, -1, position, sim.simx_opmode_blocking);
        float[] pos = position.getArray();
        return new Point3D(pos[0], pos[1], pos[2]);
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}