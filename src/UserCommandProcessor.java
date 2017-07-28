
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

/**
 * This class starts the server socket and creates threads to handle user request. 
 *
 */
public class UserCommandProcessor {
    
    
    final private Process process;
    final private ExecutorService executor;
    final private GlobalDataHolder globalDataHolder;
    
    /**
     * Contructor.
     * @param process
     * @param executor
     * @param globalDataHolder
     */
    public UserCommandProcessor(Process process, ExecutorService executor, GlobalDataHolder globalDataHolder) {
        this.process = process;
        this.executor = executor;
        this.globalDataHolder = globalDataHolder;
    }
    
    /**
     * Starts a server socket and responds to incoming socket connection.
     * @throws IOException
     */
    public void processUserCommands() throws IOException {
        ServerSocket serverSocket  = new ServerSocket(process.getUserCommandPort());

        while (true) {
           System.out.println("Sever Ready to accept user commands");
           Socket socket = serverSocket.accept();
           
           System.out.println("Got a Client connection" + socket);
           // new thread for a client
           executor.execute(new UserCommandProcessorRunnable(socket, globalDataHolder));
        }
    }
    

}
