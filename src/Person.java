import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

public class Person {
    private final int id;
    private final Belief baseBelief;
    private final int conviction;
    private final String name;

    // these data structures are to calculate expressed belief
    private Vector<Media> mediaInteractions;
    private Map<Integer, Integer> mediaInteractionCounts;
    private Map<Integer, Belief> knownBeliefs;
    private int maxInteractionAmount;
    private int maxInteractionID;

    Person(int id, Belief baseBelief, int conviction) {
        this.id = id;
        this.baseBelief = baseBelief;
        this.conviction = conviction;
        name = "Person" + id;
        mediaInteractions = new Vector<Media>(); //Stores all media interactions
        mediaInteractionCounts = new TreeMap<Integer, Integer>(); // Stores the ID and number of interactions with each belief
        knownBeliefs = new TreeMap<Integer, Belief>(); // Stores the beliefs of media interacted with
    }

    Person(int id, Belief baseBelief, int conviction, String name) {
        this.id = id;
        this.baseBelief = baseBelief;
        this.conviction = conviction;
        this.name = name;
        mediaInteractions = new Vector<Media>(); //Stores all media interactions
        mediaInteractionCounts = new TreeMap<Integer, Integer>(); // Stores the ID and number of interactions with each belief
        knownBeliefs = new TreeMap<Integer, Belief>(); // Stores the beliefs of media interacted with
    }

    public int getId() {
        return id;
    }

    public Belief getBaseBelief() {
        return baseBelief;
    }

    public int getConviction() {
        return conviction;
    }

    public String getName() {
        return name;
    }

    public Belief expressBelief() {

        // determines the number of media interactions needed to overcome conviction
        // example a conviction of 5 means at least 90% (= 5 + 4 / 10) of interactions
        // have to be with a different belief to overcome conviction
        int cNum = (conviction + 3) * mediaInteractions.size() / 10;

        if (maxInteractionAmount > cNum)
            return knownBeliefs.get(maxInteractionID);
        return baseBelief;
    }

    public void addInteraction(Media media) {

        int mediaBeliefID; // ID of the belief of the media
        int oldCount; // previous number of interactions with the media'ss belief

        // adds each media interaction to media interaction vector
        mediaInteractions.add(media);

        // adds new belief ID encountered to the media interaction trees and increments value
        mediaBeliefID = media.getBelief().getId();
        if (mediaInteractionCounts.containsKey(mediaBeliefID)) {
            oldCount = mediaInteractionCounts.get(mediaBeliefID);
            mediaInteractionCounts.put(mediaBeliefID, oldCount + 1);
        } else {
            oldCount = 0;
            mediaInteractionCounts.put(mediaBeliefID, 1);
        }

        // adds belief of media to knownBelief tree if needed
        if (!knownBeliefs.containsKey(mediaBeliefID)) {
            knownBeliefs.put(mediaBeliefID, media.getBelief());
        }

        // determines if the belief of new interaction is the new most encountered
        if (oldCount + 1 > maxInteractionAmount) {
            maxInteractionAmount = oldCount + 1;
            maxInteractionID = mediaBeliefID;
        }
    }
}
