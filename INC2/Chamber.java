import java.util.Properties;

import com.apple.laf.ClientPropertyApplicator.Property;

public class Chamber {
    private int joint;
    private int dummy1;
    private int dummy2;

    public Chamber(int joint, int dummy1, int dummy2) {
        this.joint = joint;
        this.dummy1 = dummy1;
        this.dummy2 = dummy2;
    }

    public int getJoint() { return joint; }
    public int getDummy1() { return dummy1; }
    public int getDummy2() { return dummy2; }
}