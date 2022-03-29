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
    private HashMap<Character, Point3D> entries;
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
        while (!isCleaningZoneCovered(currentCleaningZone)) {
            step(leftChamber, rightChamber);
            step(rightChamber, leftChamber);
        }
        return true;
    }

    private void step(Chamber nonMoving, Chamber moving) {
        fixChamberToFloor(nonMoving);
        sleep(1000);
        randomRobotStep(nonMoving);
        sleep(1000);
        while (!isValidStep(moving)) {
            randomRobotStep(nonMoving);
            sleep(1000);
        }
        coverage.add(getPositionOfObject(moving.getDummy1()));
        freeChamberFromFloor(nonMoving);

        sleep(1000);
    }

    private char whatCleaningZone(Point3D chamberPosition) {
        double x = chamberPosition.getX();
        double y = chamberPosition.getY();
        double z = chamberPosition.getZ();

        if (z > 0) return '-';

        if       (x >= 0 && y > 0 || x == 0 && y == 0)  return 'A';
        else if  (x > 0 && y <= 0)                      return 'B';
        else if  (x <= 0 && y < 0)                      return 'C';
        else if  (x < 0 && y >= 0)                      return 'D';
        else                                            return '-';
    }

    private void moveToNextCleaningZone() {
        if      (currentCleaningZone == 'A') currentCleaningZone = 'B';
        else if (currentCleaningZone == 'B') currentCleaningZone = 'C';
        else if (currentCleaningZone == 'C') currentCleaningZone = 'D';
        else return;

        pathToNextCleaningZone();
    }

    private void pathToNextCleaningZone() {
        Point3D entryToNextCleaningZone = entries.get(currentCleaningZone);
        if (currentPositionLeftChamber.distance(entryToNextCleaningZone) > currentPositionRightChamber.distance(entryToNextCleaningZone)) {
            while (whatCleaningZone(currentPositionLeftChamber) != currentCleaningZone || whatCleaningZone(currentPositionRightChamber) != currentCleaningZone) {
                double angle = currentPositionRightChamber.angle(entryToNextCleaningZone, currentPositionLeftChamber);
                robotStep(rightChamber, angle);

                angle = currentPositionLeftChamber.angle(entryToNextCleaningZone, currentPositionRightChamber);
                robotStep(leftChamber, angle);
            }
        } else {
            while (whatCleaningZone(currentPositionRightChamber) != currentCleaningZone || whatCleaningZone(currentPositionLeftChamber) != currentCleaningZone) {
                double angle = currentPositionLeftChamber.angle(entryToNextCleaningZone, currentPositionRightChamber);
                robotStep(leftChamber, angle);

                angle = currentPositionRightChamber.angle(entryToNextCleaningZone, currentPositionLeftChamber);
                robotStep(rightChamber, angle);
            }
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

    private void randomRobotStep(Chamber nonMoving) {
        FloatW jointPos = new FloatW(0);
        float increment = (float) (Math.toRadians(new Random().nextInt(361)));
        sim.simxGetJointPosition(clientID, nonMoving.getJoint2(), jointPos, sim.simx_opmode_blocking);
        float radiansOfMovement = jointPos.getValue() + increment;
        sim.simxSetJointTargetPosition(clientID, nonMoving.getJoint2(), radiansOfMovement, sim.simx_opmode_blocking);
    }

    private boolean isValidStep(Chamber moved) {
        Point3D newPosition = getPositionOfObject(moved.getDummy1());
        char cleaningZoneOfNewPosition = whatCleaningZone(newPosition);
        return currentCleaningZone == cleaningZoneOfNewPosition;
    }

    private void robotStep(Chamber nonMovingChamber, double angle) {
        float radiansOfMovement = (float) Math.toRadians(angle);
        sim.simxSetJointTargetPosition(clientID, nonMovingChamber.getJoint2(), radiansOfMovement, sim.simx_opmode_blocking);
    }

    private Point3D getPositionOfObject(int objectHandle) {
        FloatWA position = new FloatWA(3);
        sim.simxGetObjectPosition(clientID, objectHandle, -1, position, sim.simx_opmode_blocking);
        float[] pos = position.getArray();
        double f = 0.5;
        double x = f * Math.round(pos[0]/f);
        double y = f * Math.round(pos[1]/f);
        double z = f * Math.round(pos[2]/f);
        return new Point3D(x, y, z);
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}