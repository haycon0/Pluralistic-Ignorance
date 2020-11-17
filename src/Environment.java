import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

public class Environment {
    private final Vector<Person> personList;
    private final Vector<Integer> expressedBeliefsCount;
    private final Vector<Media> mediaList;
    private final Vector<Integer> prevalenceList; // List of the sum of the sum of the prevalence's after corresponding media was added
    private final Vector<Belief> beliefList;
    private Belief popularBelief;


    Environment(int numPeople, int numMedia, int numBelief) {

        personList = new Vector<Person>();
        mediaList = new Vector<Media>();
        beliefList = new Vector<Belief>();
        prevalenceList = new Vector<Integer>();
        expressedBeliefsCount = new Vector<Integer>();
        popularBelief = null;

        // Creates Beliefs
        for (int x = 0; x < numBelief; x++) {
            addBelief();
        }

        // Creates People
        for (int x = 0; x < numPeople; x++) {
            System.out.println("Person" + x + ":");
            manualAddPerson();
        }

        // Creates Media
        for (int x = 0; x < numMedia; x++) {
            System.out.println("Media" + x + ":");
            manualAddMedia();
        }
    }

    public Vector<Person> getPersonList() {
        return personList;
    }

    public Vector<Belief> getBeliefList() {
        return beliefList;
    }

    public Vector<Media> getMediaList() {
        return mediaList;
    }

    public Belief getPopularBelief() {
        return popularBelief;
    }

    public void sim_interactions(int numberOfInteractions) {

        Random random = new Random();
        int personID; // ID of person in the interaction
        int prevalenceNum; // number used to determine media in the interaction
        int totalPrevalence = prevalenceList.lastElement(); // upper bound for prevalenceNum

        // binary search variables
        int searchBegin;
        int searchMid;
        int searchLast;

        // simulates user or environment defined number of interaction with random media and people
        for (int x = 0; x < numberOfInteractions; x++) {
            personID = random.nextInt(personList.size()); // gets id to select person at random from list
            prevalenceNum = random.nextInt(totalPrevalence); // gets int lower than the total prevalence to select media weighted by prevalence

            // Binary search to find ID of media that the prevalenceNum corresponds to
            searchLast = prevalenceList.size() - 1;
            searchBegin = 0;
            while (true) {
                searchMid = (searchLast + searchBegin) / 2;
                if (searchMid < 0 || searchMid >= mediaList.size())
                    break;
                //Checks to ensure personal media doesn't interact with itself
                if (prevalenceList.get(searchMid) > prevalenceNum && (searchMid == 0 || prevalenceList.get(searchMid - 1) <= prevalenceNum)) {
                    if (mediaList.get(searchMid).getRhetor() == null || mediaList.get(searchMid).getRhetor().getId() != personID) {
                        mediaInteraction(searchMid, personID);
                    } else {
                        if (!(mediaList.size() == 1 && personList.size() == 1)) { //If only single person and media exists don't want program to loop
                            x--; //Makes sure x interactions take place
                        }
                    }
                    break;
                }
                if (prevalenceList.get(searchMid) > prevalenceNum) {
                    searchLast = searchMid - 1;
                } else {
                    searchBegin = searchMid + 1;
                }
            }
        }
    }

    private void mediaInteraction(int mediaID, int personID) {

        double popularBeliefUbiquity; //The portion of people that express the popular belief
        Belief originalBelief;
        Belief updatedBelief;

        originalBelief = personList.get(personID).expressBelief();

        // calculates the ubiquity of the most popular belief then uses that to update media if its a popular media
        popularBeliefUbiquity = (double) expressedBeliefsCount.get(popularBelief.getId()) / personList.size();
        mediaList.get(mediaID).checkPopular(popularBeliefUbiquity, popularBelief);
        personList.get(personID).addInteraction(mediaList.get(mediaID));

        updatedBelief = personList.get(personID).expressBelief();

        // checks if the expressed belief of the person in the interaction has changed
        // then updates the expressed beliefs count
        if (originalBelief != updatedBelief) {
            expressedBeliefsCount.set(originalBelief.getId(), expressedBeliefsCount.get(originalBelief.getId()) - 1);
            expressedBeliefsCount.set(updatedBelief.getId(), expressedBeliefsCount.get(updatedBelief.getId()) + 1);

            // updates most popular belief if needed due to the change in belief count
            if (expressedBeliefsCount.get(updatedBelief.getId()) > expressedBeliefsCount.get(popularBelief.getId()))
                popularBelief = updatedBelief;
        }
    }

    private void addPerson(int conviction, int beliefID) {
        int id = personList.size();
        personList.add(new Person(id, beliefList.get(beliefID), conviction));

        //Adds the base belief of the person to the expressed belief count
        expressedBeliefsCount.set(beliefID, expressedBeliefsCount.get(beliefID) + 1);
        if (popularBelief == null)
            popularBelief = beliefList.get(beliefID);
        if (expressedBeliefsCount.get(beliefID) > expressedBeliefsCount.get(popularBelief.getId()))
            popularBelief = beliefList.get(beliefID);
    }

    private void addPersonWithName(int conviction, int beliefID, String name) {
        int id = personList.size();
        personList.add(new Person(id, beliefList.get(beliefID), conviction, name));

        //Adds the base belief of the person to the expressed belief count
        expressedBeliefsCount.set(beliefID, expressedBeliefsCount.get(beliefID) + 1);
        if (popularBelief == null)
            popularBelief = beliefList.get(beliefID);
        if (expressedBeliefsCount.get(beliefID) > expressedBeliefsCount.get(popularBelief.getId()))
            popularBelief = beliefList.get(beliefID);
    }

    public void addPeople(int beliefID, int avgConviction, double sd, int count) {
        int conviction;
        Random random = new Random();

        // adds count people with conviction randomly sampled from a normal distribution with defined parameters
        for (int x = 0; x < count; x++) {
            conviction = (int) (random.nextGaussian() * sd + avgConviction + 0.5);
            if (conviction < 0)
                conviction = 0;
            addPerson(conviction, beliefID);
        }
    }

    private void manualAddPerson() { // adds a person with parameters entered from terminal to environment
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Conviction:");
        int conviction = scanner.nextInt();
        System.out.println("Enter Base Belief ID:");
        int beliefID = scanner.nextInt();
        addPerson(conviction, beliefID);
    }

    private void addToMediaStructures(Media media) {
        mediaList.add(media);

        // adds previous total prevalence + new media prevalence to prevalence list to implement
        // prevalence weighted media interactions
        if (prevalenceList.size() == 0)
            prevalenceList.add(media.getPrevalence());
        else
            prevalenceList.add(prevalenceList.lastElement() + media.getPrevalence());
    }

    public void addPersonsWithMedia(int beliefID, int avgPrevalence, double sdPrev, int avgConviction, double sdCon, int count) {
        for (int x = 0; x < count; x++)
            addPersonWithMedia(beliefID, avgPrevalence, sdPrev, avgConviction, sdCon);
    }

    private void addPersonWithMedia(int beliefID, int avgPrevalence, double sdPrev, int avgConviction, double sdCon) {
        int conviction;
        Random random = new Random();
        int personID = personList.size();
        conviction = (int) (random.nextGaussian() * sdCon + avgConviction + 0.5);
        if (conviction < 0)
            conviction = 0;
        addPerson(conviction, beliefID);
        addPersonalMedia(beliefID, avgPrevalence, sdPrev, personID);
    }

    public void addMedias(int beliefID, int avgPrevalence, double sd, int count) {
        for (int x = 0; x < count; x++) {
            addMedia(beliefID, avgPrevalence, sd);
        }
    }

    private void addMedia(int beliefID, int avgPrevalence, double sd) {
        int prevalence;
        Random random = new Random();
        int id = mediaList.size();
        Media media;

        // adds popular media with prevalence randomly sampled from a normal distribution with defined parameters
        prevalence = (int) (random.nextGaussian() * sd + avgPrevalence + 0.5);
        while (prevalence < 1)
            prevalence = (int) (random.nextGaussian() * sd + avgPrevalence + 0.5);
        media = new Media(id, beliefList.get(beliefID), prevalence);
        addToMediaStructures(media);
    }

    public void addPopularMedias(int beliefID, int avgPrevalence, double sd, int count) {
        for (int x = 0; x < count; x++) {
            addPopularMedia(beliefID, avgPrevalence, sd);
        }
    }

    private void addPopularMedia(int beliefID, int avgPrevalence, double sd) {
        int prevalence;
        Random random = new Random();
        int id = mediaList.size();
        Media media;

        // adds popular media with prevalence randomly sampled from a normal distribution with defined parameters
        prevalence = (int) (random.nextGaussian() * sd + avgPrevalence + 0.5);
        while (prevalence < 1)
            prevalence = (int) (random.nextGaussian() * sd + avgPrevalence + 0.5);
        media = new Media(id, beliefList.get(beliefID), prevalence, true);
        addToMediaStructures(media);
    }

    public void addPersonalMedia(int beliefID, int avgPrevalence, double sd, int personID) {
        int prevalence;
        Random random = new Random();
        int id = mediaList.size();
        Media media;

        // adds personal media with prevalence randomly sampled from a normal distribution with defined parameters
        prevalence = (int) (random.nextGaussian() * sd + avgPrevalence + 0.5);
        while (prevalence < 1)
            prevalence = (int) (random.nextGaussian() * sd + avgPrevalence + 0.5);
        media = new Media(id, beliefList.get(beliefID), prevalence, personList.get(personID));
        addToMediaStructures(media);
    }

    private void manualAddMedia() { // adds a media with parameters entered from terminal to environment
        Scanner scanner = new Scanner(System.in);
        Media media;
        int id = mediaList.size();
        System.out.println("Enter prevalence:");
        int prevalence = scanner.nextInt();
        System.out.println("Enter Belief ID:");
        int beliefID = scanner.nextInt();
        System.out.println("Is this a popular media:");
        boolean popular = scanner.nextBoolean();

        // media can't be both popular and personal these if else statements create correct type of media
        if (!popular) {
            System.out.println("Is this a personal media:");
            boolean personal = scanner.nextBoolean();
            if (personal) {
                System.out.println("Enter person ID for personal media:");
                int personID = scanner.nextInt();
                media = new Media(id, beliefList.get(beliefID), prevalence, personList.get(personID));
            } else
                media = new Media(id, beliefList.get(beliefID), prevalence);
        } else
            media = new Media(id, beliefList.get(beliefID), prevalence, true);
        addToMediaStructures(media);
    }

    public void addBeliefs(int count) {
        for (int x = 0; x < count; x++)
            addBelief();
    }

    private void addBelief() {
        int id = beliefList.size();
        beliefList.add(new Belief(id));
        expressedBeliefsCount.add(0);
    }

    public void addBeliefWithName(String name) {
        int id = beliefList.size();
        beliefList.add(new Belief(id, name));
        expressedBeliefsCount.add(0);
    }

    private void printPeople() {
        System.out.println("ID   Base Belief   Expressed Belief");
        for (Person person : personList) {
            System.out.print(person.getId() + " ");
            System.out.print(person.getBaseBelief().getName() + " ");
            System.out.println(person.expressBelief().getName());
        }
    }

    private void printMedia() {
        System.out.println("ID   Belief   Popular   Person");
        for (Media media : mediaList) {
            System.out.print(media.getId() + " ");
            System.out.print(media.getBelief().getName() + " ");
            System.out.print(media.isPopularMedia() + " ");
            if (media.getRhetor() != null)
                System.out.println(media.getRhetor().getName());
            else
                System.out.println("n/a");
        }
    }

    private void printBeliefs() {
        System.out.println("ID   Name   Count");
        for (Belief belief : beliefList) {
            System.out.print(belief.getId() + " ");
            System.out.print(belief.getName() + " ");
            System.out.println(expressedBeliefsCount.get(belief.getId()));
        }
        if (popularBelief != null)
            System.out.println("The popular belief is: " + popularBelief.getName());
    }

    public void printMenu() {
        System.out.println("Enter one of following numbers:");
        System.out.println("1 - Create New Beliefs");
        System.out.println("2 - Create New Media");
        System.out.println("3 - Create New People");
        System.out.println("4 - Simulate Multiple Media Interactions");
        System.out.println("5 - Create Specific Media Interactions");
        System.out.println("6 - Print Media");
        System.out.println("7 - Print People");
        System.out.println("8 - Print Beliefs");
    }

    public void commandLineController(int num) {
        Scanner scanner = new Scanner(System.in);
        int count;
        int beliefID;
        switch (num) {
            case 1://Add Belief
                System.out.println("Enter Number of Beliefs: ");
                count = scanner.nextInt();
                addBeliefs(count);
                break;
            case 2://Add Media
                System.out.println("Enter Number of Media: ");
                count = scanner.nextInt();
                System.out.println("Enter Average Prevalence of Media: ");
                int averagePrev = scanner.nextInt();
                System.out.println("Enter Std Dev of Prevalence (0 will result in prevalence = avg): ");
                double stdDevPrev = scanner.nextDouble();
                System.out.println("Enter Belief ID:");
                beliefID = scanner.nextInt();
                System.out.println("Is media popular? (true or false)");
                boolean popular = scanner.nextBoolean();
                if (popular) {
                    addPopularMedias(beliefID, averagePrev, stdDevPrev, count);
                } else {
                    System.out.println("Is Media Personal? (true or false)");
                    boolean personal = scanner.nextBoolean();
                    if (personal) {
                        System.out.println("Create New People? (true or false)");
                        boolean newPerson = scanner.nextBoolean();
                        if (newPerson) {
                            System.out.println("Enter Average Conviction of People: ");
                            int averageCon = scanner.nextInt();
                            System.out.println("Enter Std Dev of Conviction (0 will result in prevalence = avg): ");
                            double stdDevCon = scanner.nextDouble();
                            addPersonsWithMedia(beliefID, averagePrev, stdDevPrev, averageCon, stdDevCon, count);
                        } else {
                            for (int x = 0; x < count; x++) {
                                System.out.println("Enter ID of Person Attaching to Media:");
                                int personID = scanner.nextInt();
                                addPersonalMedia(beliefID, averagePrev, stdDevPrev, personID);
                            }
                        }
                    } else {
                        addMedias(beliefID, averagePrev, stdDevPrev, count);
                    }
                }
                break;
            case 3://Add Person
                System.out.println("Enter Number of People: ");
                count = scanner.nextInt();
                System.out.println("Enter Average Conviction of People: ");
                int averageCon = scanner.nextInt();
                System.out.println("Enter Std Dev of Conviction (0 will result in prevalence = avg): ");
                double stdDevCon = scanner.nextDouble();
                System.out.println("Enter Belief ID:");
                beliefID = scanner.nextInt();
                addPeople(beliefID, averageCon, stdDevCon, count);
                break;
            case 4://Sim Media interactions
                System.out.println("Enter Number of Media Interactions:");
                count = scanner.nextInt();
                sim_interactions(count);
                break;
            case 5://Create specific interaction
                System.out.println("Enter ID of Media: ");
                int mediaID = scanner.nextInt();
                System.out.println("Enter ID of Person:");
                int personID = scanner.nextInt();
                mediaInteraction(mediaID, personID);
                break;
            case 6:
                printMedia();
                break;
            case 7:
                printPeople();
                break;
            case 8:
                printBeliefs();
                break;
            default:
                System.out.println("Invalid Number");
        }
        printMenu();
    }

    public static void trialLoop(int avgCon1, int avgCon2, double sd1, double sd2) {
        Environment environment;
        String filename = "results/" + "a1-" + avgCon1 + "a2-" + avgCon2 + "sd-" + sd1 + ".txt";
        File file = new File(filename);
        FileWriter fileWriter;

        int numOfTrials = 1000;
        int numOfPeople = 100;
        int numOfInteractions = 100000;
        int lowerBound = 1;
        int upperBound = 50;
        int count;
        long curTime = System.nanoTime();
        long prevTime;

        System.out.println(filename);


        try {
            if (!file.createNewFile()) {
                System.out.println("File Error");
                return;
            } else {
                System.out.println(file.getName() + " created");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        try {
            fileWriter = new FileWriter(filename);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try {
            fileWriter.write("Number of trials: ");
            fileWriter.write(Integer.toString(numOfTrials));
            fileWriter.write("\nNumber of people: ");
            fileWriter.write(Integer.toString(numOfPeople));
            fileWriter.write("\nNumber of interactions: ");
            fileWriter.write(Integer.toString(numOfInteractions));
            fileWriter.write("\nAverage conviction 1: ");
            fileWriter.write(Integer.toString(avgCon1));
            fileWriter.write("\nAverage std dev 1: ");
            fileWriter.write(Double.toString(sd1));
            fileWriter.write("\nAverage conviction 2: ");
            fileWriter.write(Integer.toString(avgCon2));
            fileWriter.write("\nAverage std dev 2: ");
            fileWriter.write(Double.toString(sd1));
            fileWriter.write("\n\n\nresults:\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int y = lowerBound; y < upperBound; y++) {
            count = 0;
            for (int x = 0; x < numOfTrials; x++) {
                environment = new Environment(0, 0, 2);
                environment.addPersonsWithMedia(0, 1, 0, avgCon1, sd1, y);
                environment.addPersonsWithMedia(1, 1, 0, avgCon2, sd2, numOfPeople - y);
                environment.sim_interactions(numOfInteractions);
                if (environment.popularBelief.getId() == 0) {
                    count++;
                    if (environment.expressedBeliefsCount.get(0) < numOfPeople * 0.9) {
                        System.out.println("Low Popularity: " + environment.expressedBeliefsCount.get(0));
                    }
                } else {
                    if (environment.expressedBeliefsCount.get(1) < numOfPeople * 0.75 - y) {
                        System.out.println("Low UnPopularity: " + environment.expressedBeliefsCount.get(0));
                    }
                }
            }

            prevTime = curTime;
            curTime = System.nanoTime();
            System.out.println("trial #:" + (y) + " a1-" + avgCon1 + " a2-" + avgCon2);
            System.out.println("Trials runtime:" + Long.toString((curTime - prevTime) / 1000000000));

            try {
                fileWriter.write(Integer.toString(y));
                fileWriter.write("/");
                fileWriter.write(Integer.toString(numOfPeople));
                fileWriter.write(": ");
                fileWriter.write(Double.toString((double) count / numOfTrials));
                fileWriter.write("\n");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("File Writing Error");
                return;
            }
        }
        try {
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        int sd1 = 0;
        int sd2 = 0;
        TrialThread thread = new TrialThread(90, 50, sd1, sd2);
        thread.start();

        /*for (int a2 = 50; a2 < 100; a2 += 5) {
            for (int a1 = a2 + 5; a1 <= 100; a1 += 5) {
                TrialThread thread = new TrialThread(a1, a2, sd1, sd2);
                thread.start();
            }
        }*/
    }
}
