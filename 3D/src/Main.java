public class Main
{
    public static void main(String[] args)
    {
        Simulator sim = new Simulator();
        World world = new World(4, 0.4);
        Lappa lappa = new Lappa(sim, world);
        RandomWalkController c = new RandomWalkController(lappa, world);
        c.clean();
        sim.stopSimulation();
    }
}
            
