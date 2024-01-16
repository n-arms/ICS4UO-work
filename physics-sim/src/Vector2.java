public class Vector2 {
    private double x;
    private double y;
    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2() {
        this(0, 0);
    }

    public Vector2 add(Vector2 other) {
        return new Vector2(this.x + other.x, this.y + other.y);
    }

    public void addEquals(Vector2 other) {
        this.x += other.x;
        this.y += other.y;
    }

    public Vector2 sub(Vector2 other) {
        return this.add(other.scale(-1));
    }

    public Vector2 scale(double factor) {
        return new Vector2(x * factor, y * factor);
    }
    public Vector2 scaleX(double factor) {
        return new Vector2(x * factor, y);
    }
    public Vector2 scaleY(double factor) {
        return new Vector2(x, y * factor);
    }
    public Vector2 pairwiseMul(Vector2 other) {
        return this.scaleX(other.x).scaleY(other.y);
    }
    public double dot(Vector2 other) {
        return this.x * other.x + this.y * other.y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double magnitude() {
        return Math.sqrt(x*x + y*y);
    }

    public Vector2 normalize() {
        return this.scale(1 / this.magnitude());
    }

    @Override
    public String toString() {
        return "Vector2{" + "x=" + x + ", y=" + y + '}';
    }

    public Vector2 abs() {
        return new Vector2(Math.abs(x), Math.abs(y));
    }

    public Vector2 max(Vector2 other) {
        return new Vector2(Math.max(this.x, other.x), Math.max(this.y, other.y));
    }
}
