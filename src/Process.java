
public class Process {
    
    private String ip;
    private int userCommandPort;
    private int communicationPort;
    
    
    
    public Process(String ip, int userCommandPort, int communicationPort) {
        super();
        this.ip = ip;
        this.userCommandPort = userCommandPort;
        this.communicationPort = communicationPort;
    }
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public int getUserCommandPort() {
        return userCommandPort;
    }
    public void setUserCommandPort(int userCommandPort) {
        this.userCommandPort = userCommandPort;
    }
    public int getCommunicationPort() {
        return communicationPort;
    }
    public void setCommunicationPort(int communicationPort) {
        this.communicationPort = communicationPort;
    }
    @Override
    public String toString() {
        return "Process [ip=" + ip + ", userCommandPort=" + userCommandPort + ", communicationPort=" + communicationPort
                + "]";
    }

}
