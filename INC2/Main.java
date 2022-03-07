import coppelia.IntW;
import coppelia.remoteApi;

public class Main
{
    public static void main(String[] args)
    {
        System.out.println("Program started");
        remoteApi sim = new remoteApi();
        sim.simxFinish(-1); // just in case, close all opened connections
        int clientID = sim.simxStart("127.0.0.1",19999,true,true,5000,5);
        if (clientID!=-1)
        {
            System.out.println("Connected to remote API server");   

            // Now try to retrieve data in a blocking fashion (i.e. a service call):
            IntW objectHandles = new IntW(1);
            String[] joints = new String[]{"Floor", "Left_Dummy_1", "Left_Dummy_2", "Left_chamber", "Left_joint", "Cylinder", "Right_joint", "Right_chamber", "Right_Dummy_1", "Right_Dummy_2"};
            int[] handles = new int[10];

            for (int i = 0; i < joints.length; i++) {
                sim.simxGetObjectHandle(clientID, joints[i], objectHandles, sim.simx_opmode_blocking);
                handles[i] = objectHandles.getValue();
            }

            sim.simxStartSimulation(clientID,sim.simx_opmode_blocking);
                
            sleep(2000);

            // Run the random walk algorithm
            Controller c = new Controller(clientID, true, sim, handles); 
            c.randomWalk(4);

            sleep(2000);
            
            sim.simxStopSimulation(clientID,sim.simx_opmode_blocking);

            // Before closing the connection to CoppeliaSim, make sure that the last command sent out had time to arrive. You can guarantee this with (for example):
            IntW pingTime = new IntW(0);
            sim.simxGetPingTime(clientID,pingTime);

            // Now close the connection to CoppeliaSim:   
            sim.simxFinish(clientID);
        } else { 
            System.out.println("Failed connecting to remote API server"); 
        }

        System.out.println("Program ended");
    }

    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}
            
