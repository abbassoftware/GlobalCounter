
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;


public class UserCommandProcessor {
    
    
    private Process process;
    private ExecutorService executor;
    private GlobalDataHolder globalDataHolder;
    public UserCommandProcessor(Process process, ExecutorService executor, GlobalDataHolder globalDataHolder) {
        this.process = process;
        this.executor = executor;
        this.globalDataHolder = globalDataHolder;
    }
    
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
