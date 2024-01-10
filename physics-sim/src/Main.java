import java.awt.*;

public class Main {
    public static void main(String[] args) {
        Loop l = new Loop();
        EventQueue.invokeLater(l::start);
    }
//    public static void main(String[] args) {
//        new Disk(new Vector2(0, 0), 20, Color.GREEN).collide(new SegmentedWorld(10, 5) {
//            @Override
//            public void addParticle(int gridRow, int gridCol, Particle p) {
//                System.out.printf("Adding circle at row %s and col %s.%n", gridRow, gridCol);
//            }
//        });
//    }
}
