import java.io.File;
import java.io.IOException;

public class CompilationEngine {
	public static final int KEYWORD = 1;
	public static final int SYMBOL = 2;
	public static final int IDENTIFIER = 3;
	public static final int INT_CONST = 4;
	public static final int STRING_CONST = 5;
	String outputFileName;
	JackTokenizer token;

	public CompilationEngine(String inputFile) throws IOException {

		token = new JackTokenizer(inputFile);
		int index = inputFile.lastIndexOf(".jack");
		String fileNameWOExtn = inputFile.substring(0, index);
		outputFileName = fileNameWOExtn + "2.xml";
		File file = new File(outputFileName);

		if (file.exists())
			file.delete();
		FilesUtil.writeToTextFile(outputFileName, "<class>");
		do {
			token.advance();
			writeTerminal();						
		}while (!token.currentStr.equals("{"));
		token.advance();
		CompileClass();
	}

	void CompileClass() throws IOException {
		
		
		while ((token.hasMoreTokens())&&(token.currentStr!="}")) {
			
			if (token.tokenType() == KEYWORD) {
				if ((token.keyWord().equals("function"))||(token.keyWord().equals("constructor"))||(token.keyWord().equals("method"))) {
					CompileSubroutine();
					 
				}	
				else if ((token.keyWord().equals("static"))|| (token.keyWord().equals("field"))) {
					CompileClassVarDec();
					 
				}	
			}
			token.advance();
			
		}
		writeTerminal();
		FilesUtil.writeToTextFile(outputFileName, "</class>");
	}
	void CompileSubroutine() throws IOException{
		FilesUtil.writeToTextFile(outputFileName, "<subroutineDec>");
		writeTerminal();
		do {
			token.advance();
			writeTerminal();		
									
		}while (!token.currentStr.equals("("));
		
		token.advance();
		CompileParameterList();
		
		
		writeTerminal();
		FilesUtil.writeToTextFile(outputFileName, "<subroutineBody>");
		token.advance();
		writeTerminal();
		do {
			token.advance();
			
			if  ((token.keyWord().equals("var"))) {
				CompileVarDec();
			}
			else if  ((token.keyWord().equals("let"))|| (token.keyWord().equals("if"))|| (token.keyWord().equals("while"))|| (token.keyWord().equals("do"))|| (token.keyWord().equals("return"))) {
				CompileStatements();
			}
		}while (!token.currentStr.equals("}"));
		writeTerminal();
		FilesUtil.writeToTextFile(outputFileName, "</subroutineBody>");
		FilesUtil.writeToTextFile(outputFileName, "</subroutineDec>");
	}
	void CompileClassVarDec() throws IOException{
		FilesUtil.writeToTextFile(outputFileName, "<classVarDec>");
		writeTerminal();
		do {
			token.advance();
			writeTerminal();		
									
		}while (!token.currentStr.equals(";"));
		FilesUtil.writeToTextFile(outputFileName, "</classVarDec>");
		
	}
	void CompileVarDec() throws IOException{
		FilesUtil.writeToTextFile(outputFileName, "<varDec>");
		writeTerminal();
		do {
			token.advance();
			writeTerminal();		
									
		}while (!token.currentStr.equals(";"));
		FilesUtil.writeToTextFile(outputFileName, "</varDec>");
		
	}
	void CompileParameterList() throws IOException{
		FilesUtil.writeToTextFile(outputFileName, "<parameterList>");
		
		while  (!token.currentStr.equals(")")){
			writeTerminal();
			token.advance();
		}
		FilesUtil.writeToTextFile(outputFileName, "</parameterList>");
	}

	void CompileStatements() throws IOException {
		FilesUtil.writeToTextFile(outputFileName, "<statements>");
		do { 
		if (token.tokenType() == KEYWORD) {
			if (token.keyWord().equals("do")){
				CompileDo();
				 
			}	
			else if (token.keyWord().equals("let")){
				CompileLet();
				 
			}	
			else if (token.keyWord().equals("while")){
				CompileWhile();
				 
			}	
			
			else if (token.keyWord().equals("if")){
				CompileIf();
				 
			}	
			else if (token.keyWord().equals("return")){
				CompileReturn();
				 
			}	
			
		}
		} while (!token.currentStr.equals("}"));
		
		
		
		FilesUtil.writeToTextFile(outputFileName, "</statements>");
	}
	void CompileDo() throws IOException {
		FilesUtil.writeToTextFile(outputFileName, "<doStatement>");
		writeTerminal();
		do {
			token.advance();
			writeTerminal();		
			if (token.currentStr.equals("(")) {
				token.advance();
				CompileExpressionList();
				writeTerminal();
				token.advance();  //; 
				writeTerminal();   //write ;
			} //missing something ????????????????????????????????????????????????????
		}while (!token.currentStr.equals(";"));
		token.advance();
		FilesUtil.writeToTextFile(outputFileName, "</doStatement>");
	}
	void CompileLet() throws IOException {
		FilesUtil.writeToTextFile(outputFileName, "<letStatement>");
		writeTerminal();
		do {
			token.advance();
			writeTerminal();		
			if (token.currentStr.equals("[")){
				token.advance();
				CompileExpression();
				writeTerminal();
				token.advance();
				writeTerminal();
			}
									
		}while (!token.currentStr.equals("="));
		token.advance();
		CompileExpression();   // before a new method is called, advance() already been called!!!!!
		writeTerminal();
		token.advance();
		FilesUtil.writeToTextFile(outputFileName, "</letStatement>");
	}
	void CompileWhile() throws IOException{
		FilesUtil.writeToTextFile(outputFileName, "<whileStatement>");
		writeTerminal();
		do {
			token.advance();
			writeTerminal();		
			if (token.currentStr.equals("(")) {
				token.advance();
				CompileExpression();
				writeTerminal();
				token.advance();
				writeTerminal();   //write {
			}				
		}while (!token.currentStr.equals("{"));
		token.advance();
				
			CompileStatements();
			writeTerminal();
			token.advance();
		
		
		FilesUtil.writeToTextFile(outputFileName, "</whileStatement>");
	}
	void CompileIf() throws IOException{
		FilesUtil.writeToTextFile(outputFileName, "<ifStatement>");
		writeTerminal();
		do {
			token.advance();
			writeTerminal();		
			if (token.currentStr.equals("(")) {
				token.advance();
				CompileExpression();
				writeTerminal();
				token.advance();
				writeTerminal();   //write {
			}				
		}while (!token.currentStr.equals("{"));
		token.advance();
		CompileStatements();
		writeTerminal();
		token.advance();
		if (token.currentStr.equals("else")){
			// else??????????????????????
		}
		FilesUtil.writeToTextFile(outputFileName, "</ifStatement>");
	}
	void CompileReturn() throws IOException{
		FilesUtil.writeToTextFile(outputFileName, "<returnStatement>");
		writeTerminal();
		token.advance();
		if (!token.currentStr.equals(";")){
			CompileExpression();
		}
		writeTerminal();
		token.advance();
		FilesUtil.writeToTextFile(outputFileName, "</returnStatement>");
	}
	void CompileExpressionList() throws IOException{
		FilesUtil.writeToTextFile(outputFileName, "<expressionList>");
		
		while (!token.currentStr.equals(")")){
			CompileExpression();
			if (token.currentStr.equals(",")){
				writeTerminal();
				token.advance();
			}
		}
				
		FilesUtil.writeToTextFile(outputFileName, "</expressionList>");
	}
	void CompileExpression() throws IOException{
		String op = "(\\+|\\-|\\*|\\/|\\&|\\||\\<|\\>|\\=)";
		FilesUtil.writeToTextFile(outputFileName, "<expression>");
		do {
			
		if (token.currentStr.matches(op))
		{writeTerminal();
		token.advance();} 
		else {
			CompileTerm();
		}
			
		} while ((!token.currentStr.equals(";"))&&(!token.currentStr.equals(","))&&(!token.currentStr.equals(")"))&&(!token.currentStr.equals("]")));
		
	
		FilesUtil.writeToTextFile(outputFileName, "</expression>");
		
		
	}
	
	void CompileTerm() throws IOException{
	
		FilesUtil.writeToTextFile(outputFileName, "<term>");
		
		String op = "(\\+|\\-|\\*|\\/|\\&|\\||\\<|\\>|\\=)";

		
		if (token.currentStr.equals("(")){
			writeTerminal();
			token.advance();
			CompileExpression();
			writeTerminal();
			token.advance();
		} else if(token.currentStr.equals("~")) {
			writeTerminal();
			token.advance();
			CompileTerm();
		} 
		else {
			do {
				writeTerminal();
				token.advance();
				if (token.currentStr.equals("(")){
					writeTerminal();
					token.advance();
					CompileExpressionList();
					writeTerminal();
					token.advance();
				} else if (token.currentStr.equals("[")){
					writeTerminal();
					token.advance();
					CompileExpression();
					writeTerminal();
					token.advance();
					
				}
			}
			while (!token.currentStr.matches(op)&&(!token.currentStr.equals(";"))&&(!token.currentStr.equals(")"))&&(!token.currentStr.equals("]"))&&(!token.currentStr.equals(",")));
		}
		FilesUtil.writeToTextFile(outputFileName, "</term>");
	}
	
	void writeTerminal() throws IOException{
		String currentOutput;
		if (token.tokenType()==KEYWORD){
			
			currentOutput="<keyword> "+token.keyWord()+" </keyword>";
			FilesUtil.writeToTextFile(outputFileName,currentOutput);
		}
		
		if (token.tokenType()==SYMBOL){
			
			currentOutput="<symbol> "+token.symbol()+" </symbol>";
			FilesUtil.writeToTextFile(outputFileName,currentOutput);
		}
		
		if (token.tokenType()==IDENTIFIER){
			
			currentOutput="<identifier> "+token.identifier()+" </identifier>";
			FilesUtil.writeToTextFile(outputFileName,currentOutput);
		}
		if (token.tokenType()==INT_CONST){
			
			currentOutput="<integerConstant> "+token.intVal()+" </integerConstant>";
			FilesUtil.writeToTextFile(outputFileName,currentOutput);
		}
		if (token.tokenType()==STRING_CONST){
			
			currentOutput="<stringConstant> "+token.stringVal()+" </stringConstant>";
			FilesUtil.writeToTextFile(outputFileName,currentOutput);
		}
	}
	public static void main(String[] args){
		try {
			CompilationEngine ce = new CompilationEngine(args[0]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
