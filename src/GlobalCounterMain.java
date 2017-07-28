
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

/**
 * 
 * The main class which creates the different objects and starts the server.
 *
 */
public class GlobalCounterMain {
    
    private static ExecutorService executor = Executors.newFixedThreadPool(2);
    
    private static int MAX_THREADS_FOR_USER_COMMANDS = 10;
    
    public static void main(String[] args) {
        
        try {
            InetAddress iAddress = InetAddress.getLocalHost();
            String server_IP = iAddress.getHostAddress();
            System.out.println("Server IP address : " +server_IP);
        } catch (UnknownHostException e) {
        }

       try {
            if(args.length != 4) {
                System.out.println("usage : <program-name> -id 1 -file processes.txt");
                return;
            }
            
            for(int i=0;i<args.length ; i++) {
                System.out.println(i + ":" + args[i]);
            }
            
            int processNumber = 0;
            String filePath = "";
            if(args[0].equals("-id")) {
                processNumber = Integer.parseInt(args[1]) - 1; 
            } else {
                System.out.println("usage : <program-name> -id 1 -file processes.txt");
                return;
            }
            
            if(args[2].equals("-file")) {
                filePath = args[3];
            } else {
                System.out.println("usage : <program-name> -id 1 -file processes.txt");
                return;
            }
            System.out.println(filePath);
            InputFileParser inputFileParser = new InputFileParser(filePath);
            List<Process> processes = inputFileParser.parse();
            
            for(int i =0 ; i< processes.size(); i++) {
                System.out.println(processes.get(i));
            }
            
            GlobalDataHolder globalDataHolder = new GlobalDataHolder(processNumber, 
                    processes.size(),
                    Executors.newFixedThreadPool(1));
            
            SyncStatePublisher statePublisher = new SyncStatePublisher
                    (processes, globalDataHolder, processNumber, 
                            Executors.newFixedThreadPool(processes.size()));
            
            globalDataHolder.setStatePublisher(statePublisher);
            UserCommandProcessor userCommandProcessor = 
                    new UserCommandProcessor(processes.get(processNumber),
                            Executors.newFixedThreadPool(MAX_THREADS_FOR_USER_COMMANDS), 
                            globalDataHolder);
            
            executor.execute( new Runnable() {
                
                @Override
                public void run() {
                    try {
                        userCommandProcessor.processUserCommands();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    
                }
            });
            
            
            SyncCommandProcessor syncCommandProcessor = 
                    new SyncCommandProcessor(processes.get(processNumber),
                            Executors.newFixedThreadPool(processes.size()), 
                            globalDataHolder, processes.size());
            
            
            executor.execute( new Runnable() {
                
                @Override
                public void run() {
                    try {
                        syncCommandProcessor.processSyncCommands();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    
                }
            });
            
           
        } catch(Exception e) {
            System.out.println(e);
        }
        
        
        
    }

}
