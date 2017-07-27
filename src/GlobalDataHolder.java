
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GlobalDataHolder {
    
    private int currProcessNumber;
    private int totalNumberOfProcess;
    private List<Integer> addCounters = new CopyOnWriteArrayList<Integer>(); 
    private List<Integer> subCounters = new CopyOnWriteArrayList<Integer>();
    
    public GlobalDataHolder(int currProcessNumber, int totalNumberOfProcess) {
        this.currProcessNumber = currProcessNumber;
        this.totalNumberOfProcess = totalNumberOfProcess;
        for(int i=0 ; i< totalNumberOfProcess; i++) {
            addCounters.add(i, 0);
            subCounters.add(i,0);
        }
    }
    
    public int getCount() {
        int count = 0;
        for(int i =0; i< addCounters.size(); i++) {
            count = count + addCounters.get(i);
        }
        for(int i =0; i < subCounters.size(); i++) {
            count = count - subCounters.get(i);
        }
        
        return count;
    }
    
    synchronized public void incrementCounter() {
        int newCount = addCounters.get(currProcessNumber) + 1;
        addCounters.add(currProcessNumber, newCount);
    }
    
    synchronized public void decrementCounter() {
        int newCount = addCounters.get(currProcessNumber) - 1;
        addCounters.add(currProcessNumber, newCount);
    }

}
