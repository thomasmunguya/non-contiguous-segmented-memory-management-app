import java.util.*;
import java.util.stream.Collectors;

/**
 * A class that models computer memory
 * This class is modelled as a singleton class because there can be only be one instance of memory logically speaking
 * Even though there may be multiple instances of say RAM chips, they are all represented as one logical unit
 */

public class Memory {

    public static final int SIZE = 30; //the default size of the memory
    private final boolean[] MEMORY = new boolean[SIZE]; //the memory
    private static Memory memoryInstance; //memory instance used to model a singleton
    private final  List<Process> PROCESSES = new ArrayList<>(); //the processes in memory

    //a private constructor to prevent direct instantiation of the class
    private Memory() {
    }

    public boolean[] getMemory(){
        return MEMORY;
    }
    /**
     * Returns an instance of Memory
     * @return a Memory
     */
    public static Memory getInstance() {
        if(memoryInstance == null) {
            return new Memory();
        }
        return memoryInstance;
    }
    
    /**
     * Allocates memory to a process
     */
    public void allocateMemory(Process process) {
        if (PROCESSES.stream().anyMatch(process1 -> process1.getId() == process.getId())) { //1.if the process is already in memory
            Process processInMemory = PROCESSES.stream().filter(process1 ->
                    process1.getId() == process.getId()).collect(Collectors.toList()).get(0); //2.obtain the process from memory
            this.deallocateMemory(processInMemory); //3.deallocate memory from the process
            for (int i = 0; i < process.getSegments().size(); i++) {
                process.getSegments().get(i).setSize(Math.abs(processInMemory.getSegments().get(i).getSize()
                        + process.getSegments().get(i).getSize())); //4.change the size of each segment of the process
                process.updateSegmentTable(); //5.update the segment table of the process
            }
            PROCESSES.remove(processInMemory); //6.remove the current version of the process from the list of PROCESSES in memory so that it can be later replaced
        }

        for (int l = 0; l < process.getSegmentTable().getEntries().size(); l++) {//1. for each segment of the process
            Map<Boolean, Map<Long, Long>> entry = process.getSegmentTable().getEntries().get(l);
            boolean isInMemory = entry.keySet().contains(Boolean.TRUE); //2.obtain info about whether the segment is in memory
            Long limit = entry.values().stream().findFirst().get().keySet().stream().findFirst().get(); //3. obtain the size of the segment
            Long base = entry.values().stream().findFirst().get().values().stream().findFirst().get(); //4. obtain the starting address (in memory) of the segment
            if (!isInMemory) { //5.if a segment is not already in memory
                int roundTrip = 0; //keep track of how many times a segment iterates the array to find a free contiguous slot
                for (int i = base.intValue(); i < MEMORY.length; i++) {//6.iterate the memory and try to find a slot for the segment

                    if(roundTrip > 2) { //throw a SegmentAllocationException exception if the segment makes more than two iterations around the memory array to avoid an infinite loop
                        throw new SegmentAllocationException("A segment could not be allocated a space in memory." +
                                " Either the memory is full or there was not enough contiguous free memory for the segment.");
                    }

                    if ((i + limit.intValue()) < MEMORY.length) {
                        if (MEMORY[i] == false && MEMORY[i + limit.intValue()] == false) {
                            for (int j = i; j < (i + limit.intValue()); j++) {
                                MEMORY[j] = true;
                            }
                            //in case the segment was not allocated a portion of memory starting from its base address
                            //update the segment's base address and replace it in the segment table
                            Map<Long, Long> newLimitBasePair = new LinkedHashMap<>();
                            newLimitBasePair.put(limit, (long) i);
                            Map<Boolean, Map<Long, Long>> newEntry = new LinkedHashMap<>();
                            newEntry.put(Boolean.TRUE, newLimitBasePair);
                            process.getSegmentTable().replaceEntry(entry, newEntry);
                            break;
                        }
                    } else {
                        if (!entry.keySet().contains(Boolean.TRUE)) {
                            i = -1;
                            roundTrip++;
                        }

                    }
                }

            }
        }
        PROCESSES.add(process); //replace the previous version of the process that was removed with the current version
    }

    /**
     * De-allocates memory from a process
     * @param process
     * @return true if the memory has been successfully deallocated, otherwise false
     * This method deallocates a process from memory by setting the values for memory[base] to memory[base + limit] equal to false
     */
    public void deallocateMemory(Process process) {
        for(Map<Boolean, Map<Long, Long>> entry: process.getSegmentTable().getEntries()) {//1.For each of the segments of the process
            Long limit = entry.values().stream().findFirst().get().keySet().stream().findFirst().get(); //obtain the size of the segment
            Long base = entry.values().stream().findFirst().get().values().stream().findFirst().get(); //obtain the starting address (in memory) of the segment
            for(int i = base.intValue(); i < base.intValue() + limit.intValue(); i++) {
                MEMORY[i] = false; //setting all values of memory starting from memory[base] to memory[base + limit] equal to false
            }
            Map<Long, Long> limitBasePair = new LinkedHashMap<>();
            limitBasePair.put(limit, base);
            entry.put(Boolean.FALSE, limitBasePair);
        }
    }

}
