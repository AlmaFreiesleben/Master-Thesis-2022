import java.util.Random;

public class RandomWalkController extends Controller {

    public RandomWalkController(Lappa lappa, World world) {
        super(lappa, world);
    }

    public void clean() {
        while (!world.isAllCleaningZonesCovered(lappa.getCurrentCleaningZone())) {
            boolean isZoneCovered = randomWalk();
            if (isZoneCovered) {
                lappa.moveToNextCleaningZone();
            }
        }
    }

    private boolean randomWalk() {
        while (!world.isCleaningZoneCovered(lappa.getCurrentCleaningZone())) {
            float motor = new Random().nextInt(361) - 180;
            lappa.step(motor);
            lappa.step(motor);
        }
        return true;
    }
}