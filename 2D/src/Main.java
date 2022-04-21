public class Main
{
    public static void main(String[] args)
    {
        Simulator sim = new Simulator();
        World world = new World(5, 5, 0.4);
        Lappa lappa = new Lappa(sim, world);
        SpiralController c = new SpiralController(lappa, world);
        c.spiralWalk();
        /*c.randomWalkRecordResult();
        c.writeToFiles();

        world.preloadWorld();
        lappa.preloadAbsoluteMotorMovement();
        c.randomWalkRecordResult();
        c.writeToFiles();

        world.preloadWorld();
        lappa.preloadAbsoluteMotorMovement();
        c.randomWalkRecordResult();
        c.writeToFiles();

        world.preloadWorld();
        lappa.preloadAbsoluteMotorMovement();
        c.randomWalkRecordResult();
        c.writeToFiles();

        world.preloadWorld();
        lappa.preloadAbsoluteMotorMovement();
        c.randomWalkRecordResult();
        c.writeToFiles();

        world.preloadWorld();
        lappa.preloadAbsoluteMotorMovement();
        c.randomWalkRecordResult();
        c.writeToFiles();

        world.preloadWorld();
        lappa.preloadAbsoluteMotorMovement();
        c.randomWalkRecordResult();
        c.writeToFiles();

        world.preloadWorld();
        lappa.preloadAbsoluteMotorMovement();
        c.randomWalkRecordResult();
        c.writeToFiles();

        world.preloadWorld();
        lappa.preloadAbsoluteMotorMovement();
        c.randomWalkRecordResult();
        c.writeToFiles();

        world.preloadWorld();
        lappa.preloadAbsoluteMotorMovement();
        c.randomWalkRecordResult();
        c.writeToFiles();*/

        sim.stopSimulation();
    }
}
            
