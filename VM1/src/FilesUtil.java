import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class FilesUtil {


	public static List<String> readTextFileByLines(String fileName)
			throws IOException {
		List<String> lines = Files.readAllLines(Paths.get(fileName));
		return lines;
	}

	
	public static void writeToTextFile (String fileName, String Content) throws IOException{
		File file = new File (fileName);
		if (!file.exists()){
			file.createNewFile();
		}
		FileWriter fw = new FileWriter (file.getAbsoluteFile(),true);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(Content+"\n");
		bw.close();
		
	}
	
	// remove white space and comments
	public static void compressList (List<String> ls){
		ListIterator<String> itr = ls.listIterator();
		while (itr.hasNext()){
			String currentStr = itr.next();
			
			String modCurrent=currentStr.replaceAll("\\s", " ");
			itr.set(modCurrent);
			if ((modCurrent.length() == 0)||(modCurrent.startsWith("//"))) {
				itr.remove();
			} 
			
			else if (modCurrent.contains("//")) {
				
				
				int ind = modCurrent.indexOf("//");

			    String s2=modCurrent.substring(0, ind);
				itr.set(s2);
			}
		}
	}

	

}
