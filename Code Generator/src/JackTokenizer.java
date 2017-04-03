import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ListIterator;


public class JackTokenizer {
	public static final int KEYWORD = 1;
	public static final int SYMBOL = 2;
	public static final int IDENTIFIER = 3;
	public static final int INT_CONST = 4;
	public static final int STRING_CONST = 5;
	
	List<String> ls;
	ListIterator<String> itr;
	
	
	String currentStr;
	String inputFileName;
	
	public JackTokenizer(String inputJack) {
		try {
			
			inputFileName=inputJack;
			String inputFile = FilesUtil.readTextFile(inputJack);
			List<String> ls = FilesUtil.cleanUpInput(inputFile);
			itr = ls.listIterator();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	Boolean hasMoreTokens() {
		return itr.hasNext();
	}

	void advance() {
		if (hasMoreTokens())
			currentStr = itr.next();
	}
	
	int tokenType(){
		String keywordString = "(class|constructor|function|method|field|static|var|int|char|boolean|void|true|false|null|this|let|do|if|else|while|return)";
		String Symbol = "(\\{|\\}|\\(|\\)|\\[|\\]|\\.|\\,|\\;|\\+|\\-|\\*|\\/|\\&|\\||\\<|\\>|\\=|\\~)";
		String integerConstant = "\\d*";
		String StringConstant = "\".*\"";
		String identifier = "[a-zA-Z_][a-zA-Z0-9_]*";
		if (currentStr.matches(keywordString))
			{return KEYWORD;}
		else if (currentStr.matches(Symbol)){
			return SYMBOL;
		}
		else if (currentStr.matches(integerConstant)){
			return INT_CONST;
		}
		else if (currentStr.matches(StringConstant)){
			return STRING_CONST;
		}
		else if (currentStr.matches(identifier)){
			return IDENTIFIER;
		}
		else return 0;
	}
	String keyWord(){
		if (tokenType()==KEYWORD) return currentStr;
		else return null;
	}
	String symbol(){
		if ((tokenType()==SYMBOL)&currentStr.matches("(\\<)"))
			return "&lt;";
		if ((tokenType()==SYMBOL)&currentStr.matches("(\\>)"))
			return "&gt;";
		if ((tokenType()==SYMBOL)&currentStr.matches("(\\&)"))
			return "&amp;";
		else return currentStr;
	}
	String identifier(){
		if (tokenType()==IDENTIFIER) return currentStr;
		else return null;
	}
	int intVal(){
		if (tokenType()==INT_CONST) return Integer.parseInt(currentStr);
		else return 0;
	}
	String stringVal(){
		if (tokenType()==STRING_CONST) return currentStr.substring(1, currentStr.length()-1);
		else return null;
	}
	
	public static void main(String[] args) throws IOException {
		JackTokenizer token = new JackTokenizer(args[0]);
		int index = args[0].lastIndexOf(".jack");
		String fileNameWOExtn = args[0].substring(0, index);
		String outputFileName = fileNameWOExtn+"T2.xml";
		
		File file = new File (outputFileName);
		
		if (file.exists()) file.delete();
		FilesUtil.writeToTextFile(outputFileName, "<tokens>");
		StringBuilder Content = new StringBuilder();
		
		while (token.hasMoreTokens()) {
			String currentOutput=null;
			token.advance();
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
		FilesUtil.writeToTextFile(outputFileName, "</tokens>");

		
//		String[] splitted = inputFile.split("\\s+");
//		for (int i=0;i<splitted.length;i++){
//			System.out.println(splitted[i]);
//		}
		
		
		
//		String mod = st.replaceAll("[{}]", replacement)
//		String[] splitted = st.split("\"");
		
		
//		st=st.replaceAll("//.*\n","");
//		st=st.replaceAll("\n", " ");
//		st=st.replaceAll("\\/\\*.*\\*\\/","");
////		st=st.replaceAll("\\/\\*.*\\n","");
//	//	st=st.replaceAll(".*\\*\\/","");
//		System.out.println(st);
		
	}
	
}	

