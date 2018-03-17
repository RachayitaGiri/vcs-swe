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
 * This class will be the singleton for JSON objects. This class will create objects for manipulation, read, and write files.
 * 
 * 
 */
public class JSONMaker {
    
    /* 
     * CreateJSONObject: returns a JSON object with designated keys and values for manipulation; ie: testing key pairs
     *      keys: array of key names
     *      values: array of key values
     *  
     *  ** note: keys and values should be the same length, otherwise there could be a null value or missing key **
     */
    @SuppressWarnings("unchecked")
    public JSONObject CreateJSONObject( String[] keys, String[] values ) throws IOException {
        // the return object
        JSONObject jsons = new JSONObject();
                                        
        for( int i = 0; i < keys.length; i++) {
            jsons.put( keys[ i ], values[ i ] );
        }
        
        System.out.println( "json object: " + jsons );
        
        return jsons;
    }
    
    /*
     *  ReadFile: returns a JSON object from a file
     *      path: the name/location of the JSON file
     */
    public JSONObject ReadFile( String path ) throws FileNotFoundException, IOException, ParseException {
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse( new FileReader( path ) );
    }
    
    /*
     * WriteToFile: writes to a .json file the key-value pairs stored in jsonHash
     *  path: writes to path which contains the file name as well
     *  jsonHash: key-value pairs which will be written to said file
     */
    public void WriteToFile( String path, JSONObject jsonHash ) throws IOException {
        try( FileWriter file = new FileWriter( path ) ) {           
            file.write( jsonHash.toJSONString() );
            System.out.println("Successfully Copied JSON Object to File...");
            System.out.println("\nJSON object:" + jsonHash );
        }
    }
}
