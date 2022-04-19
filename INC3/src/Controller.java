import coppelia.remoteApi;

public class Controller {

    protected int clientID;
    protected remoteApi sim;
    protected int floor;
    protected int middle_joint;
    protected Chamber rightChamber;
    protected Chamber leftChamber;

    public Controller(int clientID, remoteApi sim, int[] handles) {
        this.clientID = clientID;
        this.sim = sim;
        this.floor = handles[0];
        this.middle_joint = handles[1];
        this.rightChamber = new Chamber(handles[2], handles[3], handles[4], handles[5]);
        this.leftChamber = new Chamber(handles[6], handles[7], handles[8], handles[9]);
    }
}

