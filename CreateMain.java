import java.io.*;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

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
        UpdateMetadata.metadata(args[1]+"/", mani_path, args, args[2]+"/.metadata.csv");
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

    //Function to checkin files
    //Main.java passes two arguments - check in folder, target repo folder    
    public static void Checkin(String[] args) throws IOException{
       File[] filesinrepo = new File(args[1] + "/").listFiles();
       String source_name = "";

       for(File f : filesinrepo){
            if(f.isDirectory())
                source_name = f.getName();
       }
       String src = args[2] + "/";                       //merge from path enterd by the user - Wall

       String target = args[1]+"/"+source_name;                  //merge to path, entered by the user - KL
       String cmd = "cin";       
       int i=1;

        File[] files = new File(src).listFiles();                   //list the files/dirs in the first argument i.e in the existing repo

        mani_path = args[1]+"/mani_cin_1_.json";
        if(new File(mani_path).exists()){
            i = CreateManifest.maniFileNo(args[1], cmd) + 1;
            mani_path = args[1]+"/mani_cin_"+(i)+"_.json";
        }

        BufferedWriter manibw = new BufferedWriter(new FileWriter(mani_path));      // Create manifest file

        // invoke metadata function to update the metadata file
        UpdateMetadata.metadata(args[1]+"/", mani_path, args, args[1]+"/.metadata.csv");

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
                        CreateManifest.Manifest(ned_stark, artID, mani_path);
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
        artId = Calc_ArtId.artID(file_name);

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
                        CreateManifest.Manifest(src_path + jon_snow, artID, mani_path);
//                     }
                }
                catch(Exception e){
                    System.out.println(e+" Exception!"+ mani_path +" "+ target+ " " + jon_snow);
                }
            }
        }
    }

}   