public class Main
{
    public static void main(String[] args)
    {
        Simulator sim = new Simulator();
        World world = new World(2.5, 0.4);
        Lappa lappa = new Lappa(sim, world);
        RandomWalkController c = new RandomWalkController(lappa, world);
        c.randomWalk();
        sim.stopSimulation();
    }
}
            
