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
	public static String readTextFile(String fileName) throws IOException {
		String content = new String(Files.readAllBytes(Paths.get(fileName)));
		return content;
	}

	public static List<String> readTextFileByLines(String fileName)
			throws IOException {
		List<String> lines = Files.readAllLines(Paths.get(fileName));
		return lines;
	}

//	public static void writeToTextFile(String fileName, String content)
//			throws IOException {
//		Files.write(Paths.get(fileName), content.getBytes(),
//				StandardOpenOption.CREATE);
//	}
	
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
			
			String modCurrent=currentStr.replaceAll("\\s", "");
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

	
	public static void main(String[] args) throws IOException {
		String input = FilesUtil.readTextFile(args[0]);
		int index = args[0].lastIndexOf(".asm");
		String fileNameWOExtn = args[0].substring(0, index);
		String NewfileNameWExtn = fileNameWOExtn+".hack";
//		System.out.println(input);
		FilesUtil.writeToTextFile(NewfileNameWExtn, "abc\n");
		FilesUtil.writeToTextFile(NewfileNameWExtn, " def\n");
		FilesUtil.writeToTextFile(NewfileNameWExtn, "111112\n");
		System.out.println(NewfileNameWExtn);
//		List<String> ls = FilesUtil.readTextFileByLines(NewfileNameWExtn);
//		List<String> ls = FilesUtil.readTextFileByLines("try.asm");
//		compressList(ls);
//    
//
//		for (String s:ls){
//			System.out.println(s);
//		}
//		String currentStr= "A=D+M;jerero";
		

//		int size=ls.size();
//		System.out.println("size=" +size);
//		System.out.println(ls.size());
		BufferedReader br = Files.newBufferedReader(Paths.get(NewfileNameWExtn));
		
//		BufferedReader br = Files.newBufferedReader(Paths.get(NewfileNameWExtn));
//		String line = br.readLine();
//		System.out.println(line);
//		line = br.readLine();
//		System.out.println(line);
//		line = br.readLine();
//		System.out.println(line);
//		System.out.println(FilesUtil.readTextFile("copy.txt"));

//		FilesUtil.readTextFileByLines(fileNameWOExtn+".asm");
	//	Path path = Paths.get("file.txt");
	}
}
