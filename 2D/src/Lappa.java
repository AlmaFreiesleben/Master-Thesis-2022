import coppelia.FloatWA;

public class Lappa {

    private final Simulator sim;
    private final Chamber redChamber;
    private final Chamber greenChamber;
    private boolean isRedFixed;
    private float accMotorMovement;

    // TEST VARIABLES REMOVE!!! TODO
    double test_x = 0;
    double test_y = 0;
    int cnt = 1;

    public Lappa(Simulator sim) {
        this.sim = sim;
        this.redChamber = sim.getRedChamber();
        this.greenChamber = sim.getGreenChamber();
        isRedFixed = true;
        accMotorMovement = 0;
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
        float predictedNextMove = predictNextChamberPos(moving, fixed, angle);
        boolean hasCollided = isFallingOfArena(fixed, predictedNextMove);

        if (!hasCollided) {
            fixed.relativeRotateChamber(angle);
            isRedFixed = !isRedFixed;
            accMotorMovement += angle;

            FloatWA pos = sim.getPositionOfHandle(moving.getJoint());
            if (Math.abs(test_x - pos.getArray()[0]) > 0.1 || Math.abs(test_y - pos.getArray()[1]) > 0.1) {
                System.out.println(cnt);
                System.out.println("predicted x: " + test_x + " predicted y: " + test_y);
                System.out.println("actual x: " + pos.getArray()[0] + " actual y: " + pos.getArray()[1]);
            }
            cnt++;
        }
    }

    private float predictNextChamberPos(Chamber moving, Chamber fixed, float angle) {
        float fixedX = fixed.getXOfChamber();
        float movingX = moving.getXOfChamber();

        float predictedNextAngle = 0;
        float transformedAngle = transformBackToAbsoluteCoordinateSystem(angle);

        if (fixedX >= movingX) predictedNextAngle = (angle > 0) ? 180 + transformedAngle : 180 - Math.abs(transformedAngle);
        if (fixedX < movingX) predictedNextAngle = (angle > 0) ? transformedAngle : 360 - Math.abs(transformedAngle);

        return transformedAngle;
    }

    private boolean isFallingOfArena(Chamber fixed, float predictedA) {
        double radius = 0.8;
        double H = 3.5;
        double W = 3.5;

        var pos = sim.getPositionOfHandle(fixed.getJoint());
        float fixedX = pos.getArray()[0];
        float fixedY = pos.getArray()[1];

        double x = fixedX + radius * Math.cos(Math.toRadians(predictedA));
        double y = fixedY + radius * Math.sin(Math.toRadians(predictedA));

        test_x = x;
        test_y = y;

        return Math.abs(x) > W/2 || Math.abs(y) > H/2;
    }

    private float transformBackToAbsoluteCoordinateSystem(float angle) {
        return angle - accMotorMovement;
    }
}
