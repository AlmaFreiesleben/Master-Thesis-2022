import java.util.Random;

public class RandomWalkController extends Controller {

    public RandomWalkController(Lappa lappa, World world) {
        super(lappa, world);
    }

    public void randomWalk() {
        while (!world.isCovered()) {
            float motor = new Random().nextInt(361) - 180;
            lappa.step(motor);
            motor = new Random().nextInt(361) - 180;
            lappa.step(motor);
        }
    }
}