package vcs;
import java.io.*;

/*
 * class name: Calc_ArtId
 * Authors: Derek Baker
 *              contact: derekjohnbaker23@gmail.com
 *              
 *          Rachayita Giri
 *              contact: rachayitagiri@gmail.com
 *              
 *          Saloni Buddhadeo
 *              contact: salonibuddhadeo@gmail.com
 * 
 * Calculates the artifact ID for the given file
 * Returns the artifact file name
 * 
 * Contains the following functions:
 * -> artID() - to calculate the artifact id and return the artifact filename 
 */

public class Calc_ArtId {

    //Function to calculate the artifact ID and return artifact filename
    //Receives one argument - filename
	public static String artID(String file_name) throws IOException {

		String art_file_name;
        int i, checksum=0, counter=0;
        char content = '\0';
        int weights[] = {1,7,11,17};

		//Open the buffered reader for the passed file
        FileReader file = new FileReader(file_name);
        File afile = new File(file_name);
        BufferedReader br=new BufferedReader(file);

        /*
         * Read the contents of the file character by character
		 * and calculate the checksum as per the algorithm in guidelines
		 */
        while((i=br.read())!=-1){
            content = (char)i;                                    
            //contentchecksum = ascii code of character * corresponding weight
            checksum += (int) (char) i * weights[counter%4];   
            //increment counter for weights 
            counter++;                                         
        }
        checksum -= (int) content * weights[(counter-1)%4];
        checksum %= (Math.pow(2,31) - 1);

        //New file name for the artifact 
        art_file_name = checksum+"-L"+afile.length()+".txt";

        //Close the buffered reader and the file reader
        br.close();
        file.close();

        //Return the artifact name
        return art_file_name;
    }
}