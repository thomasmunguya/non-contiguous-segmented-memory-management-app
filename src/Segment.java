/**
 *A class that models a segment
 * A segment is a section of a process
 */
public class Segment {

    private long size; //the size of the segment

    /**
     * Constructs a segment
     */
    public Segment() {
    }

    /**
     * Constructs a segment with the given size
     * @param size
     */
    public Segment(long size) {
        this.size = size;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

}
