import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class BeliefDisplay extends JComponent {
    private Vector<Belief> beliefs;
    private int size;
    Display display;

    BeliefDisplay(Vector<Belief> beliefs, Display display) {
        this.beliefs = beliefs;
        this.display = display;
        size = 0;
    }


    public void updateBeliefLabels(JPanel panel) {
        for (; size < beliefs.size(); size++) {
            JLabel label = new JLabel(beliefs.get(size).getName());
            label.setOpaque(true);
            label.setBackground(Circle.getColor(beliefs.get(size).getId()));
            panel.add(label);
            if (size > 8) //Changes font color to contrast with dark background
                label.setForeground(Color.WHITE);
        }
    }

    public void resetBeliefLabels(JPanel panel, Vector<Belief> beliefs) {

        for (int x = panel.getComponents().length; x > 0; x--)
            panel.remove(0);
        this.beliefs = beliefs;
        size = 0;
    }
}
