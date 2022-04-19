public class Chamber {
    private int joint1;
    private int joint2;
    private int dummy1;
    private int dummy2;

    public Chamber(int joint1, int joint2, int dummy1, int dummy2) {
        this.joint1 = joint1;
        this.joint2 = joint2;
        this.dummy1 = dummy1;
        this.dummy2 = dummy2;
    }

    public int getJoint1() { return joint1; }
    public int getJoint2() { return joint2; }
    public int getDummy1() { return dummy1; }
    public int getDummy2() { return dummy2; }
}