import coppelia.IntW;
import coppelia.remoteApi;

public class Main
{
    public static void main(String[] args)
    {
        Simulator sim = new Simulator();
        Lappa lappa = new Lappa(sim);
        RandomWalkController random = new RandomWalkController(lappa);
        random.randomWalk();
        //Test test = new Test(lappa);
        //test.test_movement_of_lappa_20_170();
        sim.stopSimulation();
    }
}
            
