import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 */
public class SyncCommandProcessorRunnable implements Runnable {
    
    final private Socket socket;
    final private GlobalDataHolder globalDataHolder;
    final private int numberOfProcesses;
    public SyncCommandProcessorRunnable(Socket socket, 
            GlobalDataHolder globalDataHolder , int numberOfProcesses) {
        this.socket = socket;
        this.globalDataHolder = globalDataHolder;
        this.numberOfProcesses = numberOfProcesses;
    }
    
    
    @Override
    public void run() {
        DataInputStream dIn = null;

        try {
            
            System.out.println("Reading sync data");
            dIn = new DataInputStream(socket.getInputStream());
            ArrayList<Integer> addCounter = new ArrayList<>();
            for(int i =0 ; i< numberOfProcesses; i++) {
                int count = dIn.readInt();
                addCounter.add(i, count);
                System.out.println("Add counter : " + i + " : " + count);
            }
            
            ArrayList<Integer> subCounter = new ArrayList<>();
            for(int i =0 ; i< numberOfProcesses; i++) {
                int count = dIn.readInt();
                subCounter.add(i, count);
                System.out.println("Sub counter : " + i + " : " + count);
            }
            globalDataHolder.merge(addCounter, subCounter);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            
            if(socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    

}
