import java.io.*;

/*
 * class name: Run_Merge
 * Authors: Derek Baker
 *              contact: derekjohnbaker23@gmail.com
 *              
 *          Rachayita Giri
 *              contact: rachayitagiri@gmail.com
 *              
 *          Saloni Buddhadeo
 *              contact: salonibuddhadeo@gmail.com
 * 
 * Does all the stuff necessary to create a merge
 * 
 *  
 * 
 */

public class Run_Merge {
    static String mani_path;
    //Function to implement the merge operation
    public static void merge( String[] arr ) throws IOException {

    	int i = 1;
    	String cmd = arr[0];
    	//Store the name of the repository
    	String repo_name = arr[1];
    	//Store the names of the versions to be merged
    	String mani_versionR = arr[2];
    	String mani_versionT = arr[3];

 		//Call the grandma() function to find the common ancestor
    	int grandmaId = grandma(repo_name, mani_versionR, mani_versionT);
        
        //Get path to the grandma based on its ID
    	String grandma_path = grandmaPath(repo_name, grandmaId);
        System.out.println(grandmaId+" Grandma path = " + grandma_path);

        //Get the folder names to be merged 
        String versionR = getFolderName(repo_name, mani_versionR);
        String versionT = getFolderName(repo_name, mani_versionT);
        System.out.println("vr name, vt name: "+ versionR + ", " + versionT);
        
    	//Perform a checkin for both versions into the grandma here
    	File[] filesR = new File(versionR + "/").listFiles();
    	File[] filesT = new File(versionT + "/").listFiles();

        //Get the original root folder's name under repo root
        File[] repo_files = new File(repo_name).listFiles();
        String source_name="";
        for(File f : repo_files){
            if(f.isDirectory())
                source_name = f.getName();
        }

        //Iterate over the hierarchies of R and T to copy into Grandma
        showFilesMerge(filesR, versionR, repo_name+"/"+source_name+"/", 0);
        showFilesMerge(filesT, versionT, repo_name+"/"+source_name+"/", 1);

    }
    
    //Function to iterate over the hierarchy of a given folder
    public static void showFilesMerge(File[] files, String src_path, String target, int flag) {
    	
    	String parent, file_name, dir_name, artID, jon_snow;

        for (File file : files) {
            jon_snow = "/";

            if (file.isDirectory()) {
                dir_name = file.getName();
                System.out.println("Dir: " + dir_name);

                jon_snow += dir_name;
                System.out.println(jon_snow + "  ");

                String newFolder = target + jon_snow;
                System.out.println(newFolder);
                if(!new File(newFolder).exists()) {
                        new File(newFolder).mkdirs();
                        System.out.println("Created new folder: " + newFolder);
                }        
                showFilesMerge(file.listFiles(), src_path + jon_snow, target + jon_snow, flag);
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
                    artID = copyFile(src_path + jon_snow, target + jon_snow, flag);
//                    CreateManifest.Manifest(src_path + jon_snow, artID, mani_path);
                }
                catch(Exception e){
                    e.printStackTrace();
                    System.out.println(e +" Exception!"+ mani_path +" "+ target+ " " + jon_snow);
                }
            }
        }
    }

    public static String copyFile(String file_name, String target, int flag) throws IOException{
        //Read the file in the source folder
        FileReader file = new FileReader(file_name);
        BufferedReader br = new BufferedReader(file);

        //Invoke the calculation of the artifact id for the given file
        String artId, file_content="";
        int i, checksum=0, counter=0;
        
        //Calculate artifact file name
        artId = Calc_ArtId.artID(file_name);

        String fileDest = target;
        File testfile = new File(fileDest, artId);
        
        //If artifact already exists in grandma, check teh flags
        if(testfile.exists()){
            String[] art_arr = artId.split("\\.");
            artId = art_arr[0] + "_MG." + art_arr[1];
            File granda = new File(fileDest, artId);
            testfile.renameTo(granda);
            
        	/* Flag = 0 --> versionR
	         * Flag = 1 --> versionT
        	 * Add _MR and _MT to the versionR and versionT files respectively.
			 */
            if (flag == 0)
        	artId = art_arr[0] + "_MR." + art_arr[1];
            else if (flag == 1)
        	artId = art_arr[0] + "_MT." + art_arr[1];
            
        }
        
        //Open the buffered writer for the artifact
        BufferedWriter bw = new BufferedWriter(new FileWriter(fileDest+"/"+artId));
        
        //Copy the contents from the source file to target artifact file 
        while((i=br.read())!=-1){
            file_content += (char)i;
        }
        bw.write(file_content);

		//Close the buffered reader, writer and file
        br.close();
        bw.close();
        file.close();

        //Return the artifact ID to the showFilesMerge() function after copying
        return artId;
    }

    //Function to return the id of the common ancestor
    public static int grandma(String repo_name, String versionR, String versionT) throws FileNotFoundException, IOException {

    	int i, grandmaId = 0;
    	int[] ancestors_T = new int[15];

    	//Get the IDs of both the versions as is in .metadata.csv file
    	int versionR_id = getVersionId(repo_name, versionR);
    	int versionT_id = getVersionId(repo_name, versionT);

    	//Store the parent IDs of both versions
    	int parentR_id = getParentId(repo_name, versionR_id);
    	int parentT_id = getParentId(repo_name, versionT_id);

    	//Iterate over all ancestors of T and store them in an array
    	i = 0;
    	while (parentT_id != 0) {
    		ancestors_T[i] = parentT_id; 
    		i++;
    		parentT_id = getParentId(repo_name, parentT_id);
    	}

    	//Compare all ancestors of T with those of R; break if they're equal
    	for (int j = 0; j < i; j++ ) {
    		if (ancestors_T[j] == parentR_id) {
    		   grandmaId = parentR_id;
    		   break;		
    		}    	
    		parentR_id = getParentId(repo_name, parentR_id);
    	}
    	return grandmaId;
    }

    //Function to return the path to the grandma
    public static String grandmaPath(String repo_name, int grandmaId) throws IOException {
    	//Initialization of metafile related variables
    	String line = "", csvSplit = ",", grandma_path="";
    	String[] csvContent = new String[0];
    	//Read the .metadata.csv file
    	FileReader metafile = new FileReader(repo_name+"/.metadata.csv");
        BufferedReader br = new BufferedReader(metafile);

        //Iterate over each row in the meta file
        while ((line = br.readLine()) != null) {
            csvContent = line.split(csvSplit);
            if((Integer.parseInt(csvContent[0])) == grandmaId) {
                grandma_path = csvContent[1];
                break;
            }
        }

        //Return the path to the common ancestor
        return grandma_path;
    }

    //Function to return the ID of a given version
    public static int getVersionId(String repo_name, String version_name) throws IOException {

    	//Initialization of metafile related variables
    	String line = "", csvSplit = ",";
    	String[] csvContent = new String[0];
        int index = 0, versionId = 0;
        
    	//Read the .metadata.csv file
    	FileReader metafile = new FileReader(repo_name+"/.metadata.csv");
        BufferedReader br = new BufferedReader(metafile);

        //Iterate over each row in the meta file
        while ((line = br.readLine()) != null) {
            csvContent = line.split(csvSplit);
            if(csvContent[1].contains(version_name)) {
                versionId = index+1;
                break;
            }
            index++;
        }

        //Return the version ID of the version name
        return versionId;
    }

    //Function to return the parent ID of a given version
    public static int getParentId(String repo_name, int version_id) throws IOException {

    	//Initialization of metafile related variables
    	String line = "", csvSplit = ",";
    	String[] csvContent = new String[0];
    	int parentId = 0;

    	//Read the .metadata.csv file
    	FileReader metafile = new FileReader(repo_name+"/.metadata.csv");
        BufferedReader br = new BufferedReader(metafile);

        //Iterate over each row in the meta file
        while ((line = br.readLine()) != null) {
            csvContent = line.split(csvSplit);
            if((Integer.parseInt(csvContent[0])) == version_id) {
                parentId = Integer.parseInt(csvContent[4]);
                break;
            }
        }

        //Return the version ID of the version name
        return parentId;
    }

    //Function to get the folder name by manifest file name
    public static String getFolderName(String repo_name, String mani) throws IOException{

        String line = "", csvSplit = ",", folder_name="";
        String[] csvContent = new String[0];
        String[] cmd_arr = new String[0];

        //Read the .metadata.csv file
        FileReader metafile = new FileReader(repo_name+"/.metadata.csv");
        BufferedReader br = new BufferedReader(metafile);

        //Iterate over the csv file to get the row corresponding to id
        while ((line = br.readLine()) != null) {
            csvContent = line.split(csvSplit);
            if(csvContent[1].contains(mani)) {
                cmd_arr = csvContent[2].split(" ");
                if (cmd_arr[0].equals("cin"))
                    return cmd_arr[2];
                    // folder_name = cmd_arr[2];
                if (cmd_arr[0].equals("cout"))
                    return cmd_arr[3];
                    // folder_name = cmd_arr[3];
                break;
            }
        }
        return folder_name;
    }
}
