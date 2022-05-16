import coppelia.FloatWA;
import coppelia.IntW;
import coppelia.remoteApi;
import javafx.geometry.Point3D;

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
            String[] joints = new String[]{"Floor", "Middle_Joint", "Right_Middle_Joint", "Right_Joint", "Right_Dummy_1", "Right_Dummy_2", "Left_Middle_Joint", "Left_Joint", "Left_Dummy_1", "Left_Dummy_2"};

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

    private int getFloor() {
        return handles[0];
    }

    public Chamber getRedChamber() {
        return new Chamber(handles[7], handles[8], handles[9], this);
    }

    public Chamber getGreenChamber() {
        return new Chamber(handles[3], handles[4], handles[5], this);
    }

    public void move(int joint, float angle) {
        api.simxSetJointTargetPosition(clientID, joint, (float) Math.toRadians(-angle), api.simx_opmode_blocking);
        sleep(1000);
    }

    public FloatWA getPositionOfHandle(int handle) {
        FloatWA position = new FloatWA(3);
        api.simxGetObjectPosition(clientID, handle, -1, position, api.simx_opmode_blocking);
        return position;
    }

    public Point3D getPositionOfObject(int objectHandle) {
        float[] pos = getPositionOfHandle(objectHandle).getArray();
        double f = 0.5;
        double x = f * Math.round(pos[0]/f);
        double y = f * Math.round(pos[1]/f);
        double z = f * Math.round(pos[2]/f);
        return new Point3D(x, y, z);
    }

    public void freeChamberFromFloor(int dummy1, int dummy2) {
        api.simxSetObjectParent(clientID, dummy2, dummy1, true, api.simx_opmode_blocking);

        FloatWA position = new FloatWA(3);
        api.simxGetObjectPosition(clientID, dummy1, -1, position, api.simx_opmode_blocking);

        api.simxSetObjectPosition(clientID, dummy1, -1, position, api.simx_opmode_blocking);
        sleep(1000);
    }

    public void fixChamberToFloor(int dummy2) {
        api.simxSetObjectParent(clientID, dummy2, getFloor(), true, api.simx_opmode_blocking);
        sleep(1000);
    }

    public void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}

