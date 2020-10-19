import java.util.Vector;

public class Person {
    private final int id;
    private final Belief baseBelief;
    private final int conviction;
    private Vector<Media> mediaInteractions;

    Person(int id, Belief baseBelief, int conviction) {
        this.id = id;
        this.baseBelief = baseBelief;
        this.conviction = conviction;
        mediaInteractions = new Vector<Media>();
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
        int max = -1;
        for (Media media : mediaInteractions) {
            if (media.getRhetoric().getId() > max)
                max = media.getRhetoric().getId();
        }
        int cNum = (conviction + 4) * mediaInteractions.size() / 10;
        if (max == -1)
            return baseBelief;
        int[] beliefNum = new int[max + 1];
        for (Media media : mediaInteractions) {
            beliefNum[media.getRhetoric().getId()]++;
            if (beliefNum[media.getRhetoric().getId()] > cNum)
                return media.getRhetoric();
        }
        return baseBelief;
    }

    public void addInteraction(Media media) {
        mediaInteractions.add(media);
    }
}
