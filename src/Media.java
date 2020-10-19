public class Media {
    private final int id;
    private Belief belief;
    private int prevalence;

    Media(int id, Belief rhetoric, int prevalence) {
        this.id = id;
        this.belief = rhetoric;
        this.prevalence = prevalence;
    }

    public int getId() {
        return id;
    }

    public Belief getBelief() {
        return belief;
    }

    public int getPrevalence() {
        return prevalence;
    }
}

