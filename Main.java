import org.json.simple.parser.ParseException;

import java.io.*;

/*
 * class name: createManifest
 * Authors: Derek Baker
 *              contact: derekjohnbaker23@gmail.com
 *              
 *          Rachayita Giri
 *              contact: rachayitagiri@gmail.com
 *              
 *          Saloni Buddhadeo
 *              contact: salonibuddhadeo@gmail.com
 * 
 * This will be the entryway into the non-GUI command line. The user will input their commands here. "exit" will close the program.
 * 
 * Working commands:
 *  create: copies directory from String "source" to String "destination"  ex: create sourceRepo destinationRepo
 *  
 *  label: 
 *      syntax: label <repo name> <manifest name> <labels separated by commas>
 *      creates a json label file with the keys of labels and value of the manifest
 *      no frills: compress ALL spaces the user inputs for labels
 *  
 *  exit: exits the program
 * 
 */

public class Main {        
    
    static Label newLabel = new Label();
    
  /*
   * main: takes user input and separates them by spaces.
   *    The keyword "exit" will stop the first while loop and go into parsing
   *    the input by spaces. Each string token will be read and matched in a switch.
   */
    public static void main( String[] args ) throws IOException, ParseException{
        
        InputParser sc = InputParser.getParser();
        Create_Repo createRepo = new Create_Repo();
        Check_In checkIn = new Check_In();
        Check_Out checkOut = new Check_Out();   
        Merge merger = new Merge();
        
        boolean isExit = false;
        
      // take all user input
        while( !isExit ) {
            String token = sc.GetToken();      
        
          //parse input and read each string  token                    
            switch( token ) {            
            
          // keyword: create - create a repository of the name in next token
            case "create":
                createRepo.RunOperation();
                break;
                
          // check in command
            case "cin":
                checkIn.RunOperation();
            	break;
            	
          // check out command
            case "cout":
                checkOut.RunOperation();
            	break;
            
          // label a manifest file
            case "label":                                       
                newLabel.CreateLabel();
            	break;
            	
          // do a merge
            case "merge":
                merger.RunOperation();
                break;
                
            case "cin":
            	String src = sc.next();
                String dest = sc.next();
                System.out.println( "Merge from: " + src );
                System.out.println( "Merge changes to: " + dest );
                String[] arr = new String[ 2 ];
                arr[0] = src;
                arr[1] = dest;
                CreateMain.Checkout( arr );
            	break;
            	
            case "cout":
            	String src = sc.next();
            	String mani = sc.next();
                String dest = sc.next();
                System.out.println( "Copy repo from: " + src );
                System.out.println( "Manifest version: " + mani );
                System.out.println( "Copy repo into: " + dest );
                String[] arr = new String[ 2 ];
                arr[0] = src;
                arr[1] = mani;
                arr[2] = dest;
                CreateMain.Checkout( arr );
            	break;
            	
            case "label":
            	break;
                
          // exit the command line
            case "exit":
                isExit = true;
                break;
                
          // default: bad input - remove the token and loop through again for a match
            default:
                System.out.println( "Bad input: " + token );                               
                break;
            }            
        }                
        sc.CloseInput();
        System.out.println( "Exited successfully." );
    }
}
