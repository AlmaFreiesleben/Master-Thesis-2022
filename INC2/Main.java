import coppelia.IntW;
import coppelia.remoteApi;

public class Main
{
    public static void main(String[] args)
    {
        System.out.println("Program started");
        remoteApi sim = new remoteApi();
        sim.simxFinish(-1); // just in case, close all opened connections
        int clientID = sim.simxStart("127.0.0.1",19997,true,true,5000,5);
        if (clientID!=-1)
        {
            System.out.println("Connected to remote API server");   

            // Now try to retrieve data in a blocking fashion (i.e. a service call):
            IntW objectHandles = new IntW(1);
            String[] joints = new String[]{"Left_joint", "Right_joint"};
            int[] handles = new int[2];

            for (int i = 0; i < joints.length; i++) {
                sim.simxGetObjectHandle(clientID, joints[i], objectHandles, sim.simx_opmode_blocking);
                handles[i] = objectHandles.getValue();
            }

            sim.simxStartSimulation(clientID,sim.simx_opmode_blocking);

            for (var h : handles) System.out.println(h);
                
            sleep(2000);

            float degrees = 180;
            sim.simxSetJointTargetPosition(clientID, handles[1], degrees, sim.simx_opmode_oneshot_wait);
            
            sleep(10000);
            
            sim.simxStopSimulation(clientID,sim.simx_opmode_blocking);

            // Before closing the connection to CoppeliaSim, make sure that the last command sent out had time to arrive. You can guarantee this with (for example):
            IntW pingTime = new IntW(0);
            sim.simxGetPingTime(clientID,pingTime);

            // Now close the connection to CoppeliaSim:   
            sim.simxFinish(clientID);
        }
        else
            System.out.println("Failed connecting to remote API server");
        System.out.println("Program ended");
    }

    public static void sleep(int millis) {
        try
            {
                Thread.sleep(millis);
            }
            catch(InterruptedException ex)
            {
                Thread.currentThread().interrupt();
            }
    }
}
            
