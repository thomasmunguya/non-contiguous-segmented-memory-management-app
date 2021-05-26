import java.util.*;

/**
 * A class that models a process
 */
public class Process {

    private int id; //the id of the process
    private List<Segment> segments; //the segments belonging to the processing
    private SegmentTable segmentTable; //the segment table belonging to the process

    /**
     * Constructs a process
     */
    public Process() {
        segments = new ArrayList<>();
        segmentTable = new SegmentTable();
    }

    /**
     * Constructs a process with a given id
     * @param id
     */
    public Process(int id) {
        this.id = id;
    }

    /**
     * Constructs a process with the given id, segments and segment table
     * @param id
     * @param segments
     * @param segmentTable
     */
    public Process(int id, List<Segment> segments, SegmentTable segmentTable) {
        this.id = id;
        this.segmentTable = segmentTable;
        setSegments(segments);
    }

    public int getId() {
        return id;
    }

    public void setId(int Id) {
        this.id = Id;
    }

    public List<Segment> getSegments() {
        return segments;
    }

    public void setSegments(List<Segment> segments) {
        if(segments == null) {
            throw new IllegalArgumentException("A segment list cannot be null.");
        }
        if(segments.stream().anyMatch(Objects::isNull))
            throw new IllegalArgumentException("You have a null value in your list of segments." +
                    " Segments should never be null");
        getSegmentTable().addEntries(segments); //add all the segments to the segment table
        this.segments = segments;
    }

    public SegmentTable getSegmentTable() {
        return segmentTable;
    }

    public void setSegmentTable(SegmentTable segmentTable) {
        this.segmentTable = segmentTable;
    }

    /**
     * Updates the segment table after a process is modified
     */
    public void updateSegmentTable() {
        getSegmentTable().setEntries(new ArrayList<>());
        getSegmentTable().addEntries(getSegments());
    }
}
