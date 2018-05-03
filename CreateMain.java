/*
 * class name: CreateMain
 * Authors: Derek Baker
 *              contact: derekjohnbaker23@gmail.com
 *              
 *          Rachayita Giri
 *              contact: rachayitagiri@gmail.com
 *              
 *          Saloni Buddhadeo
 *              contact: salonibuddhadeo@gmail.com
 * 
 * This file contains the functions to iterate over the source folder, replicate the hierarchy of the source folder in the target
 * repository, calculate the artifact IDs of the artifacts, and update the corresponding entry in the manifest file.
 *
 * Also added the Checkout function to create a local copy of a guven project version corresponding to the manifest file referred to by the user.
 * 
 */
import com.opencsv.CSVWriter;

import java.io.*;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class CreateMain {    
    static String mani_path, meta_path;
    static String source_name="";
    
    public static void CreateSource(String[] args) throws IOException {
        String target;
        File[] files = new File(args[1]).listFiles();                   //list the files/dirs in the first argument
        System.out.println("Creating repo...");
        new File(args[2]).mkdir();
        new File(args[2]+"/"+args[1]).mkdir();

        //Create manifest file for the 'create repo' command and '.metadata.json' file
        mani_path = args[2]+"/mani_create_.json";
        meta_path = args[2]+"/.metadata.csv";
        BufferedWriter manibw = new BufferedWriter(new FileWriter(mani_path));
        BufferedWriter metabw = new BufferedWriter(new FileWriter(meta_path));
        //update metadata file for create command
        metadata(args[1]+"/", mani_path, args, args[2]+"/.metadata.csv");
        metabw.close();

        System.out.println("Repo created successfully.");
        target = args[2] + "/";
        showFiles(files, args[1], target);
        
        //modify the manifest file to the correct json format
        //append "," at end of each line and "[", "]" at start and end of file    
        String line;
        StringBuffer file_content = new StringBuffer();
        FileReader file = new FileReader(mani_path);
        BufferedReader br=new BufferedReader(file);
        file_content.append("[");
        //read each line from file
        while((line = br.readLine())!= null){
            file_content.append(line);
            file_content.append(",");
       }
        file_content.append("]");
        manibw.write(file_content.toString());
        manibw.close();
    }

        public static void showFiles(File[] files, String src_name, String target) {
        String parent, file_name, dir_name, artID, ned_stark;

        for (File file : files) {
            ned_stark = src_name + "/";

            if (file.isDirectory()) {                                   //Check if folder or not
                dir_name = file.getName();                              //dir_name = name of the folder
                System.out.println("Dir: " + dir_name);

                ned_stark += dir_name;
                
                String newFolder = target + ned_stark;
                if(!new File(newFolder).exists()) {
                        new File(newFolder).mkdirs();                             //ONLY CREATES A DIR IN THE REPO
                        System.out.println( newFolder+" new folder created" );
                }

                showFiles(file.listFiles(), ned_stark, target/* + ned_stark*/);           //Calls same method again
            }
            else {
                file_name = file.getName();
                System.out.println("File: " + file_name);               //name of the file

                ned_stark += file_name;

                if(!new File(target + ned_stark).exists())    // if folder target+repo_path exists
                    new File(target + ned_stark).mkdir();

                try{
                     artID = copyFile(ned_stark, target + ned_stark);
//                     if(System.out.print(artID != null)){
                        createManifest.Manifest(ned_stark, artID, mani_path); 
//                     }
                }
                catch(Exception e){
                    System.out.println(e+" Exception!");
                }
            }
        }
    }

    public static String copyFile(String file_name, String target) throws IOException{
        //Read the file in the source folder
        FileReader file = new FileReader(file_name);
        BufferedReader br = new BufferedReader(file);

        //Invoke the calculation of the artifact id for the given file
        String artId, file_content="";
        int i, checksum=0, counter=0;
        artId = artID(file_name);

        //----------
        if(target.contains(artId))
            return null;
        String fileDest = target;

        String inputName  = artId;
        String workingDir = System.getProperty("user.dir");
        // a File instance for the directory:
        File workingDirFile = new File(workingDir);
        // a File instance for a file in that directory:
        File testfile = new File(workingDirFile, inputName);

        if(!testfile.exists()){
            //new File(fileDest).mkdir();
            BufferedWriter bw=new BufferedWriter(new FileWriter(fileDest+"/"+artId));

            while((i=br.read())!=-1){
                file_content += (char)i;
            }
            bw.write(file_content);

            br.close();
            bw.close();
            file.close();
            return artId;
        }
        else{
            System.out.print("null");
            return null;
        }
    }

    //Function to checkout files
    //Main.java passes three arguments - repofolder, manifest, targetfolder
    public static void Checkout(String[] args) throws IOException, ParseException {

        String src = args[1];
        String mani = args[2];
        String dest = args[3];
        int i;

        JSONParser parser = new JSONParser();
        //JSONArray a = (JSONArray) parser.parse(new FileReader("/home/rachayitagiri/NetBeansProjects/vcs-swe/src/"+src+"/"+mani));
        JSONArray a = (JSONArray) parser.parse(new FileReader(src+"/"+mani));

        //Process of creating the manifest file for cout command
        String cmd = "cout";
        int c=1;

        mani_path = src+"/mani_cout_1_.json";                      //path where the manifest file will be created, i.r. repo/
        if(new File(mani_path).exists()){
            c = createManifest.maniFileNo(src, cmd) + 1;        //returns the most recent version of the manifest file corresponding to cout & increments it
            mani_path = src+"/mani_cout_"+(c)+"_.json";
        }
        BufferedWriter manibw = new BufferedWriter(new FileWriter(mani_path));      //create a filewriter
//        manibw.close();

        for (Object o : a ) {

            JSONObject artifact = (JSONObject) o; 
            String path = (String) artifact.get("art_src_path");
            String artID = (String) artifact.get("art_ID");

            File myFile = new File(path);
            String myDir = myFile.getParent();          //get parent directory path
            String myFileName = myFile.getName();       //get filename
            System.out.println("Source path: "+path);   //print the path to the corresponding file in the source folder

            //get path from art_src_path and split it to get the corresponding target destination
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
            System.out.println("Destination :"+tgtfolder);

            if (new File(tgtfolder).exists()) {
                System.out.println("Destination directory "+tgtfolder+" exists...");
                System.out.println("Creating file... "+tgtfolder+"/"+myFileName);
            }
            else{
                System.out.println("Creating directory: "+tgtfolder);
                new File(tgtfolder).mkdirs();
            }
           //FileInputStream FI = new FileInputStream("/home/rachayitagiri/NetBeansProjects/vcs-swe/src/"+myFile);
            FileInputStream FI = new FileInputStream(myFile);
            File outfile = new File(tgtfolder+"/"+myFileName);
            outfile.createNewFile();

            FileOutputStream FO = new FileOutputStream(tgtfolder+"/"+myFileName);
            int b;
            //read content and write in another file
            while((b=FI.read())!=-1){ 
                FO.write(b);
            }            
            createManifest.Manifest(tgtfolder+"/"+myFileName, artID, mani_path);        //create the manifest file

            System.out.println("File Copied...");
            FI.close();
    }
        //modify the manifest file to the correct json format
        //append "," at end of each line and "[", "]" at start and end of file
        String line;
        StringBuffer file_content = new StringBuffer();
        FileReader file = new FileReader(mani_path);
        BufferedReader br=new BufferedReader(file);
        file_content.append("[");
        //read each line from file
        while((line = br.readLine())!= null){
            file_content.append(line);
            file_content.append(",");
        }
        file_content.append("]");
        manibw.write(file_content.toString());
        manibw.close();                     // make sure manibw Stream is not closed in this method 

}
    //Function to checkin files
    //Main.java passes two arguments - check in folder, target repo folder    
   public static void Checkin(String[] args) throws IOException{
       String src = args[2]+"/";                            //merge from path enterd by the user - Wall
       String target = args[1]+"/source/";                  //merge to path, entered by the user - KL
       
       String cmd = "cin";
       
       int i=1;
        
        File[] files = new File(src).listFiles();                   //list the files/dirs in the first argument i.e in the existing repo

        mani_path = args[1]+"/mani_cin_1_.json";
        if(new File(mani_path).exists()){
            i = createManifest.maniFileNo(args[1], cmd) + 1;        //does this basically return the most recent version of the manifest file????
            mani_path = args[1]+"/mani_cin_"+(i)+"_.json";
        }

        BufferedWriter manibw = new BufferedWriter(new FileWriter(mani_path));      // Create manifest file
        
        // invoke metadata function to update the metadata file
        metadata(args[1]+"/", mani_path, args, args[1]+"/.metadata.csv");

        showFilesCheckIn(files, src, target);
        System.out.println("Folder Checked in successfully.");

        //modify the manifest file to the correct json format
        //append "," at end of each line and "[", "]" at start and end of file
        
        String line;
        StringBuffer file_content = new StringBuffer();
        FileReader file = new FileReader(mani_path);
        BufferedReader br=new BufferedReader(file);
        file_content.append("[");
        //read each line from file
        while((line = br.readLine())!= null){
            file_content.append(line);
            file_content.append(",");
        }
        file_content.append("]");
        manibw.write(file_content.toString());
        manibw.close();
        
    }

    //function to update .metadata.json file
    //receives four arguments - path to repo, path of the manifest file created, command line arguments, repo name
    public static void metadata(String repo_path, String mani_path, String[] cmd_line, String repo_name){
        try {
            Date date = new Date();
            SimpleDateFormat ft = new SimpleDateFormat ("yyyy:MM:dd hh:mm:ss");
            String timestamp, meta_filename;

            meta_filename = repo_name;
            System.out.println(meta_filename);

            //calculate Object id
            int objID = countLines(meta_filename);      // total number of lines includes header line too. objID -- object id for th ehierarchy

            // calculate the parent ID
            int parentID = parentId(repo_path, cmd_line, repo_name);
            
            try (
                Writer writer = Files.newBufferedWriter(Paths.get(meta_filename), 
                     StandardOpenOption.APPEND);
                CSVWriter csvWriter = new CSVWriter(writer,
                    CSVWriter.DEFAULT_SEPARATOR,
                    CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END);
            ) {
            //Recording values in an list of object for CSV
            if(meta_filename.length() == 60){
                String[] headerRecord = {"id", "mani_path", "cmd_line", "timestamp", "parent_id"};
                csvWriter.writeNext(headerRecord);
            }

            csvWriter.writeNext(new String[]{Integer.toString(objID), mani_path, String.join(" ", cmd_line), ft.format(date), Integer.toString(parentID)});
                    System.out.println(parentID+" parent id"+Arrays.toString(cmd_line));

            // Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(repo_path+".metadata.csv", true), "UTF-8"));

            /*out.write(jsonObj.toJSONString().replace("\\","")+"\n");
            System.out.println(jsonObj.toJSONString().replace("\\","")+"\n");*/
            // out.close();
            }
        }
        catch(Exception e) {
            System.out.println(e+"Metadata error");
        }
    }
    public static int countLines(String filename) throws IOException {
        InputStream is = new BufferedInputStream(new FileInputStream(filename));
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
            return (count == 0 && !empty) ? 1 : count;
        } finally {
            is.close();
        }
    }

    //function to calculate parent id of the current manifest file
    public static int parentId(String repo_path, String[] cmd_line, String repo) throws IOException, ParseException{

        int objCount, parentId = 0;
        String ref_mani, line="", csvSplit=",";
        objCount = countLines(repo) - 1;
        String[] csvContent = new String[0];
        
        // parent id of the create manifest will be zero
        if(objCount == 0){
            parentId = 0;
        }
        else{
            switch(cmd_line[0]){
                case "cout":
                        int index = 1;
                        ref_mani = cmd_line[2];
                        FileReader file = new FileReader(repo);
                        BufferedReader br=new BufferedReader(file);

                        while ((line = br.readLine()) != null) {
                            csvContent = line.split(csvSplit);
                            if(csvContent[2].equals("repo/"+ref_mani)) {
                                parentId = index; 
                            }
                            index++;
                        }
                case "cin":
                        parentId = objCount;
                default:
                        System.out.println("Learn to code!"+ Arrays.toString(cmd_line));
            }
        }
        return parentId;
    }

     public static void showFilesCheckIn(File[] files, String src_path, String target) {
        String parent, file_name, dir_name, artID, jon_snow;

        for (File file : files) {
            jon_snow = "/";

            if (file.isDirectory()) {
                dir_name = file.getName();                              //dir_name = name of the folder
                System.out.println("Dir: " + dir_name);

                jon_snow += dir_name;//+"/";
                System.out.println(jon_snow + "  ");

                String newFolder = target + jon_snow;
                if(!new File(newFolder).exists()) {
                        new File(newFolder).mkdirs();
                        System.out.println( newFolder+" new folder created" );
                }
                
                showFilesCheckIn(file.listFiles(), src_path + jon_snow, target + jon_snow);
            }

            else {
                file_name = file.getName();
                System.out.println("File: " + file_name);

                jon_snow += file_name;
                System.out.println(target+" "+jon_snow+"  ");

                if(!new File(target + jon_snow).exists())    // if folder target+repo_path exists
                    new File(target + jon_snow).mkdir();

                try{
                    System.out.println(src_path + " " + jon_snow+" " + target + " " + jon_snow);
                    artID = copyFile(src_path + jon_snow, target + jon_snow);
//                     if(System.out.print(artID != null)){
                    System.out.println("artID okay!");
                        createManifest.Manifest(src_path + jon_snow, artID, mani_path);   // ( , , )
//                     }
                }
                catch(Exception e){
                    System.out.println(e+" Exception!"+ mani_path +" "+ target+ " " + jon_snow);
                }
            }
        }
    }

    
public static String artID(String file_name) throws IOException{
        // String file_name = "hp.txt";

        FileReader file = new FileReader(file_name);
        File afile = new File(file_name);
        BufferedReader br=new BufferedReader(file);
        String art_file_name;
        int i, checksum=0, counter=0;
        char cha = '\0';
        int weights[] = {1,7,11,17};
        // double file_size = file.length();

        while((i=br.read())!=-1){
            cha = (char)i;                                      //Converting string to char
            checksum += (int) (char) i * weights[counter%4];    //checksum calculation - ascii of character * corresponding weight
            counter++;                                          //counter for weights
        }
        checksum -= (int) cha * weights[(counter-1)%4];
        checksum %= (Math.pow(2,31) - 1);
        art_file_name = checksum+"-L"+afile.length()+".txt";    // artifactID - new file name

        br.close();
        file.close();

        return art_file_name;
    }
}
