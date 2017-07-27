import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class UserCommandClient {
    
    public static String INC = "inc";
    public static String DEC = "dec";
    public static String GET = "get";
    
    public static void main(String[] args) {
        try {
            
            //sendCommand(GET);
            sendCommand(INC, 6900);
            sendCommand(INC, 6900);
            sendCommand(DEC, 6902);
            
            Thread.sleep(2000);
            sendCommand(GET, 6900);
            sendCommand(GET, 6902);

            
            //sendCommand(GET);
            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void sendCommand(String command, int port) throws UnknownHostException, IOException {
        Socket clientSocket = new Socket("localhost", port);
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        outToServer.writeBytes(command);
        
        
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        if(command.equals(GET)) {
            int response = inFromServer.read();
            System.out.println(response);
        } else {
            String response = inFromServer.readLine();
            System.out.println(response);
        }
        outToServer.close();
        clientSocket.close();
    }

}
