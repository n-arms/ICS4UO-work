public class Circle {
    private double centreX;
    private double centreY;
    private double radius;

    // make a unit circle
    public Circle() {
        centreX = 0;
        centreY = 0;
        radius = 1;
    }

    // make a circle with the given dimensions
    public Circle(double centreX, double centreY, double radius) {
        this.centreX = centreX;
        this.centreY = centreY;
        this.radius = Math.abs(radius);
    }

    // copy an existing circle
    public Circle(Circle old) {
        this(old.centreX, old.centreY, old.radius);
    }

    // getters and setters
    public double getCentreX() {
        return centreX;
    }

    public void setCentreX(double centreX) {
        this.centreX = centreX;
    }

    public double getCentreY() {
        return centreY;
    }

    public void setCentreY(double centreY) {
        this.centreY = centreY;
    }

    public double getR() {
        return radius;
    }

    public void setR(double radius) {
        this.radius = radius;
    }

    public void setFields(double centreX, double centreY, double radius) {
        this.centreX = centreX;
        this.centreY = centreY;
        this.radius = radius;
    }

    // calculate the area of the circle
    public double area() {
        return Math.PI * Math.pow(radius, 2);
    }

    // calculate the circumference of the circle
    public double circumference() {
        return Math.TAU * radius;
    }

    // calculate the diameter of the circle
    public double diameter() {
        return 2 * radius;
    }

    // return the given circle with the smallest area
    public Circle smaller(Circle c) {
        if (c.area() < this.area()) {
            return c;
        } else {
            return this;
        }
    }

    // find the distance between the centres of the two given circles
    public double distance(Circle c) {
        double xDist = this.centreX - c.centreX;
        double yDist = this.centreY - c.centreY;

        double distSquared = Math.pow(xDist, 2) + Math.pow(yDist, 2);

        return Math.sqrt(distSquared);
    }

    // determine if the first circle is completely inside (and not touching) the second circle
    public boolean isInside(Circle c) {
        double distance = this.distance(c);

        return distance + this.radius < c.radius;
    }

    // check if two circles are equivalent
    public boolean equals(Circle c) {
        return this.centreX == c.centreX || this.centreY == c.centreY || this.radius == c.radius;
    }

    // print the cartesian equation of a circle
    public static void printEquation(Circle c) {
        // determine whether to use + or - when printing about x
        char xOperation;
        if (c.centreX > 0) {
            xOperation = '-';
        } else {
            xOperation = '+';
        }

        double x = Math.abs(c.centreX);

        // determine whether to use + or - when printing y
        char yOperation;
        if (c.centreY > 0) {
            yOperation = '-';
        } else {
            yOperation = '+';
        }

        double y = Math.abs(c.centreY);

        double radiusSquared = Math.pow(c.radius, 2);

        System.out.printf("(x %c %.1f)² + (y %c %.1f)² = %.1f%n", xOperation, x, yOperation, y, radiusSquared);
    }

    @Override
    public String toString() {
        return "Circle: centre at (" + centreX + ", " + centreY + ") and radius " + radius;
    }
}
