
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;


public class UserCommandProcessorRunnable implements Runnable {

    private static String SUCESSS = "success";
    private static String FAILURE = "failure";

    
    private Socket socket;
    private GlobalDataHolder globalDataHolder;
    public UserCommandProcessorRunnable(Socket socket, 
            GlobalDataHolder globalDataHolder) {
        this.socket = socket;
        this.globalDataHolder = globalDataHolder;
    }
    
    
    @Override
    public void run() {
        InputStream in = null;
        OutputStream out = null;
        boolean responseWritten = false;

        try {
            
            System.out.println("Reading data");
            String command = "";
            in = socket.getInputStream();
            for (int b = 0; ((b = in.read()) >= 0);) {
                System.out.println(b + " " + (char) b);
                command = command + (char)b;
            }
            
            System.out.println("Got Command " + command);
            out = socket.getOutputStream();
            switch(command) {
                case "inc" :
                    globalDataHolder.incrementCounter();
                    out.write(SUCESSS.getBytes());
                    responseWritten = true;
                    break;
                case "dec" :
                    globalDataHolder.incrementCounter();
                    out.write(SUCESSS.getBytes());
                    responseWritten = true;
                    break;
                case "get" :
                    int count = globalDataHolder.getCount();
                    out.write(count);
                    responseWritten = true;
                    break;
                
               default:
                    out.write(FAILURE.getBytes());
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(out != null) {
                try {
                    if(responseWritten == false) {
                        out.write(FAILURE.getBytes());
                    }
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            
            if(socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    

}
