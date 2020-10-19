import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

public class Person {
    private final int id;
    private final Belief baseBelief;
    private final int conviction;
    private Vector<Media> mediaInteractions;
    private Map<Integer, Integer> mediaInteraction;
    private Map<Integer, Belief> knownBeliefs;
    private int maxInteractionAmount;
    private int maxInteractionID;

    Person(int id, Belief baseBelief, int conviction) {
        this.id = id;
        this.baseBelief = baseBelief;
        this.conviction = conviction;
        mediaInteractions = new Vector<Media>(); //Stores all media interactions
        mediaInteraction = new TreeMap<Integer, Integer>(); // Stores the ID and number of interactions with each belief
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

    public Belief expressBelief() {
        int cNum = (conviction + 4) * mediaInteractions.size() / 10; // determines the number of media interactions needed to
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
        if (mediaInteraction.containsKey(mediaBeliefID)) {
            oldCount = mediaInteraction.get(mediaBeliefID);
            mediaInteraction.put(mediaBeliefID, oldCount + 1);
        } else {
            oldCount = 0;
            mediaInteraction.put(mediaBeliefID, 1);
        }

        // adds belief of media to knownBelief tree if needed
        if (!knownBeliefs.containsKey(mediaBeliefID)) {
            knownBeliefs.put(mediaBeliefID, media.getBelief());
        }

        // determines if the belief of new interaction is the new most interacted with
        if (oldCount + 1 > maxInteractionAmount) {
            maxInteractionAmount = oldCount + 1;
            maxInteractionID = mediaBeliefID;
        }


    }
}
