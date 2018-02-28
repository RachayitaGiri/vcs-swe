/* Run the program as shown below:
    java CreateMain <src_file> <target_repo>
*/

/* This file takes as input the name of the source file and
  target file in the command line*/

import java.io.*;

public class CreateMain {    
    
    public static void CreateSource(String[] args) throws IOException {
        String target;
        File[] files = new File(args[0]).listFiles();                   //list the files/dirs in the first argument
        System.out.println("Creating repo...");
        new File(args[1]).mkdir();
        new File(args[1]+"/"+args[0]).mkdir();

        //Create manifest file
        String a = args[1]+"/mani_create.txt";
		BufferedWriter manibw = new BufferedWriter(new FileWriter(a));	//------------APPENDING ISSUES IN FILE
		manibw.write("CREATE " + args[0] + args[1]);
		manibw.close();

        System.out.println("Repo created successfully.");
        target = args[1] + "/";
        showFiles(files, args[0], target);                      //args[1] + "/" + 
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
                new File(newFolder).mkdirs();                             //ONLY CREATES A DIR IN THE REPO
                showFiles(file.listFiles(), repo_path, target);                 //Calls same method again
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
        File afile = new File(file_name);
        BufferedReader br=new BufferedReader(file);

        String repo_file, file_content="";
        int i, checksum=0, counter=0;
        repo_file = artID(file_name);
        String fileDest = target+file_name;
        //new File(fileDest).mkdir();
        BufferedWriter bw=new BufferedWriter(new FileWriter(fileDest+"/"+repo_file));

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
        // double file_size = file.length();

        while((i=br.read())!=-1){
            cha = (char)i;                                      //Converting string to char
            checksum += (int) (char) i * weights[counter%4];    //checksum calculation - ascii of character * corresponding weight
            counter++;                                          //counter for weights
        }
        checksum -= (int) cha * weights[(counter-1)%4];
        checksum %= (Math.pow(2,31) - 1);
        art_file_name = checksum+"-L"+afile.length()+".txt";    // artifactID - new file name
        
        //System.out.println(art_file_name);                    //Display artifact file name
        // afile.renameTo(art_file_name);

        br.close();
        file.close();

        return art_file_name;
    }
}