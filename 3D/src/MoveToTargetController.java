import javafx.geometry.Point3D;

import java.util.Random;

public class MoveToTargetController extends Controller {

    public MoveToTargetController(Lappa lappa, World world) {
        super(lappa, world);
    }

    public void moveToTargetFromRandomPosition() {
        int randNumSteps = new Random().nextInt(6) + 5;
        moveToRandomPosition(true, randNumSteps);
        lappa.moveToTargetPoint(true, new Point3D(0,0,2.5));
        System.out.println("Target has been reached");
        System.out.println("Position of red chamber after moving towards target: " + lappa.getPositionRedChamber());
        System.out.println("Position of green chamber after moving towards target: " + lappa.getPositionGreenChamber());
    }

    private void moveToRandomPosition(boolean isPosHullSide, int randNumSteps) {
        for (int i = 0; i < randNumSteps; i++) {
            float motor = new Random().nextInt(361) - 180;
            lappa.step(motor, isPosHullSide);
            motor = new Random().nextInt(361) - 180;
            lappa.step(motor, isPosHullSide);
        }
        System.out.println("Position of red chamber before moving towards target: " + lappa.getPositionRedChamber());
        System.out.println("Position of green chamber before moving towards target: " + lappa.getPositionGreenChamber());
    }
}
