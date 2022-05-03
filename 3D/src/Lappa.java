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
        } else {
            ArrayList<Point3D> points = c.getPointsOnArc();
            world.updateCoverage(points);
        }
        c.freeChamberFromFloor();
        isRedFixed = !isRedFixed;
    }

    private Point3D getPositionOfMovingChamber() {
        Chamber moving = (isRedFixed) ? greenChamber : redChamber;
        return moving.getCurrentPosition();
    }

    private boolean isValid() {
        Point3D p = getPositionOfMovingChamber();
        return p.getZ() > 1;
    }
}

