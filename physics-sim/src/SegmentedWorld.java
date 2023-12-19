import java.util.ArrayList;

public class SegmentedWorld {
    private final double size;
    private final int gridsPerSide = 10;
    private final ArrayList<Particle>[][] grid;
    public SegmentedWorld(double size) {
        this.size = size;
        grid = new ArrayList[gridsPerSide][gridsPerSide];
    }
    public void addParticle(int gridRow, int gridCol, Particle particle) {
        grid[gridRow][gridCol].add(particle);
    }
    public void clear() {
        for (var row : grid) {
            for (var pool : row) {
                pool.clear();
            }
        }
    }
}
