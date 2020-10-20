public class Media {
    private final int id;
    private Belief belief; // The belief that the media espouses
    private final int prevalence; // The prevalence of the media will be used relative to other media
    private final Person rhetor; // The person that uses the media to communicate can be null
    private final boolean popularMedia; // If true the media changes its belief with the most popular belief

    Media(int id, Belief belief, int prevalence) {
        this.id = id;
        this.belief = belief;
        this.prevalence = prevalence;
        this.rhetor = null;
        popularMedia = false;
    }

    Media(int id, Belief belief, int prevalence, Person rhetor) {
        this.id = id;
        this.belief = belief;
        this.prevalence = prevalence;
        this.rhetor = rhetor;
        popularMedia = false;
    }

    Media(int id, Belief belief, int prevalence, boolean popularMedia) {
        this.id = id;
        this.belief = belief;
        this.prevalence = prevalence;
        this.rhetor = null;
        this.popularMedia = popularMedia;
    }

    public int getId() {
        return id;
    }

    public Belief getBelief() {
        if (rhetor != null)
            belief = rhetor.expressBelief();
        return belief;
    }

    public void checkPopular(double ubiquity, Belief popularBelief) {
        if (!popularMedia)
            return;

        // if more than 30% of people express the popular belief the media changes it's belief
        if (ubiquity > 0.3) {
            belief = popularBelief;
        }
    }

    public int getPrevalence() {
        return prevalence;
    }
}

