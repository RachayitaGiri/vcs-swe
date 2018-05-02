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
    
    @Override
    public void RunOperation() {
        
        String folder1;
        String folder2;
        
        folder1 = sc.GetToken();
        folder2 = sc.GetToken();
                
        CreateMerge.doMerge( folder1, folder2 );

    }

}
