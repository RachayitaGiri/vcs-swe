import java.io.*;
import com.opencsv.CSVWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.simple.parser.ParseException;

/*
 * class name: UpdateMetadata
 * Authors: Derek Baker
 *              contact: derekjohnbaker23@gmail.com
 *              
 *          Rachayita Giri
 *              contact: rachayitagiri@gmail.com
 *              
 *          Saloni Buddhadeo
 *              contact: salonibuddhadeo@gmail.com
 * 
 * Contains the following functions:
 * -> metadata() - to write contents to .metadata.csv file
 * -> countLines() - to calculate the number of rows in the given file
 * -> parentId() - to calculate the parent Id of the given manifest file
 */

public class UpdateMetadata {

	//Function to update .metadata.csv file
    //Receives four arguments - path to repo, path of the manifest file created, command line arguments, path to meta file
    public static void metadata(String repo_path, String mani_path, String[] cmd_line, String meta_filename){
        
        try {
            Date date = new Date();
            SimpleDateFormat ft = new SimpleDateFormat ("yyyy:MM:dd hh:mm:ss");
            String timestamp;

            //Calculate Object ID for a given row in the meta file
            int objID = countLines(meta_filename) + 1;      

            //Calculate the Parent ID for the given row in the meta file
            int parentID = parentId(repo_path, cmd_line, meta_filename);
            
            //Open the CSV Writer to enable writing in the meta file
            try (
                Writer writer = Files.newBufferedWriter(Paths.get(meta_filename), 
                     			StandardOpenOption.APPEND);
                CSVWriter csvWriter = new CSVWriter(writer,
			                    CSVWriter.DEFAULT_SEPARATOR,
			                    CSVWriter.NO_QUOTE_CHARACTER,
			                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
			                    CSVWriter.DEFAULT_LINE_END);
            ) {

	            //Write the values in the row of the meta file 
	            csvWriter.writeNext(new String[]{Integer.toString(objID), 
	            								 mani_path, 
	            								 String.join(" ", cmd_line), 
	            								 ft.format(date), 
	            								 Integer.toString(parentID)});
            }
        }
        catch(Exception e) {
            System.out.println("Metadata file error: " + e);
        }
    }

    //Function to calculate the number of rows in the given file
    //Receives the path to a file; returns the number of rows in file
    public static int countLines(String filename) throws IOException {

    	//Open the reader for the file
        InputStream is = new BufferedInputStream(new FileInputStream(filename));

        //Iterate over file and calculate the number of rows
        try {
            byte[] c = new byte[1024];
            int count = 0;
            int readChars = 0;
            boolean empty = true;
            while ((readChars = is.read(c)) != -1) {
                empty = false;
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
            }

            //Return the count
            return (count == 0 && !empty) ? 1 : count;

        } finally {
            is.close();
        }
    }

    //Function to calculate parent id of the given manifest file
    public static int parentId(String repo_path, String[] cmd_line, String metafilename) throws IOException, ParseException {

        int objCount, parentId = 0;
        String ref_mani, line="", csvSplit=",";
        objCount = countLines(metafilename);
        String[] csvContent = new String[0];
        
        //Parent ID of the create command (first row of the file) will be zero
        if(objCount == 0){
            parentId = 0;
        }

        else{
            switch(cmd_line[0]){
                case "cout":
                        int index = 1;
                        //ref_mani = Manifest file referred to by the cout command
                        ref_mani = cmd_line[2];

                        //Open the buffered reader for the meta file
                        FileReader file = new FileReader(metafilename);
                        BufferedReader br=new BufferedReader(file);

                        /*
                         * For a row corresponding to the 'cout' command,
                         * the parent ID equals the object ID of the manifest it refers to
                         */ 
                        while ((line = br.readLine()) != null) {
                            csvContent = line.split(csvSplit);
                            if(csvContent[1].contains(ref_mani)) {
                                parentId = index;
                                break;
                            }
                            index++;
                        }
                        break;

                case "cin":
                		/*
                         * For a row corresponding to the 'cin' command,
                         * the parent ID equals the object ID of ther previous row
                         */ 
                        parentId = objCount;
                        break;
                default:
                        break;
            }
        }

        //Return the parent ID
        return parentId;
    }	
}