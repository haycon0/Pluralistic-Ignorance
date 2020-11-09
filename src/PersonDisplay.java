import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class PersonDisplay extends JComponent {
    private Vector<Circle> shapes;
    private int x;
    private int y;


    PersonDisplay(Vector<Person> people) {
        setPreferredSize((new Dimension(500, 500)));
        shapes = new Vector<>();
    }

    public void updatePeople(Vector<Person> people) {
        shapes = new Vector<>();
        int side = (int) Math.ceil(Math.sqrt(people.size()));
        if (side == 0)
            return;
        int diameter = 500 / side;
        x = 0;
        y = 0;
        for (int j = 0; j < people.size(); j++) {
            addCircle(people.get(j).expressBelief().getId(), people.get(j).getBaseBelief().getId(), diameter);
            x += diameter;
            if (j % side == side - 1) {
                y += diameter;
                x = 0;
            }
        }
    }


    public void paint(Graphics g) {
        for (Circle s : shapes) {
            s.draw(g);
        }
    }

    public void addCircle(int expressedBelief, int actualBelief, int diameter) {
        shapes.add(new Circle(x, y, expressedBelief, actualBelief, diameter));
        repaint();
    }
}
