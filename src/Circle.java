import java.awt.*;

public class Circle {

    int x, y, diameter;
    Color topColor;
    Color bottomColor;

    public Circle(int x, int y, int top, int bottom, int diameter) {
        this.x = x;
        this.y = y;
        this.diameter = diameter;
        topColor = getColor(top);
        bottomColor = getColor(bottom);
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(topColor);
        g2d.fillArc(x, y, diameter, diameter, 0, 180);
        g2d.setColor(bottomColor);
        g2d.fillArc(x, y, diameter, diameter, 180, 180);
    }


    public static Color getColor(int num) {
        Color color;
        switch (num) {
            case 0:
                color = Color.MAGENTA;
                break;
            case 1:
                color = Color.ORANGE;
                break;
            case 2:
                color = Color.RED;
                break;
            case 3:
                color = Color.YELLOW;
                break;
            case 4:
                color = Color.GREEN;
                break;
            case 5:
                color = Color.PINK;
                break;
            case 6:
                color = Color.CYAN;
                break;
            case 7:
                color = Color.GRAY;
                break;
            case 8:
                color = Color.LIGHT_GRAY;
                break;
            case 9:
                color = Color.BLUE;
                break;
            default:
                color = Color.BLACK;
        }
        return color;
    }

}

