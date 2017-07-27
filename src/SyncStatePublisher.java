import java.util.List;
import java.util.concurrent.ExecutorService;


/**
 * This class publishes it current state to other process to sync.
 *
 */
public class SyncStatePublisher {
   
    private List<Process> processes;
    private GlobalDataHolder globalDataHolder;
    private int currProcessNum ;
    private ExecutorService executor;
    
    public SyncStatePublisher(List<Process> processes, GlobalDataHolder globalDataHolder,
            int currProcessNum, ExecutorService executor) {
        super();
        this.processes = processes;
        this.globalDataHolder = globalDataHolder;
        this.currProcessNum = currProcessNum;
        this.executor = executor;
    }
   
   
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
