import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;


public class CodeWriter {
	Parser ps;
	String outputFile;
	String inputFile;
	String currentFunctionName=null;
	
	public CodeWriter(String outputFileName) throws IOException{
		File file = new File (outputFileName);
		
		if (file.exists()) file.delete();
		FilesUtil.writeToTextFile(outputFileName, "// Here we go...");
		StringBuilder Content = new StringBuilder();

		Content.append("@256\n");
		Content.append("D=A\n");
		Content.append("@SP\n");
		Content.append("M=D\n");
		
		int numArgs=0;
		Content.append("@"+makeUnique(currentFunctionName,"RET")+"\n");
		Content.append("D=A\n");
		Content.append("@SP\n");
		Content.append("A=M\n");
		Content.append("M=D\n");
		Content.append("@SP\n");
		Content.append("M=M+1\n");

		Content.append("@LCL\n");
		Content.append("D=M\n");
		Content.append("@SP\n");
		Content.append("A=M\n");
		Content.append("M=D\n");
		Content.append("@SP\n");
		Content.append("M=M+1\n");
		
		Content.append("@ARG\n");
		Content.append("D=M\n");
		Content.append("@SP\n");
		Content.append("A=M\n");
		Content.append("M=D\n");
		Content.append("@SP\n");
		Content.append("M=M+1\n");
		
		Content.append("@THIS\n");
		Content.append("D=M\n");
		Content.append("@SP\n");
		Content.append("A=M\n");
		Content.append("M=D\n");
		Content.append("@SP\n");
		Content.append("M=M+1\n");		
		
		Content.append("@THAT\n");
		Content.append("D=M\n");
		Content.append("@SP\n");
		Content.append("A=M\n");
		Content.append("M=D\n");
		Content.append("@SP\n");
		Content.append("M=M+1\n");
		
		Content.append("@SP\n");
		Content.append("D=M\n");
		Content.append("@"+numArgs+"\n");
		Content.append("D=D-A\n");
		Content.append("@5\n");
		Content.append("D=D-A\n");
		Content.append("@ARG\n");
		Content.append("M=D\n");
		Content.append("@SP\n");
		Content.append("D=M\n");
		Content.append("@LCL\n");
		Content.append("M=D\n");	
		
		Content.append("@"+"Sys.init"+"\n");
		Content.append("0;JMP\n");
		
		
		
		outputFile=outputFileName;
		
		FilesUtil.writeToTextFile(outputFile, Content.toString());
	}
	

	void setFileName(String fileName) throws IOException {
		ps = new Parser(fileName);
		inputFile = fileName;

		while (ps.hasMoreCommands()) {
			ps.advance();
			if (ps.commandType() == ps.C_ARITHMETIC) {
				writeArithmetic(ps.arg1());
			}
			if (ps.commandType() == ps.C_PUSH) {
				writePushPop(ps.commandType(), ps.arg1(), ps.arg2());
			}
			if (ps.commandType() == ps.C_POP) {
				writePushPop(ps.commandType(), ps.arg1(), ps.arg2());
			}
			if (ps.commandType() == ps.C_LABEL) {
				writeLabel(ps.arg1());
			}
			if (ps.commandType() == ps.C_GOTO) {
				writeGoto(ps.arg1());
			}
			if (ps.commandType() == ps.C_IF) {
				writeIf(ps.arg1());
			}
			if (ps.commandType() == ps.C_FUNCTION) {
				currentFunctionName = ps.arg1();
				writeFunction(ps.arg1(), ps.arg2());
			}

			if (ps.commandType() == ps.C_RETURN) {

				writeReturn();
			}

			if (ps.commandType() == ps.C_CALL) {

				writeCall(ps.arg1(), ps.arg2());
			}
		}

	}
	int eqIndex=0;
	int gtIndex=0;
	int ltIndex=0;
	int rtIndex=0;
	
	void writeArithmetic (String command) throws IOException{
		if (command.equals("add")){
			StringBuilder Content = new StringBuilder(); 
			Content.append("@SP\n");
			Content.append("AM=M-1\n");
			Content.append("D=M\n");
			Content.append("@SP\n");
			Content.append("AM=M-1\n");
			Content.append("M=M+D\n");
			Content.append("@SP\n");		
			Content.append("M=M+1");
			FilesUtil.writeToTextFile(outputFile, "//"+ps.currentStr);
			FilesUtil.writeToTextFile(outputFile, Content.toString());
		}
		if (command.equals("sub")){
			StringBuilder Content = new StringBuilder(); 
			Content.append("@SP\n");
			Content.append("AM=M-1\n");
			Content.append("D=M\n");
			Content.append("@SP\n");
			Content.append("AM=M-1\n");
			Content.append("M=M-D\n");
			Content.append("@SP\n");		
			Content.append("M=M+1");
			FilesUtil.writeToTextFile(outputFile, "//"+ps.currentStr);
			FilesUtil.writeToTextFile(outputFile, Content.toString());
		}
		if (command.equals("neg")){
			StringBuilder Content = new StringBuilder(); 
			Content.append("@SP\n");
			Content.append("A=M-1\n");		
			Content.append("M=-M");
			FilesUtil.writeToTextFile(outputFile, "//"+ps.currentStr);
			FilesUtil.writeToTextFile(outputFile, Content.toString());
		}		
		if (command.equals("eq")){
			StringBuilder Content = new StringBuilder(); 
			Content.append("@SP\n");
			Content.append("AM=M-1\n");		
			Content.append("D=M\n");
			Content.append("@SP\n");
			Content.append("AM=M-1\n");
			Content.append("D=M-D\n");
			String UniqueName = findUniqueName(command,eqIndex);
			eqIndex++;
			Content.append("@"+UniqueName+"\n");
			Content.append("D;JNE\n");
			Content.append("@SP\n");
			Content.append("A=M\n");
			Content.append("M=-1\n");
			Content.append("@"+UniqueName+"_END"+"\n");
			Content.append("0;JMP\n");
			Content.append("("+UniqueName+")"+"\n");
			Content.append("@SP\n");
			Content.append("A=M\n");
			Content.append("M=0\n");
			Content.append("("+UniqueName+"_END)"+"\n");
			Content.append("@SP\n");		
			Content.append("M=M+1");
			FilesUtil.writeToTextFile(outputFile, "//"+ps.currentStr);
			FilesUtil.writeToTextFile(outputFile, Content.toString());
			
			}
		
		if (command.equals("gt")){
			StringBuilder Content = new StringBuilder(); 
			Content.append("@SP\n");
			Content.append("AM=M-1\n");		
			Content.append("D=M\n");
			Content.append("@SP\n");
			Content.append("AM=M-1\n");
			Content.append("D=M-D\n");
			String UniqueName = findUniqueName(command,eqIndex);
			eqIndex++;
			Content.append("@"+UniqueName+"\n");
			Content.append("D;JLE\n");
			Content.append("@SP\n");
			Content.append("A=M\n");
			Content.append("M=-1\n");
			Content.append("@"+UniqueName+"_END"+"\n");
			Content.append("0;JMP\n");
			Content.append("("+UniqueName+")"+"\n");
			Content.append("@SP\n");
			Content.append("A=M\n");
			Content.append("M=0\n");
			Content.append("("+UniqueName+"_END)"+"\n");
			Content.append("@SP\n");		
			Content.append("M=M+1");
			FilesUtil.writeToTextFile(outputFile, "//"+ps.currentStr);
			FilesUtil.writeToTextFile(outputFile, Content.toString());
			
			}
		if (command.equals("lt")){
			StringBuilder Content = new StringBuilder(); 
			Content.append("@SP\n");
			Content.append("AM=M-1\n");		
			Content.append("D=M\n");
			Content.append("@SP\n");
			Content.append("AM=M-1\n");
			Content.append("D=M-D\n");
			String UniqueName = findUniqueName(command,eqIndex);
			eqIndex++;
			Content.append("@"+UniqueName+"\n");
			Content.append("D;JGE\n");
			Content.append("@SP\n");
			Content.append("A=M\n");
			Content.append("M=-1\n");
			Content.append("@"+UniqueName+"_END"+"\n");
			Content.append("0;JMP\n");
			Content.append("("+UniqueName+")"+"\n");
			Content.append("@SP\n");
			Content.append("A=M\n");
			Content.append("M=0\n");
			Content.append("("+UniqueName+"_END)"+"\n");
			Content.append("@SP\n");		
			Content.append("M=M+1");
			FilesUtil.writeToTextFile(outputFile, "//"+ps.currentStr);
			FilesUtil.writeToTextFile(outputFile, Content.toString());
			
			}
		if (command.equals("and")){
			StringBuilder Content = new StringBuilder(); 
			Content.append("@SP\n");
			Content.append("AM=M-1\n");		
			Content.append("D=M\n");
			Content.append("@SP\n");
			Content.append("A=M-1\n");
		
			Content.append("M=D&M");
			FilesUtil.writeToTextFile(outputFile, "//"+ps.currentStr);
			FilesUtil.writeToTextFile(outputFile, Content.toString());
			
			}
		
		if (command.equals("or")){
			StringBuilder Content = new StringBuilder(); 
			Content.append("@SP\n");
			Content.append("AM=M-1\n");		
			Content.append("D=M\n");
			Content.append("@SP\n");
			Content.append("A=M-1\n");
		
			Content.append("M=D|M");
			FilesUtil.writeToTextFile(outputFile, "//"+ps.currentStr);
			FilesUtil.writeToTextFile(outputFile, Content.toString());
			
			}
		
		if (command.equals("not")){
			StringBuilder Content = new StringBuilder(); 
			Content.append("@SP\n");
			Content.append("A=M-1\n");		
			Content.append("M=!M");
			FilesUtil.writeToTextFile(outputFile, "//"+ps.currentStr);
			FilesUtil.writeToTextFile(outputFile, Content.toString());
			
			}
		
		
	}
	String findUniqueName(String command, int index){
		if (command.equals("eq")||command.equals("lt")||command.equals("gt")){
			StringBuilder Content = new StringBuilder(); 
			
			int a = outputFile.lastIndexOf("/")+1;
			Content.append(outputFile.substring(a,outputFile.lastIndexOf(".asm")));
			int slashIndex = inputFile.lastIndexOf("/");
			int vmIndex=inputFile.lastIndexOf(".vm");		
			Content.append("."+inputFile.substring(slashIndex+1, vmIndex));
			Content.append("."+command);
			Content.append("."+index);
			
			return Content.toString();
		} else if (command.equals("static")){
			
			StringBuilder Content = new StringBuilder(); 
			int slashIndex = inputFile.lastIndexOf("/");
			int vmIndex=inputFile.lastIndexOf(".vm");		
			Content.append(inputFile.substring(slashIndex+1, vmIndex));
			Content.append("."+index);
			
			return Content.toString();
		} else return null;
		
	}

	void writePushPop(int command, String segment, int index)
			throws IOException {
		if (command == ps.C_PUSH) {
			if (segment.equals("constant")) {
				StringBuilder Content = new StringBuilder();
				Content.append("@" + index + "\n");
				Content.append("D=A\n");
				Content.append("@SP\n");
				Content.append("A=M\n");
				Content.append("M=D\n");
				Content.append("@SP\n");
				Content.append("M=M+1");

				FilesUtil.writeToTextFile(outputFile, "//" + ps.currentStr);
				FilesUtil.writeToTextFile(outputFile, Content.toString());

			}
			if (segment.equals("local") || segment.equals("argument")
					|| segment.equals("this") || segment.equals("that")) {
				String keyWord;

				switch (segment) {
				case "local":
					keyWord = "LCL";
					break;
				case "argument":
					keyWord = "ARG";
					break;
				case "this":
					keyWord = "THIS";
					break;
				case "that":
					keyWord = "THAT";
					break;
				default:
					keyWord = "Invalid Command";
					break;
				}
				StringBuilder Content = new StringBuilder();
				Content.append("@" + index + "\n");
				Content.append("D=A\n");
				Content.append("@" + keyWord + "\n");
				Content.append("A=M+D\n");
				Content.append("D=M\n");
				Content.append("@SP\n");
				Content.append("A=M\n");
				Content.append("M=D\n");
				Content.append("@SP\n");
				Content.append("M=M+1");
				FilesUtil.writeToTextFile(outputFile, "//" + ps.currentStr);
				FilesUtil.writeToTextFile(outputFile, Content.toString());

			}
			if (segment.equals("pointer")) {
				String keyWord;

				switch (index) {
				case 0:
					keyWord = "3";
					break;
				case 1:
					keyWord = "4";
					break;
				default:
					keyWord = "Invalid";
					break;
				}
				StringBuilder Content = new StringBuilder();
				Content.append("@" + keyWord + "\n");
				Content.append("D=M\n");
				Content.append("@SP\n");
				Content.append("A=M\n");
				Content.append("M=D\n");
				Content.append("@SP\n");
				Content.append("M=M+1");
				FilesUtil.writeToTextFile(outputFile, "//" + ps.currentStr);
				FilesUtil.writeToTextFile(outputFile, Content.toString());

			}

			if (segment.equals("temp")) {
				int newIndex;
				newIndex = index + 5;

				StringBuilder Content = new StringBuilder();
				Content.append("@" + newIndex + "\n");
				Content.append("D=M\n");
				Content.append("@SP\n");
				Content.append("A=M\n");
				Content.append("M=D\n");
				Content.append("@SP\n");
				Content.append("M=M+1");
				FilesUtil.writeToTextFile(outputFile, "//" + ps.currentStr);
				FilesUtil.writeToTextFile(outputFile, Content.toString());

			}

			if (segment.equals("static")) {

				StringBuilder Content = new StringBuilder();
				Content.append("@" + findUniqueName(segment, ps.arg2()) + "\n");
				Content.append("D=M\n");
				Content.append("@SP\n");
				Content.append("A=M\n");
				Content.append("M=D\n");
				Content.append("@SP\n");
				Content.append("M=M+1");				
				
				FilesUtil.writeToTextFile(outputFile, "//" + ps.currentStr);
				FilesUtil.writeToTextFile(outputFile, Content.toString());

			}
		} else if (command == ps.C_POP) {
			if (segment.equals("local") || segment.equals("argument")
					|| segment.equals("this") || segment.equals("that")) {
				String keyWord;

				switch (segment) {
				case "local":
					keyWord = "LCL";
					break;
				case "argument":
					keyWord = "ARG";
					break;
				case "this":
					keyWord = "THIS";
					break;
				case "that":
					keyWord = "THAT";
					break;
				default:
					keyWord = "Invalid Command";
					break;
				}
				StringBuilder Content = new StringBuilder();
				Content.append("@" + index + "\n");
				Content.append("D=A\n");
				Content.append("@" + keyWord + "\n");
				Content.append("D=M+D\n");
				Content.append("@SP\n");
				Content.append("A=M\n");
				Content.append("M=D\n");
				Content.append("@SP\n");
				Content.append("A=M-1\n");
				Content.append("D=M\n");
				Content.append("@SP\n");
				Content.append("A=M\n");
				Content.append("A=M\n");
				Content.append("M=D\n");
				Content.append("@SP\n");

				Content.append("M=M-1");
				FilesUtil.writeToTextFile(outputFile, "//" + ps.currentStr);
				FilesUtil.writeToTextFile(outputFile, Content.toString());

			}

			if (segment.equals("pointer")) {
				String keyWord;

				switch (index) {
				case 0:
					keyWord = "3";
					break;
				case 1:
					keyWord = "4";
					break;
				default:
					keyWord = "Invalid";
					break;
				}
				StringBuilder Content = new StringBuilder();
				Content.append("@SP\n");
				Content.append("AM=M-1\n");
				Content.append("D=M\n");
				Content.append("@" + keyWord + "\n");

				Content.append("M=D");
				FilesUtil.writeToTextFile(outputFile, "//" + ps.currentStr);
				FilesUtil.writeToTextFile(outputFile, Content.toString());

			}

			if (segment.equals("temp")) {
				int newIndex = index + 5;
				StringBuilder Content = new StringBuilder();
				Content.append("@SP\n");
				Content.append("AM=M-1\n");
				Content.append("D=M\n");
				Content.append("@" + newIndex + "\n");

				Content.append("M=D");
				FilesUtil.writeToTextFile(outputFile, "//" + ps.currentStr);
				FilesUtil.writeToTextFile(outputFile, Content.toString());

			}

			if (segment.equals("static")) {

				StringBuilder Content = new StringBuilder();
				Content.append("@SP\n");
				Content.append("AM=M-1\n");
				Content.append("D=M\n");
				Content.append("@" + findUniqueName(segment, ps.arg2()) + "\n");
				Content.append("M=D");
				FilesUtil.writeToTextFile(outputFile, "//" + ps.currentStr);
				FilesUtil.writeToTextFile(outputFile, Content.toString());

			}

		}
	}
	
	void writeLabel (String label) throws IOException{
		StringBuilder Content = new StringBuilder();
		Content.append("("+makeUnique(currentFunctionName,label)+")");
		FilesUtil.writeToTextFile(outputFile, "//" + ps.currentStr);
		FilesUtil.writeToTextFile(outputFile, Content.toString());
	}
	String makeUnique(String domain, String inputString){
		return domain+".$"+inputString;
	}
	void writeGoto (String label) throws IOException{
		StringBuilder Content = new StringBuilder();
		Content.append("@"+makeUnique(currentFunctionName,label)+"\n");
		Content.append("0;JMP");
		FilesUtil.writeToTextFile(outputFile, "//" + ps.currentStr);
		FilesUtil.writeToTextFile(outputFile, Content.toString());
	}
	void writeIf (String label) throws IOException{
		StringBuilder Content = new StringBuilder();
		
		Content.append("@SP\n");
		Content.append("AM=M-1\n");
		Content.append("D=M\n");
		Content.append("@"+makeUnique(currentFunctionName,label)+"\n");
		Content.append("D;JNE");
		FilesUtil.writeToTextFile(outputFile, "//" + ps.currentStr);
		FilesUtil.writeToTextFile(outputFile, Content.toString());
	}
	
	void writeFunction (String functionName, int numLocals) throws IOException{
		StringBuilder Content = new StringBuilder();
		
		Content.append("("+functionName+")\n");
		int i=0;
		while (i<numLocals)	{
			
			Content.append("@" + i + "\n");
			Content.append("D=A\n");
			Content.append("@LCL\n");
			Content.append("A=M+D\n");
			Content.append("M=0\n");
			Content.append("@SP\n");
			Content.append("M=M+1\n");
			
			
			i++;
		}

		
		FilesUtil.writeToTextFile(outputFile, "//" + ps.currentStr);
		FilesUtil.writeToTextFile(outputFile, Content.toString());
	}
	void writeReturn () throws IOException{
		StringBuilder Content = new StringBuilder();
		
		Content.append("@LCL\n");
		Content.append("D=M\n");
		Content.append("@"+makeUnique(currentFunctionName,"Frame")+"\n");
		Content.append("M=D\n");
		Content.append("@5\n");
		Content.append("A=D-A\n");
		Content.append("D=M\n");		
		Content.append("@"+makeUnique(currentFunctionName,"RET")+"\n");
		Content.append("M=D\n");
		
		Content.append("@SP\n");
		Content.append("A=M-1\n");
		Content.append("D=M\n");
		Content.append("@ARG\n");
		Content.append("A=M\n");
		Content.append("M=D\n");
		Content.append("D=A+1\n");
		Content.append("@SP\n");
		Content.append("M=D\n");
		
		Content.append("@"+makeUnique(currentFunctionName,"Frame")+"\n");
		Content.append("D=M\n");
		Content.append("@1\n");
		Content.append("A=D-A\n");
		Content.append("D=M\n");
		Content.append("@THAT\n");
		Content.append("M=D\n");

		Content.append("@"+makeUnique(currentFunctionName,"Frame")+"\n");
		Content.append("D=M\n");
		Content.append("@2\n");
		Content.append("A=D-A\n");
		Content.append("D=M\n");
		Content.append("@THIS\n");
		Content.append("M=D\n");
		
		Content.append("@"+makeUnique(currentFunctionName,"Frame")+"\n");
		Content.append("D=M\n");
		Content.append("@3\n");
		Content.append("A=D-A\n");
		Content.append("D=M\n");
		Content.append("@ARG\n");
		Content.append("M=D\n");
		
		Content.append("@"+makeUnique(currentFunctionName,"Frame")+"\n");
		Content.append("D=M\n");
		Content.append("@4\n");
		Content.append("A=D-A\n");
		Content.append("D=M\n");
		Content.append("@LCL\n");
		Content.append("M=D\n");	
		
		Content.append("@"+makeUnique(currentFunctionName,"RET")+"\n");		
		Content.append("D=M\n");
		Content.append("A=D\n");		
		Content.append("0;JMP");


		FilesUtil.writeToTextFile(outputFile, "//" + ps.currentStr);
		FilesUtil.writeToTextFile(outputFile, Content.toString());
	}
	void writeCall (String functionName, int numArgs) throws IOException{
		StringBuilder Content = new StringBuilder();
		int n=numArgs;
		
		
		Content.append("@"+makeUnique(currentFunctionName,"REG")+rtIndex+"\n");
		
		
		Content.append("D=A\n");
		Content.append("@SP\n");
		Content.append("A=M\n");
		Content.append("M=D\n");
		Content.append("@SP\n");
		Content.append("M=M+1\n");

		Content.append("@LCL\n");
		Content.append("D=M\n");
		Content.append("@SP\n");
		Content.append("A=M\n");
		Content.append("M=D\n");
		Content.append("@SP\n");
		Content.append("M=M+1\n");
		
		Content.append("@ARG\n");
		Content.append("D=M\n");
		Content.append("@SP\n");
		Content.append("A=M\n");
		Content.append("M=D\n");
		Content.append("@SP\n");
		Content.append("M=M+1\n");
		
		Content.append("@THIS\n");
		Content.append("D=M\n");
		Content.append("@SP\n");
		Content.append("A=M\n");
		Content.append("M=D\n");
		Content.append("@SP\n");
		Content.append("M=M+1\n");		
		
		Content.append("@THAT\n");
		Content.append("D=M\n");
		Content.append("@SP\n");
		Content.append("A=M\n");
		Content.append("M=D\n");
		Content.append("@SP\n");
		Content.append("M=M+1\n");
		
		Content.append("@SP\n");
		Content.append("D=M\n");
		Content.append("@"+numArgs+"\n");
		Content.append("D=D-A\n");
		Content.append("@5\n");
		Content.append("D=D-A\n");
		Content.append("@ARG\n");
		Content.append("M=D\n");
		Content.append("@SP\n");
		Content.append("D=M\n");
		Content.append("@LCL\n");
		Content.append("M=D\n");	
		
		Content.append("@"+functionName+"\n");
		Content.append("0;JMP\n");
		
		Content.append("("+makeUnique(currentFunctionName,"REG")+rtIndex+")\n");
		
		rtIndex++;
		
		FilesUtil.writeToTextFile(outputFile, "//" + ps.currentStr);
		FilesUtil.writeToTextFile(outputFile, Content.toString());
	}

	void close(){
		
	}
	
	public static void main(String[] args) {
		String outputFileName=null;
		if (args.length == 1) {
			File test = new File(args[0]);
			if (test.exists()) {
				
				if (test.isFile()) {
					
					int index = test.toString().lastIndexOf(".vm");
					String fileNameWOExtn = test.toString().substring(0, index);
					 outputFileName = fileNameWOExtn+".asm";
					 CodeWriter cw;
					try {
						cw = new CodeWriter(outputFileName);
						cw.setFileName(test.toString());
						
					

						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					 
					 
				}
				if (test.isDirectory()) {
					outputFileName=test.toString()+".asm";	
					 CodeWriter cw;
					 int index=test.toString().lastIndexOf("/");
						String name = test.toString().substring(index);
						outputFileName=test.toString()+name+".asm";	
						
					try {
						cw = new CodeWriter(outputFileName);
						
						
						File dir = new File(args[0]);
						File[] files = dir.listFiles(new FilenameFilter() {
						    public boolean accept(File dir, String name) {
						        return name.toLowerCase().endsWith(".vm");
						    }
						});
						for (int i=0; i<files.length;i++){
							cw.setFileName(files[i].toString());
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
			System.out.println(outputFileName);
		} 
//		else if (args.length > 1) {
//			for (int i = 0; i < args.length; i++) {
//				File test = new File(args[i]);
//				if (test.isFile()&&test.exists()) {
//					Parser ps = new Parser(args[i]);
//				}
//				
//			}
//		}

	} 
}
