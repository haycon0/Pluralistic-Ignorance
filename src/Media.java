public class Media {
    private final int id;
    private Belief rhetoric;
    private int prevalence;

    Media(int id, Belief rhetoric, int prevalence) {
        this.id = id;
        this.rhetoric = rhetoric;
        this.prevalence = prevalence;
    }

    public int getId() {
        return id;
    }

    public Belief getRhetoric() {
        return rhetoric;
    }

    public int getPrevalence() {
        return prevalence;
    }
}

