import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileReader;
 
import org.json.simple.JSONObject; 
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
 

/*
 * class name: Label
 * 
 * @Author Derek Baker contact: derekjohnbaker23@gmail.com
 *              
 * @Author Rachayita Giri contact: rachayitagiri@gmail.com
 *              
 * @Author Saloni Buddhadeo contact: salonibuddhadeo@gmail.com
 * 
 * This class creates the labelJSON file for specified manifest files that goes in the root folder of the repo
 * 
 * 
 */
public class Label {        
    
    JSONMaker jsonClass = new JSONMaker();
    
    /*  TrimLength: if the length >30 characters, only the first 30 will be returned
     *      input: the string to be trimmed of white space an then checked for length
     */
    public String TrimLength( String input ) {
        // remove all white space        
        input = input.replaceAll( "\\s+","" );
        
        // return string = 29 characters (0 index), only used if counter>29
        String ret = "";
        
        int counter = 0;
        for( int i = 0; i < input.length(); i++ ) {
            if( Character.isLetter( input.charAt(i) ) ) {
                counter++;
            }
        }
        
        if( counter > 29 ) {
            for( int i = 0; i < counter; i++ ) {
                ret = ret+input.charAt( i );
            }                        
        } else {
            ret = input;
        }
        
        System.out.println( "trimmed and <30 characters label: " + ret );                        
        return ret;
    }
    
    
    /*  ParseInput: adds the label keys to the pairings of the manifest file indicated.
     *      mani: the name of the manifest file that the labels will reference
     *      userLables: holds up four labels (or possibly more in the future) that will act as keys for the value of the manifest 
     */    
    public JSONObject ParseInput( String mani, String userLabel ) throws IOException {                
        
        // the split strings, accepts only the first 4
        String[] labels = userLabel.split( "," );
        
        //amount of labels, only useful if > 4
        int len = (labels.length > 4)? 4 : labels.length;
        
        String[] trimmedLabels = new String[ len ];
        String[] keyValues = new String[ len ];                
        
        for( int i = 0; i < len; i++ ) {
            trimmedLabels[ i ] = TrimLength( labels[ i ] );
            keyValues[ i ] = mani;
        }
        
        return jsonClass.CreateJSONObject( trimmedLabels, keyValues ); 
    }
    
	
	/* 
	 * CreateLabel: creates a labal_mani.json file with key-value pairings for label-mani
     *      mani: the name of the manifest file that the labels will reference
     *      userLables: holds up four labels (or possibly more in the future) that will act as keys for the value of the manifest
	 */    
	public void CreateLabel( String repo, String mani, String userLabel ) throws IOException {		
	    	    
	    JSONObject jsonLabels = ParseInput( mani, userLabel );	    
	    
	    /*
	     *  TO DO: 
	     *     Update path if needed, not sure of source path or future path
	     * 
	     */
	    String path = repo+"/labels.json";
	    
	    jsonClass.WriteToFile( path, jsonLabels );
	}
	
	/* 
     * CheckForLabel: checks the input to see if it matches a label, if a match, return the manifest name
     *      repo: the repository where the labels reference the manifest files
     *      input: the label to be checked against
     */    
	public String CheckForLabel( String repo, String input ) throws FileNotFoundException, IOException, ParseException {
	    
	    String path = repo+"/labels.json";
	    
	    JSONObject parsedJSON = jsonClass.ReadFile( path );
	    
	    System.out.println( "PARSED LABELS: " + parsedJSON.toJSONString() );
	    
	    String ret = input;
	    
	    if( parsedJSON.containsKey( input ) ) {
	        ret = (String) parsedJSON.get( input );
	    }
	    
	    /*for( Object keys : parsedJSON ) {
	        JSONObject label = (JSONObject) keys;
	        if( label.toString()==input ) {
	            ret = (String) label.get( input );
	            break;
	        }        	       
	    } */	    
	    return ret;
	}

}
