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
                    currOuterRadius += 15;
                    currInnerRadius += 15;
                    double lengthOfCleanedArc = getLengthOfCleanedArc(outerMotor, currOuterRadius);
                    double numStepsOnOuterPerimeter = Math.round(getLengthOfPerimeter(currOuterRadius) / lengthOfCleanedArc);
                    innerMotor = (float) Math.floor(getLengthOfPerimeter(currInnerRadius) / numStepsOnOuterPerimeter);
                    lappa.step(-innerMotor);
                    lappa.step(outerMotor);
                } else {
                    lappa.step(-innerMotor);
                }
            }
        }
    }

    private double getLengthOfPerimeter(double radius) { return 2*Math.PI*radius; }

    private double getLengthOfCleanedArc(double degree, double radius) { return (degree/360) * (2*Math.PI*radius); }

    private boolean hasRotatedEntireCircle() {
        double lengthOfCleanedArc = getLengthOfCleanedArc(accOuterDegree, currOuterRadius);
        double lengthOfPerimeter = getLengthOfPerimeter(currOuterRadius);
        return lengthOfCleanedArc >= lengthOfPerimeter;
    }
}
