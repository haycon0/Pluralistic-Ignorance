import javax.swing.*;
import java.awt.*;

public class MediaDisplay {
    Environment environment;
    Display display;

    MediaDisplay(Environment environment, Display display) {
        this.environment = environment;
        this.display = display;
    }

    public JPanel getButtons() {

        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(800, 200));
        panel.setLayout(new GridLayout(0, 4, 1, 1));
        JButton simButton = new JButton("Simulate Interaction");
        JTextField simText = new JTextField("0");
        JButton addMediaButton = new JButton("Add Media");
        JButton addPeopleButton = new JButton("Add People");
        JButton addBeliefButton = new JButton("Add Belief");
        JButton displayMedia = new JButton("Display Media");
        JButton displayPeople = new JButton("Display People");
        JButton reset = new JButton("Reset");
        simText.setPreferredSize(new Dimension(100, 25));

        panel.add(simButton);
        panel.add(simText);
        panel.add(addMediaButton);
        panel.add(addPeopleButton);
        panel.add(addBeliefButton);
        panel.add(displayMedia);
        panel.add(displayPeople);
        panel.add(reset);

        simButton.addActionListener(e -> simInteraction(simText));

        addPeopleButton.addActionListener(e -> addPeople());

        addMediaButton.addActionListener(e -> addMedia());

        addBeliefButton.addActionListener(e -> addBelief());

        displayMedia.addActionListener(e -> displayMedia());

        displayPeople.addActionListener(e -> displayPeople());

        reset.addActionListener(e -> display.reset());

        return panel;
    }

    private void displayMedia() {
        JFrame frame = new JFrame("Media Display");
        int mediaNumber = environment.getMediaList().size();
        int numOfColumns = (int) Math.ceil((double) mediaNumber / 50);
        int height = (mediaNumber + 1) * 50;
        if (height > 800)
            height = 800;
        frame.setSize(new Dimension(300, height));
        frame.setLayout(new GridLayout(0, 2 * numOfColumns, 1, 1));
        frame.setVisible(true);
        for (int x = 0; x < numOfColumns; x++) {
            frame.add(new JLabel("Type"));
            frame.add(new JLabel("Prevalence"));
        }
        for (Media media : environment.getMediaList()) {
            Color color = Circle.getColor(media.getBelief().getId());
            JLabel typeLabel = new JLabel();
            JLabel prevalenceLabel = new JLabel(Integer.toString(media.getPrevalence()));
            if (media.isPopularMedia()) {
                color = Circle.getColor(environment.getPopularBelief().getId());
                typeLabel.setText("Popular");
            } else if (media.getRhetor() != null) {
                typeLabel.setText("Personal " + media.getRhetor().getId());
            } else {
                typeLabel.setText("Normal");
            }
            typeLabel.setOpaque(true);
            typeLabel.setBackground(color);
            prevalenceLabel.setOpaque(true);
            prevalenceLabel.setBackground(color);
            frame.add(typeLabel);
            frame.add(prevalenceLabel);
        }

        // console print
        /*System.out.println("id   belief   prevalence   popular   rhetor");
        for (Media media : environment.getMediaList()) {
            System.out.println(media.getId() + " " + media.getBelief().getName() + " " +
                    media.getPrevalence() + " " + media.isPopularMedia() + " "
                    + media.getRhetor());
        }*/
    }

    private void displayPeople() {
        JFrame frame = new JFrame("Display People");
        int peopleNumber = environment.getPersonList().size();
        int numOfColumns = (int) Math.ceil((double) peopleNumber / 50);
        int height = (peopleNumber + 1) * 50;
        if (height > 800)
            height = 800;
        frame.setSize(new Dimension(400 * numOfColumns, height));
        frame.setLayout(new GridLayout(0, 2 * numOfColumns, 1, 1));
        frame.setVisible(true);
        for (int x = 0; x < numOfColumns; x++) {
            frame.add(new JLabel("Name"));
            frame.add(new JLabel("Conviction"));
        }
        for (int x = 0; x < numOfColumns; x++) {
            frame.add(new JLabel("(Color of base Belief)"));
            frame.add(new JLabel("(Color of Expressed Belief)"));
        }
        for (Person person : environment.getPersonList()) {
            Color colorBase = Circle.getColor(person.getBaseBelief().getId());
            Color colorExpressed = Circle.getColor(person.expressBelief().getId());
            JLabel nameLabel = new JLabel(person.getName());
            JLabel convictionLabel = new JLabel(Integer.toString(person.getConviction()));
            nameLabel.setOpaque(true);
            nameLabel.setBackground(colorBase);
            convictionLabel.setOpaque(true);
            convictionLabel.setBackground(colorExpressed);
            frame.add(nameLabel);
            frame.add(convictionLabel);
        }
    }

    private void simInteraction(JTextField simText) {
        try {
            int simNum = Integer.parseInt(simText.getText());
            if (simNum >= 0) {
                environment.sim_interactions(simNum);
            } else {
                simText.setText("Invalid");
            }
        } catch (NumberFormatException e) {
            simText.setText("Invalid");
        } catch (Exception ignored) {
            simText.setText("Error");
        }
        display.update();
    }


    private void addPeople() {
        JFrame frame = new JFrame("Add Person");
        frame.setLayout(new GridLayout(0, 2, 1, 1));
        frame.setSize(new Dimension(400, 200));


        JLabel log = new JLabel("Replace following Fields");
        JLabel blank = new JLabel();
        JLabel countText = new JLabel("# of People");
        JTextField count = new JTextField();
        JLabel beliefIDText = new JLabel("ID of Belief");
        JTextField beliefID = new JTextField();
        JLabel avgConvictionText = new JLabel("Average Conviction of People");
        JTextField avgConviction = new JTextField();
        JLabel sdConvictionText = new JLabel("Std Dev of Conviction");
        JTextField sdConviction = new JTextField();
        JCheckBox addMedia = new JCheckBox("Add with Personal Media");
        JButton submit = new JButton("Submit");


        frame.setVisible(true);
        frame.add(log);
        frame.add(blank);
        frame.add(countText);
        frame.add(count);
        frame.add(beliefIDText);
        frame.add(beliefID);
        frame.add(avgConvictionText);
        frame.add(avgConviction);
        frame.add(sdConvictionText);
        frame.add(sdConviction);
        frame.add(addMedia);
        frame.add(submit);


        submit.addActionListener(e -> {

            int ct = 0, belief = 0, avgCon = 0, sdCon = 0;
            boolean valid = true;
            try {
                ct = Integer.parseInt(count.getText());
                if (ct < 0) {
                    count.setText("Invalid");
                    valid = false;
                }
            } catch (Exception e1) {
                count.setText("Invalid");
                valid = false;
            }
            try {
                belief = Integer.parseInt(beliefID.getText());
                if (belief >= environment.getBeliefList().size() || belief < 0) {
                    beliefID.setText("Invalid");
                    valid = false;
                }
            } catch (Exception e2) {
                beliefID.setText("Invalid");
                valid = false;
            }
            try {
                avgCon = Integer.parseInt(avgConviction.getText());
                if (avgCon < 0) {
                    avgConviction.setText("Invalid");
                    valid = false;
                }
            } catch (Exception e3) {
                avgConviction.setText("Invalid");
                valid = false;
            }
            try {
                sdCon = Integer.parseInt(sdConviction.getText());
                if (sdCon < 0) {
                    sdConviction.setText("Invalid");
                    valid = false;
                }
            } catch (Exception e4) {
                sdConviction.setText("Invalid");
                valid = false;
            }
            if (valid) {
                if (addMedia.isSelected()) {
                    JFrame frame1 = new JFrame("Add Person with Media");
                    frame1.setLayout(new GridLayout(0, 2, 1, 1));
                    frame1.setSize(new Dimension(400, 150));
                    frame1.setVisible(true);
                    JLabel log1 = new JLabel("Replace following Fields");
                    JLabel blank1 = new JLabel();
                    JLabel avgPrevalenceText = new JLabel("Average Prevalence of Media");
                    JTextField avgPrevalence = new JTextField();
                    JLabel sdPrevalenceText = new JLabel("Std Dev of Prevalence");
                    JTextField sdPrevalence = new JTextField();
                    JButton submit1 = new JButton("Submit");
                    frame1.add(log1);
                    frame1.add(blank1);
                    frame1.add(avgPrevalenceText);
                    frame1.add(avgPrevalence);
                    frame1.add(sdPrevalenceText);
                    frame1.add(sdPrevalence);
                    frame1.add(submit1);
                    int tempBelief = belief;
                    int tempAvgCon = avgCon;
                    int tempSdCon = sdCon;
                    int tempCt = ct;
                    submit1.addActionListener(e12 -> {
                        int avgPrev = 0, sdPrev = 0;
                        boolean valid1 = true;
                        try {
                            avgPrev = Integer.parseInt(avgPrevalence.getText());
                            if (avgPrev <= 0) {
                                avgPrevalence.setText("Invalid");
                                valid1 = false;
                            }
                        } catch (Exception e3) {
                            avgPrevalence.setText("Invalid");
                            valid1 = false;
                        }
                        try {
                            sdPrev = Integer.parseInt(sdPrevalence.getText());
                            if (sdPrev < 0) {
                                sdPrevalence.setText("Invalid");
                                valid1 = false;
                            }
                        } catch (Exception e4) {
                            sdPrevalence.setText("Invalid");
                            valid1 = false;
                        }
                        if (valid1) {
                            environment.addPersonsWithMedia(tempBelief, avgPrev, sdPrev, tempAvgCon, tempSdCon, tempCt);
                            frame1.dispose();
                            display.update();
                        }
                    });
                } else {
                    environment.addPeople(belief, avgCon, sdCon, ct);
                    display.update();
                }
                frame.dispose();
            } else {
                log.setText("Invalid. Reenter Fields");
                log.setOpaque(true);
                log.setBackground(Color.RED);
            }
        });
    }

    private void addMedia() {
        JFrame frame = new JFrame("Add Media");
        frame.setSize(new Dimension(400, 250));
        frame.setLayout(new GridLayout(0, 2, 1, 1));


        JLabel log = new JLabel("Replace following Fields");
        JLabel blank = new JLabel();
        JLabel countText = new JLabel("# of Media");
        JTextField count = new JTextField();
        JLabel beliefIDText = new JLabel("ID of Belief");
        JTextField beliefID = new JTextField();
        JLabel avgPrevalenceText = new JLabel("Average Prevalence of Media");
        JTextField avgPrevalence = new JTextField();
        JLabel sdPrevalenceText = new JLabel("Std Dev of Prevalence");
        JTextField sdPrevalence = new JTextField();
        JCheckBox popular = new JCheckBox("Popular Media");
        JCheckBox personal = new JCheckBox("Personal Media");
        JButton submit = new JButton("Submit");


        frame.setVisible(true);
        frame.add(log);
        frame.add(blank);
        frame.add(countText);
        frame.add(count);
        frame.add(beliefIDText);
        frame.add(beliefID);
        frame.add(avgPrevalenceText);
        frame.add(avgPrevalence);
        frame.add(sdPrevalenceText);
        frame.add(sdPrevalence);
        frame.add(popular);
        frame.add(personal);
        frame.add(submit);


        submit.addActionListener(e -> {

            int ct = 0, belief = 0, avg = 0, sd = 0;
            boolean valid = true;
            try {
                ct = Integer.parseInt(count.getText());
                if (ct < 0) {
                    count.setText("Invalid");
                    valid = false;
                }
            } catch (Exception e1) {
                count.setText("Invalid");
                valid = false;
            }
            try {
                belief = Integer.parseInt(beliefID.getText());
                if (belief >= environment.getBeliefList().size() || belief < 0) {
                    beliefID.setText("Invalid");
                    valid = false;
                }
            } catch (Exception e2) {
                beliefID.setText("Invalid");
                valid = false;
            }
            try {
                avg = Integer.parseInt(avgPrevalence.getText());
                if (avg <= 0) {
                    avgPrevalence.setText("Invalid");
                    valid = false;
                }
            } catch (Exception e3) {
                avgPrevalence.setText("Invalid");
                valid = false;
            }
            try {
                sd = Integer.parseInt(sdPrevalence.getText());
                if (sd < 0) {
                    sdPrevalence.setText("Invalid");
                    valid = false;
                }
            } catch (Exception e4) {
                sdPrevalence.setText("Invalid");
                valid = false;
            }
            if (personal.isSelected() && popular.isSelected()) {
                valid = false;
            }
            if (valid) {
                if (popular.isSelected()) {
                    environment.addPopularMedias(belief, avg, sd, ct);
                    display.update();
                } else if (personal.isSelected()) {
                    JFrame frame1 = new JFrame("Add Personal Media");
                    frame1.setLayout(new BoxLayout(frame1.getContentPane(), BoxLayout.Y_AXIS));
                    frame1.setSize(new Dimension(400, 200));
                    frame1.setVisible(true);
                    JLabel log1 = new JLabel("Replace following Fields");
                    JLabel blank1 = new JLabel();
                    JLabel personIDText = new JLabel("Person ID to attach media");
                    JTextField personID = new JTextField();
                    JButton submit1 = new JButton("Submit");
                    frame1.add(log1);
                    frame1.add(blank1);
                    frame1.add(personIDText);
                    frame1.add(personID);
                    frame1.add(submit1);
                    int tempBelief = belief;
                    int tempAvg = avg;
                    int tempSd = sd;


                    submit1.addActionListener(e12 -> {
                        int person = -1;
                        boolean valid1 = true;
                        try {
                            person = Integer.parseInt(personID.getText());
                            if (person >= environment.getPersonList().size() || person < 0) {
                                personID.setText("Invalid");
                                valid1 = false;
                            }
                        } catch (Exception e1) {
                            personID.setText("Invalid");
                            valid1 = false;
                        }
                        if (valid1) {
                            environment.addPersonalMedia(tempBelief, tempAvg, tempSd, person);
                            frame1.dispose();
                            display.update();
                        }
                    });
                } else {
                    environment.addMedias(belief, avg, sd, ct);
                    display.update();
                }
                frame.dispose();
            } else {
                if (personal.isSelected() && popular.isSelected()) {
                    log.setText("Can't select both popular and personal");
                } else {
                    log.setText("Invalid. Reenter Fields");
                }
                log.setOpaque(true);
                log.setBackground(Color.RED);
            }
        });
    }

    private void addBelief() {
        JFrame frame = new JFrame("Add Belief");
        frame.setSize(new Dimension(300, 100));
        frame.setLayout(new GridLayout(0, 2, 1, 1));


        JLabel log = new JLabel("Replace following Fields");
        JLabel blank = new JLabel();
        JLabel countText = new JLabel("# of Beliefs");
        JTextField count = new JTextField();
        JButton submit = new JButton("Submit");


        frame.setVisible(true);
        frame.add(log);
        frame.add(blank);
        frame.add(countText);
        frame.add(count);
        frame.add(submit);


        submit.addActionListener(e -> {
            int ct = 0;
            boolean valid = true;
            try {
                ct = Integer.parseInt(count.getText());
                if (ct < 0) {
                    count.setText("Invalid");
                    valid = false;
                }
            } catch (Exception e1) {
                count.setText("Invalid");
                valid = false;
            }
            if (valid) {
                environment.addBeliefs(ct);
                frame.dispose();
                display.update();
            } else {
                log.setText("Invalid. Reenter Fields");
                log.setOpaque(true);
                log.setBackground(Color.RED);
            }
        });
    }

    public void reset(Environment environment) {
        this.environment = environment;
    }
}
