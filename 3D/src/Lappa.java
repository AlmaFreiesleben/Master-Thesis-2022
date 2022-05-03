import javafx.geometry.Point3D;

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
        }
        c.freeChamberFromFloor();
        //world.updateCoverage(c.whatCleaningZone(), c.getCurrentPosition());
        isRedFixed = !isRedFixed;
    }

    private boolean isValid() {
        Chamber moving = (isRedFixed) ? greenChamber : redChamber;
        Point3D p = moving.getCurrentPosition();
        return p.getZ() >= 0;
    }
}

