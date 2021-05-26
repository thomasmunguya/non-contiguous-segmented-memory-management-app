# non-contigous-segmented-memory-management
A simple console application that models non-contiguous memory management

In a nutshell. The application works as follows:
1. A list of processes is read from a processes.txt file
2. Each process read form the processes.txt file is in the following format:
Process Pi, Segment 0, Segment 1, Segment 2,..., Segment i and is allocated/ deallocated memory as illustrated below:
For example, 1, 100, 200, 10 refers to Process 1 requiring to be allocated 100 bytes (each entry in the memory array is taken to be a byte) to Segment 0, 200 bytes to Segment 1 and 10 bytes to Segment 2. The first allocation creates a baseline, and all other calls to Process i will add or remove memory from segments.

Example 1: 1, 100, 200, 10 followed by 1, 100, 200, 10 tells us that Process 1 has allocated 200 bytes to Segment 0, 400 to Segment 1, and 30 to Segment 2

Example 2: 1, 100, 200, 10 followed by 1, -40, 200, -10 tells us that Process 1 has allocated 60 bytes to Segment 0, 400 to Segment 1 and 0 to Segment 2.

Example 3: 1, 100, 200, 10, Segment followed by 1, -40, -200, 10 and then followed by 1, -40, 200, tells us that Process 1 has allocated 60 bytes to Segment 0, Segment 1 dissappears (200 - 200), and Segment 2 becomes Segment 1 with 220 bytes in memory. The number of segments can vary. Notice that 1, -40, 200 allocates 200 bytes to old Segment 2 (now on second position)

NOTE: FOR A COMPREHENSIVE DESCRIPTION OF HOW THE APPLICATION WORKS, KEEP READING

Process Class

The Process class models a process. A process has an id, a list of segments, and a segment table as depicted in the class diagram of Figure 1. This class has three constructors. A default constructor, a constructor with a provided process id, and a third a constructor with a provided id, list of segments and a segment table. Getters and setters are provided for each of the instance variables. The process class also contains a method updateSegmentTable that updates the segment table whenever a segment is added to the list of segments in order to keep the segment table consistent with the segments of the process.

Segment Class
The Segment class models a process segment. It has a size variable, and a getter and setter for this variable. It also has 2 constructors; a default one, and another that takes in a segment size.

SegmentTable Class

The SegmentTable class models a segment table. It has a variable, entries, of type List<Map<Boolean, Map<Long, Long>>> , and a getter and setter for this variable. A segment table is modelled as a List of maps. Each map in this list represents an entry (or row) of a segment table. The first type argument (Boolean) of each map is used to signify whether the segment has been allocated memory or not. This is helpful in ensuring that the segment is only allocated memory once. Each map in turn has a map with Long type arguments. The first argument of this nested map represents the size (limit) of the segment and the second one represents the base address of the segment in memory. In essence, an entry (or row) in the segment table is a map whose first parameter is Boolean and second parameter is a map. The SegmentTable class has methods addEntry (which adds a segment to the segment table), addEntries (which adds a list of segments to the segment table) and replaceEntry (which replaces an entry in the segment table). The replaceEntry method is used during the allocation of memory to update the segment table entry when a segment’s size has changed or when it has been allocated or deallocated memory. Lastly, the segment table has a private generateAddress method that returns a random base address for a segment in the segment table.

Memory Class

The Memory class models a memory. Because we only need a single memory per program, this class is modelled as a singleton class. It has a private default constructor and a method getInstance that checks if the class has already been instantiated or not before returning an instance. If the class has already been instantiated, the existing instance is returned, otherwise a new instance is returned. It has a final static variable SIZE which is the default size of the memory, a final MEMORY variable (which is the actual memory, and a getter for this variable), a memoryInstance variable which is used to model the singleton design pattern, and a PROCESSES variable which keeps track of the processes that are in memory.
The memory is modelled as an array of boolean. Each index in the array is taken to be a byte. memory[i] is true if that by of memory has been allocated to a segment, and false otherwise.
The Memory class has two additional methods; allocateMemory that takes in a process as an argument and allocates memory to the process and deallocateMemory that deallocates memory from a process. The underlying algorithm for the former obtains all of the segments from the process’s segment table. It then sets the memory array indexes memory[base] to memory[base + limit] to true. The algorithm first checks if the process is already in memory. If it’s in memory, then the process’s segments are simply modified according to the instructions given in the assignment by deducting from or adding to the size of each segment. It then attempts to allocate memory to the process starting at memory[base]. If memory[base + limit] is true at the time of allocation, it means the slot in memory starting at memory[base] to memory[base + limit] is not big enough to accommodate the segment. This is so because memory[base + limit] would be false if that byte of memory were not allocated to some other segment. The program will then loop in an attempt to find another slot where the segment will fit and upon reaching the end of the memory array will go to the beginning of the array and make a full iteration through the array. If at the end of this iteration there’s still no contiguous space for the segment to fit or if the memory is full, a SegmentAllocationException will be thrown to signify that the segment and in turn the process could not be allocated memory.
The deallocateMemory method deallocates memory from a process by setting indexes memory[base] to memory[base + limit] to false for each segment of the process.

MemoryManagement Class

This is the main class. It contains the main method and another method readProcessesFromFile that reads the list of processes from a processes.txt file and returns an ArrayList of processes. This method reads the process information line by line and creates an instance of Process and instance(s) of Segment for each line of process information obtained. Thereafter each process is added to an ArrayList of Process.
The main method has an instance of Memory called memory. It invokes the readProcessesFromMemory method to obtain the list of processes and then invokes memory.allocateMemory for each of the processes in the process ArrayList. For each process that has been allocated and then deallocated memory at least once, the details for that process are printed to the console displaying the segments affected and the state of the memory after those changes are made.

SegmentAllocationException Class

This is an exception class. The SegmentAllocationException is thrown when a process segment cannot be allocated memory either because there was no contiguous free memory where it could fit or the memory is full. This class has two constructors. A default constructor and another constructor that takes in the message associated with the exception.
