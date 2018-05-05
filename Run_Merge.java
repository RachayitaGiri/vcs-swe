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
    
    //Function to implement the merge operation
    public static void doMerge( String arr ) {

    	//Store the name of the repository
    	String repo_name = arr[1];

    	//Store the names of the versions to be merged
    	String versionR = arr[2];
    	String versionT = arr[3];

 		//Call the grandma() function to find the common ancestor
    	int grandmaId = grandma(repo_name, versionR, versionT);

    	//Perform a checkin for both versions into the grandma here
    	//...
    	//...
    	//...
    }

    //Function to return the id of the common ancestor
    public static int grandma(String repo_name, String versionR, String versionT) {

    	//
    	int i;
    	int[] ancestors_T;

    	//Get the IDs of both the versions as is in .metadata.csv file
    	int versionR_id = getVersionId(repo_name, versionR);
    	int versionT_id = getVersionId(repo_name, versionT);

    	//Store the parent IDs of both versions
    	int parentR_id = getParentId(versionR_id);
    	int parentT_id = getParentId(versionT_id);

    	//Iterate over all ancestors of T and store them in an array
    	i = 0;
    	while (parentT_id != 0) {
    		ancestors_T[i] = parentT_id; 
    		i++;
    		parentT_id = getParentId(parentT_id);
    	}

    	//Compare all ancestors of T with those of R; break if they're equal
    	for (int j = 0; j < i; j++ ) {
    		if (ancestors_T[i] == parentR_id) {
    		   grandmaId = parentR_Id; 
    		   break;		
    		}    	
    		parentR_id = getParentId(parentR_id);
    	}
    	return grandmaId;
    }

    //Function to return the ID of a given version
    public static int getVersionId(String repo_name, String version_name) {

    	//Initialization of metafile related variables
    	String line = "", csvSplit = ",";
    	String[] csvContent = new String[0];

    	//Read the .metadata.csv file
    	FileReader metafile = new FileReader(repo_name+"/.metadata.csv");
        BufferedReader br = new BufferedReader(metafile);

        //Iterate over each row in the meta file
        while ((line = br.readLine()) != null) {
            csvContent = line.split(csvSplit);
            if(csvContent[1].contains(version_name)) {
                versionId = index;
                break;
            }
            index++;
        }

        //Return the version ID of the version name
        return versionId;
    }

    //Function to return the parent ID of a given version
    public static int getParentId(int version_id) {

    	int parentId;

    	//Read the .metadata.csv file
    	FileReader metafile = new FileReader(repo_name+"/.metadata.csv");
        BufferedReader br = new BufferedReader(metafile);

        //Iterate over each row in the meta file
        while ((line = br.readLine()) != null) {
            csvContent = line.split(csvSplit);
            if(csvContent[0].equals(version_id)) {
                parentId = csvContent[4];
                break;
            }
            index++;
        }

        //Return the version ID of the version name
        return parentId;
    }
}
