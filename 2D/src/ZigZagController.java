public class ZigZagController {

    private final Lappa lappa;
    private final World world;

    public ZigZagController(Lappa lappa, World world) {
        this.lappa = lappa;
        this.world = world;
    }

    public void zigZagWalk() {
        while (!world.isCovered()) {
            float motor = 75;

            if (lappa.getIsRedFixed()) {
                lappa.step(motor);
            } else {
                lappa.step(-motor);
            }
        }
        System.out.println("World is covered");
    }
}
