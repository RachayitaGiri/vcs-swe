import java.io.*;
import java.util.*;
import java.text.*;
import org.json.simple.JSONArray; 
import org.json.simple.JSONObject; 
import org.json.simple.parser.JSONParser;
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
 * This file will create a text file known as the Manifest file in the first directory with name mani_create.txt. 
 * The file will contain the command and the date/time of the creation of each subdirector/file contained within.
 * 
 */


public class CreateManifest {
    public static void Manifest(String repo_path, String artID, String target) throws IOException {
        try {
            Date date = new Date();
            SimpleDateFormat ft = new SimpleDateFormat ("yyyy:MM:dd hh:mm:ss");
            /*
                To record:
                    path name
                    artifact ID
                    date and time
            */
            String timestamp;
            String b = target;
            JSONObject jsonMani = new JSONObject();
            jsonMani.put("path", repo_path);
            jsonMani.put("artID", artID);
            jsonMani.put("Date", ft.format(date));

            Writer  out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(b, true), "UTF-8"));
            out.write(jsonMani.toJSONString().replace("\\",""));
//          out.write("\n" + repo_path + "; " + artID + "; " + ft.format(date) + ";");
            out.close();
        }   
        catch(Exception e) {
            System.out.println(e);
        }   
    }
        public static int maniFileNo(String dirPath, String cmd) throws IOException{
        int i = 1;
        List<String> al = new ArrayList<String>();
        String maniFile="mani_";
        if(cmd == "cin"){
            maniFile = "mani_cin_";
        }
        else if(cmd == "cout"){
            maniFile = "mani_cout_";
        }

        File f = new File(dirPath);
        ArrayList<String> names = new ArrayList<String>(Arrays.asList(f.list()));
        for(String maniCmd : names)
            if(maniCmd.contains(maniFile))
                al.add(maniCmd);
        int lastFile = getLatestFilefromDir(al);
//        String lastFile = getLatestFilefromDir(dirPath).getName();
//        String[] parts = lastFile.split("_");
//        i = Integer.parseInt(parts[2]);
        
        return lastFile;
    }

    public static int getLatestFilefromDir(List dirPath){
        
        int[] myArray = new int[dirPath.size()];
        int counter=0;
        for(Object file : dirPath){
            String fileName = file.toString();
            String parts[] = fileName.split("_");
            myArray[counter] = Integer.parseInt(parts[parts.length-2]);
            System.out.println(myArray[counter]+"myArray --");
            counter ++;
        }
        Arrays.sort(myArray);        
        int max = myArray[myArray.length - 1];

        return max;
    }
}
