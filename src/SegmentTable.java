import java.util.*;

/**
 * A class that models a segment table
 * A segment table keeps track of the size and location (in memory) of each segment of a process
 * Each entry in the segment is modelled as a map of boolean keys (representing whether that segment is in memory or not),
 * And a map of long keys (the key is the limit of the segment) and long values (the value is the base of the segment)
 */
public class SegmentTable {

    private List<Map<Boolean, Map<Long, Long>>> entries;

    /**
     * Constructs a segment table
     */
    public SegmentTable() {
        entries = new ArrayList<>();
    }

    /**
     * Constructs a segment table for the given limit and base pairs
     * @param entries
     */
    public SegmentTable(List<Map<Boolean, Map<Long, Long>>> entries) {
        this.entries = entries;
    }

    public List<Map<Boolean, Map<Long, Long>>> getEntries() {
        //return an unmodifiable list to the user so that they do not directly add segments to the segment table
        //this is because when a segment is added to the segment table, a random base address is created for it
        //thus, adding an entry directly to the segment table is not desirable
        return Collections.unmodifiableList(entries);
    }

    public void setEntries(List<Map<Boolean, Map<Long, Long>>> entries) {
        this.entries = entries;
    }

    /**
     * Adds a segment to the segment table
     * @param segment the segment to be added
     */
    public void addEntry(Segment segment) {

        if(segment == null) {
            throw new IllegalArgumentException("You cannot add a null segment to the segment table");
        }

        Map<Boolean, Map<Long, Long>> entry = new LinkedHashMap<>();
        Map<Long, Long> limitBasePair = new LinkedHashMap<>();

        long limit = segment.getSize();
        long base = generateAddress(); //generate a random base address for the segment

        limitBasePair.put(limit, base);

        entry.put(Boolean.FALSE, limitBasePair);

        entries.add(entry); //add the entry to the segment table
    }

    /**
     * Adds a list of segments to the segment table
     * @param segments the segments to be added to the segment table
     */
    public void addEntries(List<Segment> segments) {
        if(segments == null) {
            throw new IllegalArgumentException("You cannot add a null list of segments to the segment table");
        }
        segments.forEach(this::addEntry);
    }

    /**
     * Replaces an entry in the segment table
     * @param oldEntry the entry to replace
     * @param newEntry replacement entry
     */
    public void replaceEntry(Map<Boolean, Map<Long, Long>> oldEntry, Map<Boolean, Map<Long, Long>> newEntry) {
        int indexOfOldEntry = entries.indexOf(oldEntry); //add the entry to the segment table
        entries.remove(indexOfOldEntry); //remove the old entry
        entries.add(indexOfOldEntry,newEntry); //add the new entry

    }

    /**
     * Generates a random memory address
     * @return a random memory address
     */
    private long generateAddress() {
        return new Random().nextInt(Memory.SIZE);
    }

}
