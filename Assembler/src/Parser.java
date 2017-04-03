import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

public class Parser {
	public static final int A_COMMAND = 1;
	public static final int C_COMMAND = 2;
	public static final int L_COMMAND = 3;

	List<String> ls;
	ListIterator<String> itr;

	public Parser(String filename) {
		try {
			ls = FilesUtil.readTextFileByLines(filename);
			FilesUtil.compressList(ls);
			itr = ls.listIterator();
		
			int index = filename.lastIndexOf(".asm");
			String fileNameWOExtn = filename.substring(0, index);
			String NewfileNameWExtn = fileNameWOExtn+".hack";
			File file = new File (NewfileNameWExtn);
			System.out.println(file.toString());
			if (file.exists()) file.delete();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	SymbolTable FirstPass() {
		int ROMIndex = -1;
		int RAMIndex = 16;
		String currentStr = null;
		SymbolTable st = new SymbolTable();
		while (this.hasMoreCommands()) {
			this.advance();
			if (this.commandType() == A_COMMAND) {
				ROMIndex++;

				if (!isNumeric(this.symbol()))
					currentStr = this.symbol();
				// if
				// ((!st.RAMtable.contains(ps.symbol()))&&(!isNumeric(ps.symbol())))
				// {
				// st.RAMtable.put(ps.symbol(), new Integer(RAMIndex));
				// }
			}
			if (this.commandType() == C_COMMAND) {
				ROMIndex++;
				if (currentStr != null) {
					if ((jump() == null)&&(this.currentStr.contains("M")) && (!st.table.containsKey(currentStr))) {
						st.table.put(currentStr, new Integer(RAMIndex));
						RAMIndex++;
					}
				}
			}
			if (this.commandType() == L_COMMAND) {
				st.table.put(this.symbol(), new Integer(ROMIndex + 1));
			}
		}
		itr = ls.listIterator();
		return st;
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

		if (currentStr.startsWith("@")) {
			return A_COMMAND;
		} else if ((currentStr.contains("=")) || (currentStr.contains(";"))) {
			return C_COMMAND;
		} else if ((currentStr.startsWith("(")) && currentStr.endsWith(")")) {
			return L_COMMAND;
		} else {
			return 0;
		}
	}

	String symbol() {
		String sy;
		if (commandType() == A_COMMAND) {
			return sy = currentStr.substring(1, currentStr.length());

		} else if (commandType() == L_COMMAND) {
			return sy = currentStr.substring(1, currentStr.length() - 1);

		} else
			return sy = null;

	}

	String dest() {
		String sy;
		if (commandType() == C_COMMAND) {
			int index;
			if (currentStr.contains("=")) {
				index = currentStr.indexOf("=");
				sy = currentStr.substring(0, index);
			} else
				sy = null;

		} else
			sy = null;
		return sy;
	}

	String comp() {
		String sy;
		if (commandType() == C_COMMAND) {
			int index;
			if ((currentStr.contains("=")) && (currentStr.contains(";"))) {
				sy = currentStr.substring(currentStr.indexOf("=") + 1,
						currentStr.indexOf(";"));
			} else if (currentStr.contains("=")) {
				sy = currentStr.substring(currentStr.indexOf("=") + 1,
						currentStr.length());
			} else if (currentStr.contains(";")) {
				sy = currentStr.substring(0, currentStr.indexOf(";"));
			} else
				sy = null;

		} else
			sy = null;
		return sy;
	}

	String jump() {
		String sy;
		if (commandType() == C_COMMAND) {
			int index;
			if (currentStr.contains(";")) {
				sy = currentStr.substring(currentStr.indexOf(";") + 1,
						currentStr.length());
			} else
				sy = null;

		} else
			sy = null;
		return sy;
	}

	public static void main(String[] args) throws IOException {
		String filename = args[0];
		int index = args[0].lastIndexOf(".asm");
		String fileNameWOExtn = args[0].substring(0, index);
		String NewfileNameWExtn = fileNameWOExtn+".hack";
		
		String outputString;
		Parser ps = new Parser(filename);
		SymbolTable st = ps.FirstPass();
		System.out.println("contains?"+st.table.containsKey("ponggame.0"));
//		Set <String> keys=st.table.keySet();
//		for (String s:keys){
//			System.out.printf("Symbol: %15s; value: %15s \n",s,st.table.get(s));
//		}
		while (ps.hasMoreCommands()) {
			ps.advance();
			int cType = ps.commandType();
			String cTypeStr;
			
			switch (cType) {
			case 1:
				cTypeStr = "A Command";
				break;
			case 2:
				cTypeStr = "C Command";
				break;
			case 3:
				cTypeStr = "L Command";
				break;
			default:
				cTypeStr = "Invalid Command";
				break;
			}
			int tempInt;
			if (cType == A_COMMAND) {
				if(!isNumeric(ps.symbol())){
					 tempInt = ((Integer) st.table.get(ps.symbol())).intValue();}
				else {
					tempInt = Integer.parseInt(ps.symbol());
				}
							

					outputString = Integer.toBinaryString(tempInt);
					StringBuilder sb = new StringBuilder();

					for (int toPrepend=15-outputString.length(); toPrepend>0; toPrepend--) {
					    sb.append('0');
					}

					sb.append(outputString);
					sb.insert(0,'0');
					
					String newString = sb.toString();
//					System.out.printf("%15s %15s \n",newString,ps.currentStr );
					FilesUtil.writeToTextFile(NewfileNameWExtn, newString);
				}

			
			if (cType==C_COMMAND){
				StringBuilder sb = new StringBuilder();
				sb.append("111");
				sb.append(Code.comp(ps.comp()));
				sb.append(Code.dest(ps.dest()));
				sb.append(Code.jump(ps.jump()));
				String newString = sb.toString();

//				System.out.printf("%15s %15s \n",newString,ps.currentStr );
				FilesUtil.writeToTextFile(NewfileNameWExtn, newString);
			}
			
	}

			// System.out.printf("%10s %10s %10s %10s %10s %n",cTypeStr,
			// ps.symbol(), ps.dest(),ps.comp(),ps.jump());

			// System.out.printf("%10s %n",Code.comp(biOut));

		}
	
	public static boolean isNumeric(String str)
	{
	    for (char c : str.toCharArray())
	    {
	        if (!Character.isDigit(c)) return false;
	    }
	    return true;
	}

}
