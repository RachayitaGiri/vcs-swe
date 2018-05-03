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
    
    static Scanner sc = new Scanner(System.in); // takes user input
    static Label newLabel = new Label();  
    
  /*
   * main: takes user input and separates them by spaces.
   *    The keyword "exit" will stop the first while loop and go into parsing
   *    the input by spaces. Each string token will be read and matched in a switch.
   */
    public static void main( String[] args ) throws IOException, ParseException{
        
        String src;
        String dest;
        String mani;
        String label;
        String[] arr;
//        File filin = new File("cinCount.txt"), filout = new File("coutCount.txt");
        boolean isExit = false;
        
      // take all user input
        while( !isExit ) {
            String token = sc.next();                  
        
          //parse input and read each string  token                    
            switch( token ) {            
            
            //keyword: create - create a repository of the name in next token
            case "create":
                src = sc.next();
                dest = sc.next();
                System.out.println( "Create a new repository from source: " + src );
                System.out.println( "Copy repository into: " + dest );
                arr = new String[ 3 ];
                arr[0] = token;
                arr[1] = src;
                arr[2] = dest;
                CreateMain.CreateSource( arr );
                break;
                
            case "cin":
            	src = sc.next();
                dest = sc.next();
                System.out.println( "Merge from: " + src );
                System.out.println( "Merge changes to: " + dest );
                arr = new String[ 3 ];
                arr[0] = token;
                arr[1] = src;
                arr[2] = dest;
                
                CreateMain.Checkin( arr );
            	break;
            	
            case "cout":
            	src = sc.next();
            	mani = sc.next();
            	dest = sc.next();
            	
//                mani = newLabel.CheckForLabel( src, mani );

                System.out.println( "Copy repo from: " + src );
                System.out.println( "Manifest version: " + mani );

                arr = new String[ 4 ];
                arr[0] = token;
                arr[1] = src;
                arr[2] = mani;
                arr[3] = dest;
                CreateMain.Checkout( arr );
                System.out.println( "Copy repo into: " + dest );
            	break;

            case "label":
                src = sc.next();
                mani = sc.next();
                String temp = sc.nextLine();
                label = temp.trim();
                newLabel.CreateLabel( src, mani, label );
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
        sc.close();
    }
}
