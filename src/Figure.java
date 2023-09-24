import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Figure {
    private int x;
    private int y;
    private int size;
    private String shapeType;
    private Color color;

    public Figure(int x, int y, int size, String shapeType, Color color) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.shapeType = shapeType;
        this.color = color;
    }
    public boolean containsPoint(Point point) {
        if (shapeType.equals("Square")) {
            Rectangle bounds = new Rectangle(x-size/2, y-size/2, size, size);
            return bounds.contains(point);
        } else if (shapeType.equals("Circle")) {
            Ellipse2D.Double bounds = new Ellipse2D.Double(x-size/2, y-size/2, size, size);
            return bounds.contains(point);
        }
        return false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSize() {
        return size;
    }

    public String getShapeType() {
        return shapeType;
    }

    public Color getColor() {
        return color;
    }
}
