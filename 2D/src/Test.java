public class Test {

    private final Lappa lappa;

    public Test(Lappa lappa) {
        this.lappa = lappa;
    }

    public void test_movement_of_one_chamber_90() {
        lappa.simpleStep(90);
        lappa.simpleStep(90);
        lappa.simpleStep(90);
        lappa.simpleStep(90);
        lappa.simpleStep(90);
        lappa.simpleStep(90);
    }

    public void test_movement_of_one_chamber_random_degrees() {
        for (int i = 0; i < 20; i++) {
            lappa.simpleStep(17);
        }

        for (int i = 0; i < 20; i++) {
            lappa.simpleStep(-17);
        }
    }

    public void test_movement_of_one_chamber_180() {
        for (int i = 0; i < 10; i++) {
            lappa.simpleStep(180);
        }
    }

    public void test_movement_of_one_chamber_170() {
        for (int i = 0; i < 10; i++) {
            lappa.simpleStep(-170);
        }
    }

    public void test_movement_of_lappa_20_minus40_minus40() {
        lappa.step(20);
        lappa.step(-40);
        lappa.step(-40);
    }

    public void test_movement_of_lappa_20_170() {
        lappa.step(20);
        lappa.step(170);
    }

    public void test_movement_of_lappa_minus20_minus170() {
        lappa.step(-20);
        lappa.step(-170);
    }

    public void test_movement_of_lappa_90_times_4() {
        lappa.step(90);
        lappa.step(90);
        lappa.step(90);
        lappa.step(90);
    }

    public void test_movement_of_lappa_4_moves() {
        lappa.step(10);
        lappa.step(30);
        lappa.step(-70);
        lappa.step(90);
    }
}
