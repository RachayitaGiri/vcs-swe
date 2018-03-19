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
import java.io.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class CreateMain {    
    static String a;
    static String source_name="";
    
    public static void CreateSource(String[] args) throws IOException {
        String target;
        File[] files = new File(args[0]).listFiles();                   //list the files/dirs in the first argument
        System.out.println("Creating repo...");
        new File(args[1]).mkdir();
        new File(args[1]+"/"+args[0]).mkdir();

        //Create manifest file
        a = args[1]+"/mani_create_.json";
        BufferedWriter manibw = new BufferedWriter(new FileWriter(a));	//------------APPENDING ISSUES IN FILE
	manibw.write("//CREATE " + args[0] + "/" + args[1]+"\n");
	manibw.close();

        System.out.println("Repo created successfully.");
        target = args[1] + "/";
        showFiles(files, args[0], target);                      //args[1] + "/" + 
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
            else{
                System.out.println("Creating directory: "+tgtfolder);
                new File(tgtfolder).mkdirs();
            }
            FileInputStream FI = new FileInputStream("/home/rachayitagiri/NetBeansProjects/vcs-swe/src/"+myFile);
            File outfile = new File(tgtfolder+"/"+myFileName);

            outfile.createNewFile();

            FileOutputStream FO = new FileOutputStream(tgtfolder+"/"+myFileName);
            int b;
            //read content and write in another file
            while((b=FI.read())!=-1){ 
                FO.write(b);
            }
            System.out.println("File Copied...");
            FI.close();
    }
}
    //Function to checkin files
    //Main.java passes two arguments - check in folder, target repo folder    
   public static void Checkin(String[] args) throws IOException{
       String src = args[0]+"/";                   //merge from path enterd by the user 
       
       String target = args[1]+"/source";                //merge to path, entered by the user
       String cmd = "cin";
       int i=1;
        
        File[] files = new File(src).listFiles();                   //list the files/dirs in the first argument

        a = args[1]+"/mani_cin_1_.json";
        if(new File(a).exists()){
            i = createManifest.maniFileNo(args[1], cmd) + 1;
            a = args[1]+"/mani_cin_"+(i)+"_.json";
        }    
        
        BufferedWriter manibw = new BufferedWriter(new FileWriter(a));	//------------APPENDING ISSUES IN FILE
        //Create manifest file
        manibw.write("//CHECKIN " + args[0] +" "+ args[1]+"\n");
        manibw.close();
        
        System.out.println("File Checked in successfully.");
        
        showFiles(files, args[0], target);                  //args[1] + "/" + 
    }
    
    public static void showFiles(File[] files, String repo_, String target) {
        String parent, file_name, dir_name, repo_path, artID, repo_name;

        for (File file : files) {
            repo_name = repo_+"/";
            repo_path = target+"/";
            
            if (file.isDirectory()) {                                   //Check if folder or not
                dir_name = file.getName();                              //dir_name = name of the folder
                System.out.println("Dir: " + dir_name);

                repo_path += file.getName();                            //repository PATH name - repo_path
                repo_name += dir_name;
                
                String newFolder = repo_path;
                System.out.println( newFolder+"new filesdfd" );
                if(!new File(newFolder).exists()) {
                        new File(newFolder).mkdirs();                             //ONLY CREATES A DIR IN THE REPO
                }                             //ONLY CREATES A DIR IN THE REPO
//                target+= 
                showFiles(file.listFiles(), repo_name, repo_path);           //Calls same method again
            }
            else {
                file_name = file.getName();
                System.out.println("File: " + file_name);               //name of the file
                /*String folderName = repo_path;*/
                
                repo_path += file_name;
                repo_name += file_name;
                if(!new File(repo_path).exists())    // if folder target+repo_path exists
                    new File(repo_path).mkdir();
                
//              String file_path = repo_path + file_name;               //file path = repository path + the folder created for file to save artifact files
                System.out.println(repo_path+" repo_path");
                try{
                     artID = copyFile(repo_name,repo_path);
//                     if(System.out.print(artID != null)){
                        System.out.println("-------0-0-0-0-0-0-"+artID+"-0-0-0-0-0-0-0--------"+a+repo_path);
                        createManifest.Manifest(repo_path, artID, a);  // a manifest name track -_-
  //                   }
                }
                catch(Exception e){
                    System.out.println(e+" Exception!" + repo_path);
                }
            }
        }
    }

    public static String copyFile(String file_name, String target) throws IOException{

        FileReader file = new FileReader(file_name);
        BufferedReader br=new BufferedReader(file);

        String artId, file_content="";
        int i, checksum=0, counter=0;
        artId = artID(file_name);
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
