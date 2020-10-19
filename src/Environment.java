import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

public class Environment {
    private Vector<Person> personList;
    private Vector<Media> mediaList;
    private Vector<Belief> beliefList;
    private int numberOfInteractions;

    Environment(int numPeople, int numMedia, int numBelief, int numberOfInteractions) {
        personList = new Vector<Person>();
        mediaList = new Vector<Media>();
        beliefList = new Vector<Belief>();
        for (int x = 0; x < numBelief; x++) {
            addBelief();
        }
        for (int x = 0; x < numMedia; x++) {
            System.out.println("Media" + x + ":");
            addMedia();
        }
        for (int x = 0; x < numPeople; x++) {
            System.out.println("Person" + x + ":");
            addPerson();
        }
        this.numberOfInteractions = numberOfInteractions;

    }

    private void sim_interactions() {
        Random random = new Random();
        int personID;
        int mediaID;
        int totalPrevalence = 0;
        for (Media value : mediaList) // adds up total prevalence
            totalPrevalence += value.getPrevalence();
        for (int x = 0; x < numberOfInteractions; x++) {
            personID = random.nextInt(personList.size()); // gets id to select person at random from list
            mediaID = random.nextInt(totalPrevalence); // gets int lower than the total prevalence to select media weighted by prevalence
            for (Media media : mediaList) {
                mediaID -= media.getPrevalence(); // decrements the total prevalence until
                if (mediaID < 0) {
                    personList.get(personID).addInteraction(media);
                    break;
                }
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
    }

    private void addMedia() {
        Scanner scanner = new Scanner(System.in);
        int id = mediaList.size();
        System.out.println("Enter prevalence:");
        int prevalence = scanner.nextInt();
        System.out.println("Enter Rhetoric ID:");
        int beliefID = scanner.nextInt();
        mediaList.add(new Media(id, beliefList.get(beliefID), prevalence));
    }

    private void addBelief() {
        int id = beliefList.size();
        beliefList.add(new Belief(id));
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
        int numPeople = 1;
        int numMedia = 2;
        int numBeliefs = 2;
        int numInteractions = 1000;
        Environment environment = new Environment(numPeople, numMedia, numBeliefs, numInteractions);
        environment.sim_interactions();
        environment.printStats();
        environment.sim_interactions();
        environment.printStats();
        environment.sim_interactions();
        environment.printStats();
    }
}
