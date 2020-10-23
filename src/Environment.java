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

    private void sim_interactions(int numberOfInteractions) {

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
                searchMid = (searchBegin + searchLast) / 2;
                if (searchMid == 0 || (prevalenceList.get(searchMid) > prevalenceNum && prevalenceList.get(searchMid - 1) <= prevalenceNum)) {
                    mediaInteraction(searchMid, personID);
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

    private void addPeople(int beliefID, int avgConviction, double sd, int count) {
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

    private void addPersonsWithMedia(int beliefID, int avgPrevalence, double sdPrev, int avgConviction, double sdCon, int count) {
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
        addPerson(conviction, personID);
        addPersonalMedia(beliefID, avgPrevalence, sdPrev, personID);
    }

    private void addMedias(int beliefID, int avgPrevalence, double sd, int count) {
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

    private void addPopularMedias(int beliefID, int avgPrevalence, double sd, int count) {
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

    private void addPersonalMedia(int beliefID, int avgPrevalence, double sd, int personID) {
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

    private void addBelief() {
        int id = beliefList.size();
        beliefList.add(new Belief(id));
        expressedBeliefsCount.add(0);
    }

    private void printStats() {
        System.out.println("ID   Base Belief   Expressed Belief");
        for (Person person : personList) {
            System.out.print(person.getId() + " ");
            System.out.print(person.getBaseBelief().getName() + " ");
            System.out.println(person.expressBelief().getName());
        }
    }

    public static void main(String[] args) {
        int numPeople = 0;
        int numMedia = 0;
        int numBeliefs = 2;
        Environment environment = new Environment(numPeople, numMedia, numBeliefs);
        environment.addPerson(1, 0);
        environment.addPerson(7, 0);
        environment.addPerson(3, 0);
        environment.addPerson(0, 0);
        environment.addPerson(1, 1);
        environment.addPerson(7, 1);
        environment.addPerson(3, 1);
        environment.addPerson(0, 1);
        environment.addPerson(0, 1);
        environment.addMedia(0, 1, 0);
        environment.addPersonWithMedia(1, 1, 0, 1, 0);
        environment.mediaInteraction(0, 1);
        environment.mediaInteraction(0, 0);
        environment.sim_interactions(100);
        environment.printStats();
        environment.addMedia(0, 100, 1);
        environment.sim_interactions(1000);
        environment.printStats();
    }
}
