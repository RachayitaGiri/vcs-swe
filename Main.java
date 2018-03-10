import java.util.Scanner;
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
 *  exit: exits the program
 * 
 */

public class Main {
    
    static Scanner sc = new Scanner(System.in); // takes user input    
    
  /*
   * main: takes user input and separates them by spaces.
   *    The keyword "exit" will stop the first while loop and go into parsing
   *    the input by spaces. Each string token will be read and matched in a switch.
   */
    public static void main( String[] args ) throws IOException{
        
        boolean isExit = false;
      // take all user input
        while( !isExit ) {
            String token = sc.next();                  
        
          //parse input and read each string  token                    
            switch( token ) {            
            
            //keyword: create - create a repository of the name in next token
            case "create":
                String src = sc.next();
                String dest = sc.next();
                System.out.println( "Create a new repository from source: " + src );
                System.out.println( "Copy repository into: " + dest );
                String[] arr = new String[ 2 ];
                arr[0] = src;
                arr[1] = dest;
                CreateMain.CreateSource( arr );
                
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
        sc.close();
    }
}
