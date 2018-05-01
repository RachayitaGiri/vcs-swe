import java.util.Scanner;

/*
 * class name: InputParser
 * Authors: Derek Baker
 *              contact: derekjohnbaker23@gmail.com
 *              
 *          Rachayita Giri
 *              contact: rachayitagiri@gmail.com
 *              
 *          Saloni Buddhadeo
 *              contact: salonibuddhadeo@gmail.com
 * 
 * The singleton for user input since we don't want more than 1 instance gathering input.
 * 
 */

public class InputParser {
    
    private static InputParser singleInstance = null;
    private static Scanner sc = new Scanner(System.in); // takes user input    
    
    /*
     * GetToken: returns the next token in input
     */
    public String GetToken() {        
        String token = sc.next();        
        return token;
    }
    
    /*
     * GetLine: returns the entire line until return carriage
     */
    public String GetLine() {
        String line = sc.nextLine();
        return line;
    }
    
    /*
     * CloseInput: close the input
     */
    public void CloseInput() {
        sc.close();
    }
    
    /*
     * getParser: return the singleton parser, don't want more than one instance of the input parser
     */
    public static InputParser getParser() {
        if( singleInstance == null ) {
            singleInstance = new InputParser();
        }
        return singleInstance;
    }
    
}
