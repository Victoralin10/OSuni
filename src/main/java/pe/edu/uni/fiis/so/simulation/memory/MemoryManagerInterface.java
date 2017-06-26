package pe.edu.uni.fiis.so.simulation.memory;

import java.util.List;

/**
 * Created by vcueva on 6/24/17.
 */
public interface MemoryManagerInterface {

    /**
     * Reserve memory for a process.
     *
     * @param size size of the memory to reserve.
     * @param pid  pid of the process.
     * @return A list of the pages reserved.
     */
    List<Integer> malloc(int size, int pid);

    /**
     * Free the pages if not are read only.
     *
     * @param pages List of pages.
     * @return - true if it was possible to reserve memory.
     * - false if it was not possible to reserve memory.
     */
    boolean free(List<Integer> pages);

    /**
     * Free a page if is not read only.
     *
     * @param page the page to free.
     * @return - true if it was possible to reserve.
     * - false if it was not possible
     */
    boolean free(int page);

    /**
     * Free all memory reserved by a process.
     *
     * @param pid pid of the process.
     */
    void terminate(int pid);

    /**
     * Free all memory reserved by a process using their page table.
     *
     * @param pid       pid of the process.
     * @param pageTable The page table of the process.
     */
    void terminate(int pid, List<Integer> pageTable);
}
