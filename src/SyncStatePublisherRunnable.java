import java.io.DataOutputStream;
import java.net.Socket;
import java.util.List;

/**
 * This class publishes its current state to one another processes. 
 *
 */
public class SyncStatePublisherRunnable implements Runnable {

    private Process process;
    private List<Integer> addCounter;
    private List<Integer> subCounter;
    
    public SyncStatePublisherRunnable(Process process, List<Integer> addCounter, List<Integer> subCounter) {
        super();
        this.process = process;
        this.addCounter = addCounter;
        this.subCounter = subCounter;
    }

    @Override
    public void run() {
        try {
            Socket clientSocket = new Socket(process.getIp(), process.getCommunicationPort());
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            
            for(int i = 0 ; i < addCounter.size(); i++) {
                outToServer.writeInt(addCounter.get(i));
            }
            
            for(int i = 0 ; i < subCounter.size(); i++) {
                outToServer.writeInt(subCounter.get(i));
            }
            
            outToServer.close();
            clientSocket.close();
            
        } catch(Exception e) {
            e.printStackTrace();
        }
        
    }

}
