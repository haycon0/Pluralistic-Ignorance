import javax.swing.*;
import java.awt.*;

public class Display extends JComponent {

    private final PersonDisplay personDisplay;
    private final BeliefDisplay beliefDisplay;
    private final MediaDisplay mediaDisplay;
    private Environment environment;
    private final JFrame frame;
    private JPanel beliefLabelPanel;

    Display() {
        environment = new Environment(0, 0, 0);
        beliefLabelPanel = new JPanel();
        beliefLabelPanel.setLayout(new BoxLayout(beliefLabelPanel, BoxLayout.X_AXIS));
        personDisplay = new PersonDisplay(environment.getPersonList());
        personDisplay.setBorder(BorderFactory.createLineBorder(Color.black));
        beliefDisplay = new BeliefDisplay(environment.getBeliefList(), this);
        mediaDisplay = new MediaDisplay(environment, this);
        frame = new JFrame("Window");
        JPanel buttons = mediaDisplay.getButtons();
        beliefLabelPanel = new JPanel();
        buttons.setBounds(100, 600, 600, 50);
        beliefLabelPanel.setBounds(600, 0, 175, 550);
        personDisplay.setBounds(0, 0, 550, 550);
        beliefDisplay.setBounds(600, 0, 25, 550);
        beliefDisplay.setLayout(null);
        frame.setSize(800, 800);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.add(beliefLabelPanel);
        frame.add(personDisplay);
        frame.add(beliefDisplay);
        frame.add(buttons);
        beliefDisplay.updateBeliefLabels(beliefLabelPanel);
        personDisplay.updatePeople(environment.getPersonList());
        update();
    }

    public void update() {
        personDisplay.updatePeople(environment.getPersonList());
        beliefDisplay.updateBeliefLabels(beliefLabelPanel);
        frame.setVisible(true);
    }

    public void reset() {
        this.environment = new Environment(0, 0, 0);
        beliefDisplay.resetBeliefLabels(beliefLabelPanel, environment.getBeliefList());
        mediaDisplay.reset(environment);
        frame.setVisible(false);
        update();
    }

    public static void main(String[] args) {
        Display display1 = new Display();
        display1.environment.addBeliefWithName("Republican");
        display1.environment.addBeliefWithName("Democrat");
        display1.environment.addPersonsWithMedia(0, 1, 0, 90, 0, 37);
        display1.environment.addPersonsWithMedia(1, 1, 0, 50, 0, 63);
        display1.update();
        Display display2 = new Display();
        display2.environment.addBeliefWithName("Republican");
        display2.environment.addBeliefWithName("Democrat");
        display2.environment.addPersonsWithMedia(0, 1, 0, 90, 0, 37);
        display2.environment.addPersonsWithMedia(1, 1, 0, 50, 0, 63);
        display2.update();
    }
}
