import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * This class responds to each sync connection made by another process.
 */
public class SyncCommandProcessorRunnable implements Runnable {
    
    final private Socket socket;
    final private GlobalDataHolder globalDataHolder;
    final private int numberOfProcesses;
    
    /**
     * Constructor.
     * @param socket
     * @param globalDataHolder
     * @param numberOfProcesses
     */
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
           
            ArrayList<Integer> addCounter = readAddCounterFromSocket(dIn);
            ArrayList<Integer> subCounter = readSubCounterFromSocket(dIn);
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
    
    /**
     * Reads the addCounter from the socket.
     * @param dIn
     * @return
     * @throws IOException
     */
    private ArrayList<Integer> readAddCounterFromSocket(final DataInputStream dIn) throws IOException {
        ArrayList<Integer> addCounter = new ArrayList<>();
        for(int i =0 ; i< numberOfProcesses; i++) {
            int count = dIn.readInt();
            addCounter.add(i, count);
            System.out.println("Add counter : " + i + " : " + count);
        }
        return addCounter;
    }
    
    /**
     * Reads the subCounter from the socket.
     * @param dIn
     * @return
     * @throws IOException
     */
    private ArrayList<Integer> readSubCounterFromSocket(final DataInputStream dIn) throws IOException {
        ArrayList<Integer> subCounter = new ArrayList<>();
        for(int i =0 ; i< numberOfProcesses; i++) {
            int count = dIn.readInt();
            subCounter.add(i, count);
            System.out.println("Sub counter : " + i + " : " + count);
        }
        
        return subCounter;
    }
    


}
