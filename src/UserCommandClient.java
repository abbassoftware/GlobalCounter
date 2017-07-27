import java.io.DataOutputStream;
import java.net.Socket;

public class UserCommandClient {
    
    public static String INC = "inc";
    public static String DEC = "dec";
    public static String GET = "get";
    
    public static void main(String[] args) {
        try {
            Socket clientSocket = new Socket("localhost", 6900);
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            outToServer.writeBytes(INC);
            outToServer.close();
            clientSocket.close();
            
            Socket clientSocket2 = new Socket("localhost", 6900);
            DataOutputStream outToServer2 = new DataOutputStream(clientSocket.getOutputStream());
            outToServer2.writeBytes(INC);
            outToServer2.close();
            clientSocket2.close();
            
            
            Socket clientSocket3 = new Socket("localhost", 6900);
            DataOutputStream outToServer3 = new DataOutputStream(clientSocket.getOutputStream());
            outToServer3.writeBytes(INC);
            outToServer3.close();
            clientSocket3.close();
            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
