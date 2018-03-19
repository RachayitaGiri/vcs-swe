import java.io.*;
import java.util.Scanner;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

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

public class CreateMain {    
    
    public static void CreateSource(String[] args) throws IOException {
        String target;
        File[] files = new File(args[0]).listFiles();                   //list the files/dirs in the first argument
        System.out.println("Creating repo...");
        new File(args[1]).mkdir();
        new File(args[1]+"/"+args[0]).mkdir();

        //Create manifest file
        String a = args[1]+"/mani_create.txt";
		BufferedWriter manibw = new BufferedWriter(new FileWriter(a));	
		manibw.write("create " + args[0] + " " + args[1]);
		manibw.close();

        System.out.println("Repo created successfully.");
        target = args[1] + "/";
        showFiles(files, args[0], target);                      
        }

    public static void showFiles(File[] files, String repo_name, String target) {
        String parent, file_name, dir_name, repo_path, artID;
        for (File file : files) {
            repo_path = repo_name + "/";

            if (file.isDirectory()) {                                   //Check if folder or not
                dir_name = file.getName();                              //dir_name = name of the folder
                System.out.println("Dir: " + dir_name);

                repo_path += file.getName();                            //repository PATH name - repo_path
                String newFolder = target+repo_path;
                System.out.println( newFolder );                
                new File(newFolder).mkdirs();                             
                showFiles(file.listFiles(), repo_path, target);         //Calls same method again
            }
            else {
                file_name = file.getName();
                System.out.println("File: " + file_name);               //name of the file
                /*String folderName = repo_path;*/
                repo_path += file_name;
                new File(target+repo_path).mkdir();
//              String file_path = repo_path + file_name;               //file path = repository path + the folder created for file to save artifact files
                System.out.println(repo_path+" repo_path");
                try{
                     artID = copyFile(repo_path,target);  
                     createManifest.Manifest(repo_path, artID, target);                  
                }
                catch(Exception e){
                    System.out.println(e+" Exception");
                }
            }
        }
    }
    public static String copyFile(String file_name, String target) throws IOException{
        
        FileReader file = new FileReader(file_name);
        BufferedReader br=new BufferedReader(file);

        String repo_file, file_content="";
        int i, checksum=0, counter=0;
        repo_file = artID(file_name);
        String fileDest = target+file_name;
        BufferedWriter bw = new BufferedWriter(new FileWriter(fileDest+"/"+repo_file));
 
        while((i=br.read())!=-1){
            file_content += (char)i;
        }
        bw.write(file_content);
        
        br.close();
        bw.close();
        file.close();
        return repo_file;
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
//Function to checkout files
    //Main.java passes three arguments - repofolder, manifest, targetfolder
    public static void Checkout(String[] args) throws IOException, ParseException {

        String src = args[0];
        String mani = args[1];
        String dest = args[2];

        int i;

        JSONParser parser = new JSONParser();
        JSONArray a = (JSONArray) parser.parse(new FileReader("/home/rachayitagiri/NetBeansProjects/vcs-swe/src/"+src+"/"+mani));

        for (Object o : a ) {

            JSONObject artifact = (JSONObject) o; 
            String path = (String) artifact.get("art_src_path");

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
            else 
            {
                System.out.println("Creating directory: "+tgtfolder);
                new File(tgtfolder).mkdirs();
            }
            FileInputStream FI = new FileInputStream("/home/rachayitagiri/NetBeansProjects/vcs-swe/src/"+myFile);
            File outfile = new File(tgtfolder+"/"+myFileName);

            outfile.createNewFile();

            FileOutputStream FO = new FileOutputStream(tgtfolder+"/"+myFileName);
            int b;
            //read content and write in another file
            while((b=FI.read())!=-1)
            { 
                FO.write(b);
            }
            System.out.println("File Copied...");
            FI.close();
    }
}
}
