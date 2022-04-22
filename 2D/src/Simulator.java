import coppelia.FloatWA;
import coppelia.IntW;
import coppelia.remoteApi;

public class Simulator {

    private remoteApi api;
    private final int clientID;
    private final int[] handles;

    public Simulator() {
        this.api = new remoteApi();
        api.simxFinish(-1);
        this.clientID = api.simxStart("127.0.0.1",19997,true,true,5000,5);
        this.handles  = new int[10];

        if (clientID != -1)
        {
            IntW objectHandles = new IntW(1);
            String[] joints = new String[]{"Floor", "Red_Dummy_1", "Red_Dummy_2", "Red_chamber", "Red_joint", "Cylinder", "Green_joint", "Green_chamber", "Green_Dummy_1", "Green_Dummy_2"};

            for (int i = 0; i < joints.length; i++) {
                api.simxGetObjectHandle(clientID, joints[i], objectHandles, api.simx_opmode_blocking);
                handles[i] = objectHandles.getValue();
            }

            api.simxStartSimulation(clientID, api.simx_opmode_blocking);
        } else {
            System.out.println("Failed connecting to remote API server");
        }
    }

    public void stopSimulation() {
        api.simxStopSimulation(clientID, api.simx_opmode_blocking);
        IntW pingTime = new IntW(0);
        api.simxGetPingTime(clientID,pingTime);
        api.simxFinish(clientID);
    }

    public int getFloor() {
        return handles[0];
    }

    public Chamber getRedChamber() {
        return new Chamber(handles[4], handles[1], handles[2], this);
    }

    public Chamber getGreenChamber() {
        return new Chamber(handles[6], handles[8], handles[9], this);
    }

    public void move(int joint, float angle) {
        api.simxSetJointTargetPosition(clientID, joint, (float) Math.toRadians(-angle), api.simx_opmode_blocking);
        sleep(500);
    }

    public FloatWA getPositionOfHandle(int handle) {
        FloatWA position = new FloatWA(3);
        api.simxGetObjectPosition(clientID, handle, -1, position, api.simx_opmode_blocking);
        return position;
    }

    public void freeChamberFromFloor(int dummy1, int dummy2) {
        api.simxSetObjectParent(clientID, dummy2, dummy1, true, api.simx_opmode_blocking);

        FloatWA position = new FloatWA(3);
        api.simxGetObjectPosition(clientID, dummy1, -1, position, api.simx_opmode_blocking);

        api.simxSetObjectPosition(clientID, dummy1, -1, position, api.simx_opmode_blocking);
        sleep(500);
    }

    public void fixChamberToFloor(int dummy2) {
        int floor = getFloor();
        api.simxSetObjectParent(clientID, dummy2, floor, true, api.simx_opmode_blocking);
        sleep(500);
    }

    public void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

}
