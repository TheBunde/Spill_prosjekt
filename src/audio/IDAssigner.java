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
     * Get method.
     * @return Base ID
     */
    public int getBID() {
        return bID;
    }

    /**
     * Increments the Base ID.
     * @return Base ID + 1;
     */
    public int nextBID() {
        return bID++;
    }

}