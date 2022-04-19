public class Main
{
    public static void main(String[] args)
    {
        Simulator sim = new Simulator();
        World world = new World(3.5, 3.5, 0.4);
        Lappa lappa = new Lappa(sim, world);
        ZigZagController c = new ZigZagController(lappa, world);
        c.zigZagWalk();
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
            
