public class Main
{
    public static void main(String[] args)
    {
        Simulator sim = new Simulator();
        World world = new World(3, 3, 0.4);
        Lappa lappa = new Lappa(sim, world);
        SnakeController c = new SnakeController(lappa, world);
        c.snakeWalk();
        //c.writeToFiles();
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
            
