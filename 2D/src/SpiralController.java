import java.util.ArrayList;

public class SpiralController extends Controller {

    private double currOuterRadius;
    private double currInnerRadius;
    private float accOuterDegree;

    public SpiralController(Lappa lappa, World world) {
        super(lappa, world);
        this.currOuterRadius = 80;
        this.currInnerRadius = 0;
        this.accOuterDegree = 0;
    }

    public void spiralWalk() {
        float outerMotor = 80;
        float innerMotor = 0;

        while (!world.isCovered()) {
            if (lappa.getIsRedFixed()) {
                lappa.step(outerMotor);
                accOuterDegree += outerMotor - innerMotor;
            } else {
                if (hasRotatedEntireCircle()) {
                    accOuterDegree = 0;
                    currOuterRadius += 10;
                    currInnerRadius += 10;
                    double lengthOfCleanedArc = getLengthOfCleanedArc(outerMotor, currOuterRadius);
                    double numStepsOnOuterPerimeter = Math.round(getLengthOfPerimeter(currOuterRadius) / lengthOfCleanedArc);
                    innerMotor = (float) Math.floor(getLengthOfPerimeter(currInnerRadius) / numStepsOnOuterPerimeter);
                } else {
                    lappa.step(-innerMotor);
                }
            }
        }
    }

    /*public void spiralRecordResult() {
        ArrayList<String> coveragePercentage = new ArrayList<>();
        ArrayList<String> time = new ArrayList<>();
        int numSteps = 0;
        float outerMotor = 80;
        float innerMotor = 0;
        boolean isFalling = false;

        while (!isFalling) {

            if (numSteps % 10 == 0) {
                double percent = world.getCoveragePercentage();
                System.out.println(percent);
                float absAngle = lappa.getAbsoluteMotorMovement();
                float t = convertAbsAngleToTimeInMinutes(absAngle, numSteps);
                coveragePercentage.add(Double.toString(percent));
                time.add(Float.toString(t));
            }

            if (lappa.getIsRedFixed()) {
                isFalling = lappa.stepWithoutSim(outerMotor);
                accOuterDegree += outerMotor;
            } else {
                if (hasRotatedEntireCircle()) {
                    currOuterRadius += 30;
                    currInnerRadius += 30;
                    isFalling = lappa.stepWithoutSim(-10 - (innerMotor/2));
                    double lengthOfCleanedArc = getLengthOfCleanedArc(currOuterRadius);
                    double numStepsOnOuterPerimeter = Math.floor(getLengthOfPerimeter(currOuterRadius) / lengthOfCleanedArc);
                    innerMotor = (float) Math.floor(getLengthOfPerimeter(currInnerRadius) / numStepsOnOuterPerimeter);
                    accOuterDegree = 0;
                } else {
                    isFalling = lappa.stepWithoutSim(-innerMotor);
                }
            }

            numSteps++;
        }

        System.out.println("World is covered");
        coverageResults.add(convertListToArray(coveragePercentage));
        timeResults.add(convertListToArray(time));
    }*/

    private double getLengthOfPerimeter(double radius) { return 2*Math.PI*radius; }

    private double getLengthOfCleanedArc(double degree, double radius) { return (degree/360) * (2*Math.PI*radius); }

    private boolean hasRotatedEntireCircle() {
        double lengthOfCleanedArc = getLengthOfCleanedArc(accOuterDegree, currOuterRadius);
        double lengthOfPerimeter = getLengthOfPerimeter(currOuterRadius);
        return lengthOfCleanedArc >= lengthOfPerimeter;
    }
}
