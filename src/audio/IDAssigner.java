package audio;
/**
 * This class is used to make a base ID which can be utilized by the thread classes.
 * @author henrikwt
 */
public class IDAssigner {

    private int bID;
    /**
     * Constructor.
     * @param bID Base ID.
     */
    public IDAssigner(int bID) {
        this.bID = bID;
    }
    /**
     * Increments the Base ID.
     * @return Base ID + 1;
     */
    public int next() {
        return bID++;
    }
    /**
     * Get method.
     * @return Base ID
     */
    public int getID() {
        return bID;
    }
}