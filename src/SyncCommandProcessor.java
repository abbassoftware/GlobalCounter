import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class SyncCommandProcessor {
    
    private Process process;
    private ExecutorService executor;
    private GlobalDataHolder globalDataHolder;
    final private int numberOfProcesses;

    public SyncCommandProcessor(Process process, 
            ExecutorService executor, 
            GlobalDataHolder globalDataHolder,
            int numberOfProcesses) {
        this.process = process;
        this.executor = executor;
        this.globalDataHolder = globalDataHolder;
        this.numberOfProcesses = numberOfProcesses;
    }
    
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
