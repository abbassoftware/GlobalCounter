import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

/**
 * This class starts the server socket and creates threads to handle sync request. 
 *
 */
public class SyncCommandProcessor {
    
    final private Process process;
    final private ExecutorService executor;
    final private GlobalDataHolder globalDataHolder;
    final private int numberOfProcesses;

    /**
     * Constructor.
     * @param process
     * @param executor
     * @param globalDataHolder
     * @param numberOfProcesses
     */
    public SyncCommandProcessor(Process process, 
            ExecutorService executor, 
            GlobalDataHolder globalDataHolder,
            int numberOfProcesses) {
        this.process = process;
        this.executor = executor;
        this.globalDataHolder = globalDataHolder;
        this.numberOfProcesses = numberOfProcesses;
    }
    
    /**
     * Starts a server socket and responds to incoming socket connection.
     * @throws IOException
     */
    
    public void processSyncCommands() throws IOException {
        ServerSocket serverSocket  = new ServerSocket(process.getCommunicationPort());

        while (true) {
           System.out.println("Sever Ready to accept sync commands");
           Socket socket = serverSocket.accept();
           
           System.out.println("Got a Client connection for sync" + socket);
           // new thread for a client
           executor.execute(new SyncCommandProcessorRunnable(socket, 
                   globalDataHolder, numberOfProcesses));
        }
    }

}
