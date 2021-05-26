/**
 * An exception class for an exception thrown when a segment cannot be allocated memory either because the memory is full
 * or there's not enough contiguous free memory for the segment
 */
public class SegmentAllocationException extends RuntimeException {

    /**
     * Constructs an SegmentAllocationException
     */
    public SegmentAllocationException() {
        super();
    }

    /**
     * Constructs an SegmentAllocationException with the given message
     * @param message
     */
    public SegmentAllocationException(String message) {
        super(message);
    }
}
