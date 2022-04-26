public class Main
{
    public static void main(String[] args)
    {
        Simulator sim = new Simulator();
        World world = new World(5, 10, 0.4);
        Lappa lappa = new Lappa(sim, world);
        SnakeController c = new SnakeController(lappa, world);
        c.snakeWalkRecordResults();
        c.writeToFiles();

        /*for (int i = 1; i < 10; i++) {
            world.preloadWorld();
            lappa.preloadAbsoluteMotorMovement();
            c.snakeWalkRecordResults();
            c.writeToFiles();
        }*/

        sim.stopSimulation();
    }
}
            
