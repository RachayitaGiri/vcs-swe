import java.io.IOException;

/*
 * class name: Create_Repo
 * Authors: Derek Baker
 *              contact: derekjohnbaker23@gmail.com
 *              
 *          Rachayita Giri
 *              contact: rachayitagiri@gmail.com
 *              
 *          Saloni Buddhadeo
 *              contact: salonibuddhadeo@gmail.com
 * 
 * Implements the RunOperation to create a new repo at the specified location
 * 
 *  sc: uses the singleton for input parsing
 * 
 */
public class Create_Repo implements Component_Input {
    
    private InputParser sc = InputParser.getParser();    
    
    /*
     * RunOperation: implented so that a new repository will be created at dest with src 
     *      syntax: create 'source' 'destination'
     */
    @Override
    public void RunOperation() {
        String src;
        String dest;
        String[] arr;
        
        src = sc.GetToken();
        dest = sc.GetToken();
        System.out.println( "Create a new repository from source: " + src );
        System.out.println( "Copy repository into: " + dest );
        arr = new String[ 2 ];
        arr[0] = src;
        arr[1] = dest;
        try {
            CreateMain.CreateSource( arr );
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
