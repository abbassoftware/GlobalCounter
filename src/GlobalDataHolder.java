
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;

/*
 * This class holds the global counter data. This also has the capabilities to merge 
 * data which comes from the GlobalDataHolder of other process via sync. 
 */

public class GlobalDataHolder {
    
    final private int currProcessNumber;
    final private int totalNumberOfProcess;
    final private List<Integer> addCounters = new CopyOnWriteArrayList<Integer>(); 
    final private List<Integer> subCounters = new CopyOnWriteArrayList<Integer>();
    private SyncStatePublisher statePublisher = null;
    final private ExecutorService executor;

    
    private Object addCounterLock = new Object();
    private Object subCounterLock = new Object();
    
    /**
     * Constructor.
     * @param currProcessNumber
     * @param totalNumberOfProcess
     * @param executor
     */
    
    public GlobalDataHolder(int currProcessNumber, int totalNumberOfProcess ,
            ExecutorService executor) {
        this.currProcessNumber = currProcessNumber;
        this.totalNumberOfProcess = totalNumberOfProcess;
        for(int i=0 ; i < totalNumberOfProcess; i++) {
            addCounters.add(i, 0);
            subCounters.add(i,0);
        }
        
        this.executor = executor;
    }
    
    /*
     * This function returns the global count value currently present in this process.
     */
    public int getCount() {
        int count = 0;
        synchronized (addCounterLock) {
            for(int i =0; i< addCounters.size(); i++) {
                System.out.println("addCounters " + i + " : " + addCounters.get(i));
                count = count + addCounters.get(i);
            }
        }
        System.out.println("count after add " + count);

        synchronized (subCounterLock) {
            for(int i =0; i < subCounters.size(); i++) {
                System.out.println("subCounters " + i + " : " + subCounters.get(i));
                count = count - subCounters.get(i);
            }
        }
        System.out.println("count after sub " + count);

        return count;
    }
    
    /*
     * This function increments the global counter value by 1.
     */
    public void incrementCounter() {
        synchronized (addCounterLock) {
            int newCount = addCounters.get(currProcessNumber) + 1;
            addCounters.set(currProcessNumber, newCount);
        }
        
        System.out.println("currProcessNumber " + currProcessNumber);
        System.out.println("addCounters.size " + addCounters.size());
        System.out.println("add counter " + addCounters.get(currProcessNumber));
        publishStateToOtherProcessesAsync();
    }
    
    /*
     * This function increments the global decrements value by 1.
     */
    synchronized public void decrementCounter() {
        synchronized (subCounterLock) {
            int newCount = subCounters.get(currProcessNumber) + 1;
            subCounters.set(currProcessNumber, newCount);
        }
        System.out.println("sub counter " + subCounters.get(currProcessNumber));
        publishStateToOtherProcessesAsync();
    }
    
    /*
     * Returns a copy of the AddCounter array.
     */
    public List<Integer> getAddCounter() {
        synchronized (addCounterLock) {
            return new ArrayList<Integer>(addCounters);
        }
    }
    
    /*
     * Returns a copy of the Sub Counters array.
     */
    public List<Integer> getSubCounter() {
        synchronized (subCounterLock) {
            return new ArrayList<Integer>(subCounters);
        }
    }
    
    /**
     * Triggers a publish state of its data in Async manner.
     */
    private void publishStateToOtherProcessesAsync() {
        if(statePublisher != null) {
            executor.submit(new Runnable() {
                
                @Override
                public void run() {
                    statePublisher.publishState();
                    
                }
            });
        }
    }

    /**
     * Sets the reference of state publisher, which will use to publish its state 
     * to other processes.
     * @param statePublisher
     */
    public void setStatePublisher(SyncStatePublisher statePublisher) {
        this.statePublisher = statePublisher;
    }
    
   /**
    * Merges the addCounter , subCounter values coming from another process.
    * @param addCounter
    * @param subCounter
    */
    public void merge(List<Integer> addCounter , List<Integer> subCounter) {
        if(addCounter.size() != this.addCounters.size() ||
                subCounter.size() != this.addCounters.size()) {
            System.out.println("Merge input is corrurt. Ignore");
            return;
        }
        
        synchronized (addCounterLock) {
            for(int i =0; i< this.addCounters.size(); i++) {
                if(addCounter.get(i) >  this.addCounters.get(i)) {
                    this.addCounters.set(i, addCounter.get(i));
                }
            }
        }
        
        synchronized (subCounterLock) {
            for(int i =0; i< this.subCounters.size(); i++) {
                if(subCounter.get(i) >  this.subCounters.get(i)) {
                    this.subCounters.set(i, subCounter.get(i));
                }
            }
        }
        
    }

}
