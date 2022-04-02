public class Chamber {
    private int motorDir;
    private int joint;
    private int dummy1;
    private int dummy2;

    public Chamber(int joint, int dummy1, int dummy2, int motorDir) {
        this.joint = joint;
        this.dummy1 = dummy1;
        this.dummy2 = dummy2;
        this.motorDir = motorDir;
    }

    public int getJoint() { return joint; }
    public int getDummy1() { return dummy1; }
    public int getDummy2() { return dummy2; }
    public int getMotorDir() {return motorDir; }
}