

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class InputFileParser {
    
    public String filepath;
    public InputFileParser(String filePath) {
        this.filepath = filePath;
    }
    
    public List<Process>  parse() throws IOException {
        
        ArrayList<Process> processes = new ArrayList<Process>();
        
        FileInputStream fstream = new FileInputStream(filepath);
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

        String strLine;

        //Read File Line By Line
        while ((strLine = br.readLine()) != null)   {
            String[] elements = strLine.split(" ");
            if(elements.length != 3) {
                throw new RuntimeException("Invalid input file");
            }
            
            processes.add(new Process(elements[0],
                    Integer.parseInt(elements[1]), 
                    Integer.parseInt(elements[2])));
        }

        //Close the input stream
        br.close();
        
        return processes;
        
        
    }

}
