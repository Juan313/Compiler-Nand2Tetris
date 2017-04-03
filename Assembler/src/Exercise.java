import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.ListIterator;


public class Exercise {
	public static final int A_COMMAND = 1;
	public static final int C_COMMAND = 2;
	public static final int L_COMMAND = 3;

	public static void writeToTextFile (String fileName, String Content) throws IOException{
		File file = new File (fileName);
		if (!file.exists()){
			file.createNewFile();
		}
		FileWriter fw = new FileWriter (file.getAbsoluteFile(),true);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(Content);
		bw.close();
		
	}

	public static void main(String[] args) throws IOException {
		
		for (int i=0;i<=10;i++){
			writeToTextFile("prog.asm","erer\n");
			writeToTextFile("prog.asm","1234\n");
			System.out.println(i);
			
		}
		
		
	}
}
