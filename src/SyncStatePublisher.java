import java.util.List;
import java.util.concurrent.ExecutorService;


/**
 * This class publishes it current state to other process to sync.
 *
 */
public class SyncStatePublisher {
   
    final private List<Process> processes;
    final private GlobalDataHolder globalDataHolder;
    final private int currProcessNum ;
    final private ExecutorService executor;
    
    /**
     * Constructor.
     * @param processes
     * @param globalDataHolder
     * @param currProcessNum
     * @param executor
     */
    public SyncStatePublisher(List<Process> processes, GlobalDataHolder globalDataHolder,
            int currProcessNum, ExecutorService executor) {
        super();
        this.processes = processes;
        this.globalDataHolder = globalDataHolder;
        this.currProcessNum = currProcessNum;
        this.executor = executor;
    }
   
   /**
    * Publises its current state to other processes in different threads.
    */
    public void publishState() {
        List<Integer> addCounter = globalDataHolder.getAddCounter();
        List<Integer> subCounter = globalDataHolder.getSubCounter();

        for(int i = 0; i< processes.size(); i++) {
            if(i != currProcessNum) {
                executor.submit(new SyncStatePublisherRunnable(processes.get(i),
                        addCounter, subCounter));
            }
        }
    }
    
}
