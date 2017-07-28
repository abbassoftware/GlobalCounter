
/**
 * This class holds the information regarding one process.
 * 
 */
public class Process {
    
    private String ip;
    private int userCommandPort;
    private int communicationPort;
    
    
    /**
     * Constructor.
     * @param ip
     * @param userCommandPort
     * @param communicationPort
     */
    public Process(String ip, int userCommandPort, int communicationPort) {
        super();
        this.ip = ip;
        this.userCommandPort = userCommandPort;
        this.communicationPort = communicationPort;
    }
    
    /**
     * 
     * @return IP address of the process.
     */
    public String getIp() {
        return ip;
    }
  
    /**
     * 
     * @return Port number where the process listens to user command.
     */
    public int getUserCommandPort() {
        return userCommandPort;
    }
   
    /**
     * 
     * @return Port number where the process listens to sync command.
     */
    public int getCommunicationPort() {
        return communicationPort;
    }
    
    @Override
    public String toString() {
        return "Process [ip=" + ip + ", userCommandPort=" + userCommandPort + ", communicationPort=" + communicationPort
                + "]";
    }

}
