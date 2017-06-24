package pe.edu.uni.fiis.so.simulation.memory;

/**
 * Created by vcueva on 6/24/17.
 */
public class Memory {

    public static final long DEFAULT_MEMORY_SIZE = 8L * 1024 * 1024*1024; // 8GB
    public static final int DEFAULT_PAGE_SIZE = 4 * 1024 * 1024; // 4KB

    private long memorySize;
    private int pageSize;

    private int[] map;
    private int totalPages;

    private long freeMemorySize;

    public Memory() {
        this(DEFAULT_MEMORY_SIZE, DEFAULT_PAGE_SIZE);
    }

    public Memory(long memorySize) {
        this(memorySize, DEFAULT_PAGE_SIZE);
    }

    public Memory(long memorySize, int pageSize) {
        this.memorySize = memorySize;
        this.pageSize = pageSize;

        totalPages = (int) (memorySize / pageSize);
        map = new int[totalPages];
        for (int i = 0; i < totalPages; i++) {
            map[i] = -1;
        }

        freeMemorySize = memorySize;
    }

    public long getMemorySize() {
        return memorySize;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public long getFreeMemorySize() {
        return freeMemorySize;
    }

    public void setFreeMemorySize(int freeMemorySize) {
        this.freeMemorySize = freeMemorySize;
    }
}
