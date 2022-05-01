public class Main
{
    public static void main(String[] args)
    {
        Simulator sim = new Simulator();
        World world = new World(3, 3, 0.4);
        Lappa lappa = new Lappa(sim, world);
        RandomWalkController c = new RandomWalkController(lappa, world);
        c.randomWalkRecordResult();
        c.writeToFiles();

        for (int i = 1; i < 10; i++) {
            world.preloadWorld();
            lappa.preloadAbsoluteMotorMovement();
            c.randomWalkRecordResult();
            c.writeToFiles();
        }

        sim.stopSimulation();
    }
}
            
