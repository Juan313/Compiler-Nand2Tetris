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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class FilesUtil {

	public static String readTextFile(String fileName) throws IOException {
		String content = new String(Files.readAllBytes(Paths.get(fileName)));
		return content;
	}
	
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
	public static List<String> cleanUpInput (String str){
		
		// remove all the comments
		str=str.replaceAll("//.*","");
//		str=str.replaceAll("\n", " ");
		str=str.replaceAll("(?s)\\/\\*[^\\/]*\\*\\/","");
//		System.out.print(str);
		String temp;
		
		String Symbol = "(\\{|\\}|\\(|\\)|\\[|\\]|\\.|\\,|\\;|\\+|\\-|\\*|\\/|\\&|\\||\\<|\\>|\\=|\\~)";
		
		StringBuilder newString = new StringBuilder();
		
//		List<String> ls = new ArrayList<String>();
		int left=0;
		while(left<str.length()){
			int right = left+1;
			while (right<=str.length()){
				temp=str.substring(left, right-1);
				String singleString = str.substring(right-1,right);
				if ((singleString.matches(Symbol))&&(!str.substring(left, left+1).matches("\""))){
					newString.append(temp);
					
					if (! temp.isEmpty()){
						newString.append("\n");}
					newString.append(singleString);
					newString.append("\n");
//					if (temp!="\\s+"){
//					ls.add(temp);}
//					ls.add(singleString);
					
					left=right;
					break;
				}
				else if ((singleString.matches("\\s"))&&(!str.substring(left, left+1).matches("\""))){
					newString.append(temp);
					if (! temp.isEmpty()){
					newString.append("\n");}
//					if (temp!="\\s+"){
//					ls.add(temp.trim());}
					left=right;
					break;
				}
				else if ((str.substring(left, left+1).matches("\""))&&(str.substring(right, right+1).matches("\""))&&(left!=right)){
					newString.append(str.substring(left, right+1));
					newString.append("\n");
//					ls.add(str.substring(left, right+1));
					left=right+1;
					break;
				}
				else {
					right++;
				}
				
				
			}
			
			
		}
		List<String> myList = new ArrayList<String>(Arrays.asList(newString.toString().split("\n")));
	//	System.out.print(myList);
		return myList;

	}

	

}
