public class Vector2D {
    private double x;
    private double y;

    /**
     * Construct a cartesian vector with the given coordinates.
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Construct the zero vector.
     */
    public Vector2D() {
        this(0, 0);
    }

    /**
     * Compute the sum of two vectors.
     * @param other the vector to add
     * @return the sum
     */
    public Vector2D add(Vector2D other) {
        return new Vector2D(this.x + other.x, this.y + other.y);
    }

    /**
     * Compute the sum of two vectors, storing the result in the current vector.
     * @param other the vector to add into the current vector
     */
    public void addEquals(Vector2D other) {
        this.x += other.x;
        this.y += other.y;
    }

    /**
     * Compute the difference of two vectors.
     * @param other the vector to subtract
     * @return the difference
     */
    public Vector2D sub(Vector2D other) {
        return this.add(other.scale(-1));
    }

    /**
     * Compute the product of a vector and a scalar.
     * @param factor the scalar
     * @return the scalar product
     */
    public Vector2D scale(double factor) {
        return new Vector2D(x * factor, y * factor);
    }

    /**
     * Compute the pairwise product [a, b] * [c, d] = [ac, bd] of two vectors.
     * @param other the multiplicand
     * @return the product
     */
    public Vector2D pairwiseMul(Vector2D other) {
        return new Vector2D(this.x * other.x, this.y * other.y);
    }

    /**
     * Compute the dot product of two vectors
     * @param other the vector to dot with
     * @return the dot product
     */
    public double dot(Vector2D other) {
        return this.x * other.x + this.y * other.y;
    }

    /**
     * Get the x component of a cartesian vector.
     * @return the x component
     */
    public double getX() {
        return x;
    }

    /**
     * Get the y component of a cartesian vector.
     * @return the y component
     */
    public double getY() {
        return y;
    }

    /**
     * Compute the magnitude of a vector.
     * @return the magnitude
     */
    public double magnitude() {
        return Math.sqrt(x*x + y*y);
    }

    /**
     * Find a unit vector on the same span as the given vector.
     * @return the unit vector
     */
    public Vector2D normalize() {
        return this.scale(1 / this.magnitude());
    }

    /**
     * Compute the component-wise absolute value of a vector.
     * @return the component-wise absolute value
     */
    public Vector2D abs() {
        return new Vector2D(Math.abs(x), Math.abs(y));
    }

    /**
     * Compute the component-wise maximum between two vectors.
     * @param other the vector to compare to
     * @return the component-wise maximum
     */
    public Vector2D max(Vector2D other) {
        return new Vector2D(Math.max(this.x, other.x), Math.max(this.y, other.y));
    }

    /**
     * Convert a vector to a human-readable string
     * @return the string
     */
    @Override
    public String toString() {
        return "Vector2{" + "x=" + x + ", y=" + y + '}';
    }
}
