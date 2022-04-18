import coppelia.IntW;
import coppelia.remoteApi;

public class Main
{
    public static void main(String[] args)
    {
        Simulator sim = new Simulator();
        World world = new World(4, 4, 0.4);
        Lappa lappa = new Lappa(sim, world);
        ZigZagController c = new ZigZagController(lappa, world);
        c.zigZagRecordResult();
        c.writeToFiles();

        world.preloadWorld();
        lappa.preloadAbsoluteMotorMovement();
        c.zigZagRecordResult();
        c.writeToFiles();

        sim.stopSimulation();
    }
}
            
