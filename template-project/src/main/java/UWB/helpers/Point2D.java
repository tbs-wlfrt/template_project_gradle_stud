package UWB.helpers;

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
}
