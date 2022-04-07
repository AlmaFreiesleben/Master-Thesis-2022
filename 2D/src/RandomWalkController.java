import java.util.*;

public class RandomWalkController {

    Lappa lappa;
    World world;

    public RandomWalkController(Lappa lappa, World world) {
        this.lappa = lappa;
        this.world = world;
    }

    public void randomWalk() {
        while (!world.isCovered()) {
            float motor = new Random().nextInt(361) - 180;

            world.printCoverage(); // TODO remove

            if (lappa.getIsRedFixed()) {
                lappa.step(motor);
            } else {
                lappa.step(motor);
            }
        }
        System.out.println("World is covered");
    }
}