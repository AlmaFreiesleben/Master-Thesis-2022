import java.util.*;

public class RandomWalkController {

    Lappa lappa;

    public RandomWalkController(Lappa lappa) {
        this.lappa = lappa;
    }

    public void randomWalk() {
        while (true) { //TODO: while !isCovered()
            float motor = new Random().nextInt(361) - 180;

            if (lappa.getIsRedFixed()) {
                lappa.step(motor);
            } else {
                lappa.step(motor);
            }
        }
    }
}