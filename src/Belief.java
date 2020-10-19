public class Belief {
    private final int id;
    private final String name;

    Belief(int id) {
        this.id = id;
        name = "Belief" + id;
    }

    Belief(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
