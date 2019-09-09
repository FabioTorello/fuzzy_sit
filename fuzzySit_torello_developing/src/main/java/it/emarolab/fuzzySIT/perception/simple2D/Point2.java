package it.emarolab.fuzzySIT.perception.simple2D;

public class Point2 {

    private double x, y;

    public Point2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }

    public double distance(Point2 p){
        return Math.sqrt((p.y - y) * (p.y - y) + (p.x - x) * (p.x - x));
    }

    @Override
    public String toString() {
       return "(x:" + x + ", y:" + y + ")";
    }
}
