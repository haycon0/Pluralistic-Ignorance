import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

public class Environment {
    private Vector<Person> personList;
    private Vector<Integer> expressedBeliefsCount;
    private Vector<Media> mediaList;
    private Vector<Integer> prevalenceList; // List of the sum of the sum of the prevalence's after corresponding media was added
    private Vector<Belief> beliefList;
    private int numberOfInteractions; // number of interactions to be simulated
    private Belief popularBelief;


    Environment(int numPeople, int numMedia, int numBelief, int numberOfInteractions) {
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

        // Creates Media
        for (int x = 0; x < numMedia; x++) {
            System.out.println("Media" + x + ":");
            addMedia();
        }

        // Creates People
        for (int x = 0; x < numPeople; x++) {
            System.out.println("Person" + x + ":");
            addPerson();
        }
        this.numberOfInteractions = numberOfInteractions;

    }

    private void sim_interactions() {
        Random random = new Random();
        int personID; // ID of person in the interaction
        int prevalenceNum; // number used to determine media in the interaction
        int totalPrevalence = prevalenceList.lastElement(); // upper bound for prevalenceNum
        double popularBeliefUbiquity; //The portion of people that express the popular belief
        Belief originalBelief;
        Belief updatedBelief;

        // binary search variables
        int searchBegin;
        int searchMid;
        int searchLast;


        for (int x = 0; x < numberOfInteractions; x++) {
            personID = random.nextInt(personList.size()); // gets id to select person at random from list
            prevalenceNum = random.nextInt(totalPrevalence); // gets int lower than the total prevalence to select media weighted by prevalence
            originalBelief = personList.get(personID).expressBelief();

            // Binary search to find ID of media that the prevalenceNum corresponds to
            searchLast = prevalenceList.size() - 1;
            searchBegin = 0;
            while (true) {
                searchMid = (searchBegin + searchLast) / 2;
                if (searchMid == 0 || (prevalenceList.get(searchMid) > prevalenceNum && prevalenceList.get(searchMid - 1) <= prevalenceNum)) {

                    //TODO add comment
                    popularBeliefUbiquity = (double) expressedBeliefsCount.get(popularBelief.getId()) / personList.size();
                    mediaList.get(searchMid).checkPopular(popularBeliefUbiquity, popularBelief);
                    personList.get(personID).addInteraction(mediaList.get(searchMid));
                    break;
                }
                if (prevalenceList.get(searchMid) > prevalenceNum) {
                    searchLast = searchMid - 1;
                } else {
                    searchBegin = searchMid + 1;
                }
            }
            updatedBelief = personList.get(personID).expressBelief();

            // TODO add comment
            if (originalBelief != updatedBelief) {
                expressedBeliefsCount.set(originalBelief.getId(), expressedBeliefsCount.get(originalBelief.getId()) - 1);
                expressedBeliefsCount.set(updatedBelief.getId(), expressedBeliefsCount.get(updatedBelief.getId()) + 1);
                if (expressedBeliefsCount.get(updatedBelief.getId()) > expressedBeliefsCount.get(popularBelief.getId()))
                    popularBelief = updatedBelief;
            }
        }
    }

    private void addPerson() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Conviction:");
        int conviction = scanner.nextInt();
        int id = personList.size();
        System.out.println("Enter Base Belief ID:");
        int beliefID = scanner.nextInt();
        personList.add(new Person(id, beliefList.get(beliefID), conviction));

        //TODO add comment
        expressedBeliefsCount.set(beliefID, expressedBeliefsCount.get(beliefID) + 1);
        if (popularBelief == null)
            popularBelief = beliefList.get(beliefID);
        if (expressedBeliefsCount.get(beliefID) > expressedBeliefsCount.get(popularBelief.getId()))
            popularBelief = beliefList.get(beliefID);
    }

    private void addMedia() {
        Scanner scanner = new Scanner(System.in);
        int id = mediaList.size();
        System.out.println("Enter prevalence:");
        int prevalence = scanner.nextInt();
        System.out.println("Enter Belief ID:");
        int beliefID = scanner.nextInt();
        mediaList.add(new Media(id, beliefList.get(beliefID), prevalence));

        // adds previous total prevalence + new media prevalence to prevalence list to implement
        // prevalence weighted media interactions
        if (prevalenceList.size() == 0)
            prevalenceList.add(prevalence);
        else
            prevalenceList.add(prevalenceList.lastElement() + prevalence);
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

    public static void main(String args[]) {
        int numPeople = 4;
        int numMedia = 1;
        int numBeliefs = 2;
        int numInteractions = 10;
        Environment environment = new Environment(numPeople, numMedia, numBeliefs, numInteractions);
        environment.sim_interactions();
        environment.printStats();
        environment.sim_interactions();
        environment.printStats();
        environment.sim_interactions();
        environment.printStats();
    }
}
