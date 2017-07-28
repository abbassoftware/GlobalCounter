
import java.io.IOException;
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
            if(args.length != 4) {
                System.out.println("usage : <program-name> -id 1 -file processes.txt");
                return;
            }
            
            for(int i=0;i<args.length ; i++) {
                System.out.println(i + ":" + args[i]);
            }
            
            int processNumber = getProcessNumber(args);
            String filePath = getFilePath(args);
            System.out.println(filePath);
            
            //Parse the input file
            InputFileParser inputFileParser = new InputFileParser(filePath);
            List<Process> processes = inputFileParser.parse();
            
            for(int i =0 ; i< processes.size(); i++) {
                System.out.println(processes.get(i));
            }
            
            //Create the Objects
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
            
            SyncCommandProcessor syncCommandProcessor = 
                    new SyncCommandProcessor(processes.get(processNumber),
                            Executors.newFixedThreadPool(processes.size()), 
                            globalDataHolder, processes.size());
            
            //Start the servers.
            startUserCommandsServerAsyc(userCommandProcessor);
            startSyncCommandsServerAsyc(syncCommandProcessor);

        } catch(Exception e) {
            System.out.println(e);
        }
        
    }
    
    /**
     * Gets the Process number from command line arguments.
     * @param args
     * @return
     */
    
    static private int getProcessNumber(String[] args) {
        if(args[0].equals("-id")) {
            return Integer.parseInt(args[1]) - 1; 
        } else {
            System.out.println("usage : <program-name> -id 1 -file processes.txt");
            System.exit(0);
            return -1;
        }
    }
    
    /**
     * Gets the file path from command line arguments.
     * @param args
     * @return
     */
    static private String getFilePath(String[] args) {
        if(args[2].equals("-file")) {
            return args[3];
        } else {
            System.out.println("usage : <program-name> -id 1 -file processes.txt");
            System.exit(0);
            return null;
        }
    }
    
    /**
     * Starts the User command server.
     * @param userCommandProcessor
     */
    static private void startUserCommandsServerAsyc(UserCommandProcessor userCommandProcessor) {
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
    }
    
    
    /**
     * Starts the Sync command server.
     * @param syncCommandProcessor
     */
    static private void startSyncCommandsServerAsyc(SyncCommandProcessor syncCommandProcessor) {
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
    }


}
