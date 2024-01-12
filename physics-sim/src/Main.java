import java.awt.*;

public class Main {
    public static void main(String[] args) {
        Loop l = new Loop();
        EventQueue.invokeLater(l::start);
    }
//    public static void main(String[] args) {
//        SegmentedWorld w = new SegmentedWorld(10, 1);
//        Particle p = new Disk(new Vector2(5, 2), new Vector2(1, 0), 1, Color.BLUE);
//        Particle q = new Disk(new Vector2(6.5, 2), new Vector2(0, 0), 1, Color.BLUE);
//
//        p.collide(w);
//        q.collide(w);
//        w.update();
//        System.out.println("Done");
//    }
}
