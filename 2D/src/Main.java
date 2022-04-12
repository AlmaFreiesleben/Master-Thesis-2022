import coppelia.IntW;
import coppelia.remoteApi;

public class Main
{
    public static void main(String[] args)
    {
        Simulator sim = new Simulator();
        World world = new World(5, 5, 0.4);
        Lappa lappa = new Lappa(sim, world);
        RandomWalkController random = new RandomWalkController(lappa, world);
        random.randomWalk();
        sim.stopSimulation();
    }
}
            
