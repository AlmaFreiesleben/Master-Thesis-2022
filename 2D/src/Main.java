public class Main
{
    public static void main(String[] args)
    {
        Simulator sim = new Simulator();
        World world = new World(3, 6, 0.4);
        Lappa lappa = new Lappa(sim, world);
        WallBumpController c = new WallBumpController(lappa, world);
        c.wallBumpRecordResult();
        c.writeToFiles();

        for (int i = 1; i < 10; i++) {
            world.preloadWorld();
            lappa.preloadAbsoluteMotorMovement();
            c.wallBumpRecordResult();
            c.writeToFiles();
        }

        sim.stopSimulation();
    }
}
            
