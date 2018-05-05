import java.io.IOException;

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
 * Implements the RunOperation to run the merge command
 * 
 *  sc: uses the singleton for input parsing
 * 
 */

public class Merge implements Component_Input {

    private InputParser sc = InputParser.getParser();
    
    /*
	 * RunOperation: implemented so that the merge command can be carried out
	 *		syntax: merge 'repo_name' 'version_name_1' 'version_name_2'
     */
    @Override
    public void RunOperation() {
        
        String versionR;
        String versionT;
        
        //Read input version names 
        versionR = sc.GetToken();
        versionT = sc.GetToken();
        
        //Print version names
        System.out.println( "Merge folders " + versionR + "and " + versionT);
        System.out.println( "Merge to repo: " + repo_name); 

        //Create array to hold command line arguments
        arr = new String[ 4 ];
        arr[0] = "merge";
        arr[1] = repo_name;
        arr[2] = versionR;
        arr[3] = versionT;

        //Call the merge function in Run_Merge.java
        try{
        	Run_Merge.doMerge( arr );
        } catch (IOException e) {
        	e.printStackTrace();
        }
        

    }

}
