package audio;

public class IDAssigner {

    private int bID;

    public IDAssigner(int bID) {
        this.bID = bID;
    }

    public int next() {
        return bID++;
    }

    public int getID() {
        return bID;
    }
}