package OurCode.UWB.helpers;

public class Point2D {
    public int x;
    public int y;

    public Point2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point2D add(Point2D p2) {
        return new Point2D(x+p2.x, y+p2.y);
    }

    public Point2D div(int num) {
        return new Point2D(x/num, y/num);
    }

    public double dot(Point2D p2) {
        return x*p2.x + y*p2.y;
    }

    public Point2D sub(Point2D p2) {
        return new Point2D(x-p2.x, y-p2.y);
    }

    public double dist(Point2D p2) {
        return Math.sqrt(Math.pow((x - p2.x), 2) + Math.pow((y - p2.y), 2));
    }

    public String toString() {
        return "{x: " + x + ", y:" + y + "}";
    }

    public static double getAngle(Point2D a, Point2D b, Point2D c) {
        /*
        Returns the angle between the vectors ba and bc.
        Negative angle is anticlockwise
        Positive is clockwise
         */
        Point2D ba = a.sub(b);
        Point2D bc = b.sub(c);

        double thau1 = Math.atan2(ba.y, ba.x);
        double thau2 = Math.atan2(bc.y, bc.x);

//        return Math.toDegrees(thau1 - thau2) - 180;
        return Math.toDegrees(thau1 - thau2) + 180;

    }
}
