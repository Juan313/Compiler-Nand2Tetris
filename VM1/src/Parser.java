import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ListIterator;


public class Parser {
	public static final int C_ARITHMETIC = 1;
	public static final int C_PUSH = 2;
	public static final int C_POP = 3;
	public static final int C_LABEL = 4;
	public static final int C_GOTO = 5;
	public static final int C_IF = 6;
	public static final int C_FUNCTION = 7;
	public static final int C_RETURN = 8;
	public static final int C_CALL = 9;
	
	List<String> ls;
	ListIterator<String> itr;

	public Parser(String filename) {
		try {
			ls = FilesUtil.readTextFileByLines(filename);
			FilesUtil.compressList(ls);
			itr = ls.listIterator();
		
//			int index = filename.lastIndexOf(".vm");
//			String fileNameWOExtn = filename.substring(0, index);
//			String NewfileNameWExtn = fileNameWOExtn+".asm";
//			File file = new File (NewfileNameWExtn);
//			if (file.exists()) file.delete();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	String currentStr;

	Boolean hasMoreCommands() {
		return itr.hasNext();
	}

	void advance() {
		if (hasMoreCommands())
			currentStr = itr.next();
	}
	
	int commandType() {
		
		if (currentStr.startsWith("add")){
			return C_ARITHMETIC;
		} 
		else if (currentStr.startsWith("sub")){
			return C_ARITHMETIC;
		} 		
		else if (currentStr.startsWith("neg")){
			return C_ARITHMETIC;
		} 
		else if (currentStr.startsWith("eq")){
			return C_ARITHMETIC;
		} 
		else if (currentStr.startsWith("gt")){
			return C_ARITHMETIC;
		} 
		else if (currentStr.startsWith("lt")){
			return C_ARITHMETIC;
		} 
		else if (currentStr.startsWith("and")){
			return C_ARITHMETIC;
		} 
		else if (currentStr.startsWith("or")){
			return C_ARITHMETIC;
		} 
		else if (currentStr.startsWith("not")){
			return C_ARITHMETIC;
		} 
		else if (currentStr.startsWith("push")) {
			return C_PUSH;
		} else if (currentStr.startsWith("pop")) {
			return C_POP;
		} 
		else if (currentStr.startsWith("label")) {
			return C_LABEL;}
		else if (currentStr.startsWith("goto")) {
			return C_GOTO;}
		else if (currentStr.startsWith("if-goto")) {
			return C_IF;}
		else if (currentStr.startsWith("function")) {
			return C_FUNCTION;}	
		else if (currentStr.startsWith("return")) {
			return C_RETURN;}
		else if (currentStr.startsWith("call")) {
			return C_CALL;}
		else {
			return 0;
		}
		
	}
	String arg1() {
		if (commandType()==C_ARITHMETIC){
			
			String modCurrent=currentStr.replaceAll("\\s", "");
			
			return modCurrent;
		} else if ((commandType()==C_PUSH)||(commandType()==C_POP)
				||(commandType()==C_LABEL)||(commandType()==C_GOTO)
				||(commandType()==C_IF)||(commandType()==C_FUNCTION)
				||(commandType()==C_CALL)){
			String[] parts=currentStr.split(" ");
			return parts[1];
		} 
		else {
			return null;
		}
		
	}
	int arg2() {
		if ((commandType()==C_PUSH)||(commandType()==C_POP)
				||(commandType()==C_FUNCTION)||(commandType()==C_CALL)){
			String[] parts=currentStr.split(" ");
			return Integer.parseInt(parts[2]);
		} 
		else {
			return 0;
		}
		
	}
	
	public static void main(String[] args) throws IOException {
		String filename = args[0];
		int index = args[0].lastIndexOf(".vm");
		String fileNameWOExtn = args[0].substring(0, index);
		String NewfileNameWExtn = fileNameWOExtn + ".asm";


		Parser ps = new Parser(filename);


	}
}
