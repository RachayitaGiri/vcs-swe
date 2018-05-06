import java.io.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/*
 * class name: Merge
 * Authors: Derek Baker
 *              contact: derekjohnbaker23@gmail.com
 *              
 *          Rachayita Giri
 *              contact: rachayitagiri@gmail.com
 *              
 *          Saloni Buddhadeo
 *              contact: salonibuddhadeo@gmail.com
 * 
 * Execute the checkout command
 * Contains the following functions:
 * -> checkout() - create the manifest file for the 'cout' command, and check out the required version 
 */

public class Run_Checkout {
    static String mani_path;
	//Function to checkout files
    //Main.java passes three arguments - repofolder, manifest, targetfolder
    public static void checkout(String[] args) throws IOException, ParseException {

        String src = args[1];
        String mani = args[2];
        String dest = args[3];
        int i, b;

        //Parse the manifest file using the JSON Parser
        JSONParser parser = new JSONParser();
        JSONArray a = (JSONArray) parser.parse(new FileReader(src+"/"+mani));

        //Create the manifest file for the 'cout' command
        String cmd = "cout";
        int c=1;

        //Path where the manifest file will be created
        mani_path = src+"/mani_cout_1_.json";        

        //Returns the most recent version of the manifest file corresponding to cout and increments it
        if(new File(mani_path).exists()){
            c = CreateManifest.maniFileNo(src, cmd) + 1;        
            mani_path = src+"/mani_cout_"+(c)+"_.json";
        }

        //Create a filewriter for the manifest file
        BufferedWriter manibw = new BufferedWriter(new FileWriter(mani_path));      

        //Invoke metadata function to update the metadata file
        UpdateMetadata.metadata(args[1]+"/", mani_path, args, args[1]+"/.metadata.csv");

        //Perform the checkout
        for (Object o : a ) {

            JSONObject artifact = (JSONObject) o; 
            String path = (String) artifact.get("art_src_path");
            String artID = (String) artifact.get("art_ID");

            File myFile = new File(path);
            String myDir = myFile.getParent();          //Get parent directory path
            String myFileName = myFile.getName();       //Get filename
            System.out.println("Source path: "+path);   //Print the path to the corresponding file in the source folder

            //Get path from 'art_src_path' and split it to get the corresponding target destination
            String parts[] = myDir.split("/");
            String[] arr = new String[parts.length-1];
            for (i=0;i<parts.length-1;i++ ) {
                arr[i] = parts[i+1];
            }
            StringBuilder result = new StringBuilder();
            for(String string : arr) {
                result.append(string);
                result.append("/");
            }
            String tgtfolder = result.length() > 0 ? result.substring(0, result.length() - 1): ""; 
            tgtfolder = dest+"/"+tgtfolder;
            System.out.println("Destination: "+tgtfolder);

            if (new File(tgtfolder).exists()) {
                System.out.println("Destination directory "+tgtfolder+" exists...");
                System.out.println("Creating file: "+tgtfolder+"/"+myFileName+"...");
            }
            else{
                System.out.println("Creating directory: "+tgtfolder+"...");
                new File(tgtfolder).mkdirs();
            }

            //Create the file in the checked out folder with the original extension
            FileInputStream FI = new FileInputStream(myFile);
            File outfile = new File(tgtfolder+"/"+myFileName);
            outfile.createNewFile();

            //Copy the contents from the artifact to the checked out file
            FileOutputStream FO = new FileOutputStream(tgtfolder+"/"+myFileName);
            while((b=FI.read())!=-1){ 
                FO.write(b);
            }            

            //Update the manifest file corresponding to cout
            CreateManifest.Manifest(tgtfolder+"/"+myFileName, artID, mani_path);        

            System.out.println("File Copied...");
            FI.close();
    	}

        //Modify the manifest file to the correct json format
        //Append "," at end of each line and "[", "]" at start and end of file
        String line;
        StringBuffer file_content = new StringBuffer();
        FileReader file = new FileReader(mani_path);
        BufferedReader br=new BufferedReader(file);
        file_content.append("[");
        
        //Read each line from file
        while((line = br.readLine())!= null){
            file_content.append(line);
            file_content.append(",");
        }
        file_content.append("]");

        //Close the buffered writer
        manibw.write(file_content.toString());
        manibw.close();                     
	}
}