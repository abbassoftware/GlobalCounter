
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
        for(int i=0 ; i < totalNumberOfProcess; i++) {
            addCounters.add(i, 0);
            subCounters.add(i,0);
        }
    }
    
    public int getCount() {
        int count = 0;
        for(int i =0; i< addCounters.size(); i++) {
            System.out.println("addCounters " + i + " : " + addCounters.get(i));
            count = count + addCounters.get(i);
        }
        System.out.println("count after add " + count);

        for(int i =0; i < subCounters.size(); i++) {
            System.out.println("subCounters " + i + " : " + subCounters.get(i));
            count = count - subCounters.get(i);
        }
        System.out.println("count after sub " + count);

        return count;
    }
    
    synchronized public void incrementCounter() {
        int newCount = addCounters.get(currProcessNumber) + 1;
        addCounters.set(currProcessNumber, newCount);
        System.out.println("currProcessNumber " + currProcessNumber);
        System.out.println("addCounters.size " + addCounters.size());


        System.out.println("add counter " + addCounters.get(currProcessNumber));
    }
    
    synchronized public void decrementCounter() {
        int newCount = subCounters.get(currProcessNumber) + 1;
        subCounters.set(currProcessNumber, newCount);
        System.out.println("sub counter " + subCounters.get(currProcessNumber));

    }

}
