import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

public class CompilationEngine {
	public static final int KEYWORD = 1;
	public static final int SYMBOL = 2;
	public static final int IDENTIFIER = 3;
	public static final int INT_CONST = 4;
	public static final int STRING_CONST = 5;
	String outputFileName;
	String vmOut;
	JackTokenizer token;
	SymbolTable st;
	String className;
	String currentSubroutine;
	String subroutineName;
	VMWriter vm;
	int nArgs;

	int nArgsAdd;
	int nArgTemp;
	int whileIndexGlobal = -1;
	int ifIndexGlobal;

	public CompilationEngine(String inputFile) throws IOException {

		String outputFileName = null;
		File test = new File(inputFile);
		if (test.exists()) {

			if (test.isFile()) {

				int index = test.toString().lastIndexOf(".jack");
				String fileNameWOExtn = test.toString().substring(0, index);
				outputFileName = fileNameWOExtn + ".vm";

				try {
					if (index != 0) {
						CompileEachFile(inputFile);
					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			if (test.isDirectory()) {
				outputFileName = test.toString() + ".vm";

				int index = test.toString().lastIndexOf("/");
				String name = test.toString().substring(index);
				outputFileName = test.toString() + name + ".vm";

				try {

					File dir = new File(inputFile);
					File[] files = dir.listFiles(new FilenameFilter() {
						public boolean accept(File dir, String name) {
							return name.toLowerCase().endsWith(".jack");
						}
					});
					for (int i = 0; i < files.length; i++) {
						CompileEachFile(files[i].toString());
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

	}

	void CompileEachFile(String inputFile) throws IOException {

		token = new JackTokenizer(inputFile);
		int index = inputFile.lastIndexOf(".jack");
		String fileNameWOExtn = inputFile.substring(0, index);
		outputFileName = fileNameWOExtn + ".xml";
		vmOut = fileNameWOExtn + ".vm";
		File file = new File(outputFileName);

		if (file.exists())
			file.delete();
		FilesUtil.writeToTextFile(outputFileName, "<class>");

		vm = new VMWriter(vmOut);

		do {

			token.advance();
			if ((!token.currentStr.equals("class"))
					&& (!token.currentStr.equals("{"))) {
				className = token.currentStr;
			}
			writeTerminal();
		} while (!token.currentStr.equals("{"));
		st = new SymbolTable();
		token.advance();
		CompileClass();

	}

	void CompileClass() throws IOException {

		while ((token.hasMoreTokens()) && (token.currentStr != "}")) {

			if (token.tokenType() == KEYWORD) {
				if (token.keyWord().equals("function")) {
					currentSubroutine = "function";
					whileIndexGlobal = -1;
					ifIndexGlobal = 0;
					CompileSubroutine();

				} else if (token.keyWord().equals("constructor")) {
					currentSubroutine = "constructor";
					whileIndexGlobal = -1;
					ifIndexGlobal = 0;
					CompileSubroutine();
				} else if (token.keyWord().equals("method")) {
					currentSubroutine = "method";
					whileIndexGlobal = -1;
					ifIndexGlobal = 0;
					CompileSubroutine();
				} else if ((token.keyWord().equals("static"))
						|| (token.keyWord().equals("field"))) {
					CompileClassVarDec();

				}
			}
			token.advance();

		}
		writeTerminal();
		FilesUtil.writeToTextFile(outputFileName, "</class>");
	}

	void CompileSubroutine() throws IOException {

		FilesUtil.writeToTextFile(outputFileName, "<subroutineDec>");
		writeTerminal();
		if (token.currentStr.equals("method")) {

			st.startSubroutine();
			st.Define("this", className, "argument");

		} else if (token.currentStr.equals("function")) {

			st.startSubroutine();
		} else if (token.currentStr.equals("constructor")) {

			st.startSubroutine();
		}

		token.advance();
		writeTerminal();
		token.advance();
		writeTerminal();
		subroutineName = token.currentStr;
		token.advance();
		writeTerminal();

		token.advance();
		CompileParameterList();

		writeTerminal();
		FilesUtil.writeToTextFile(outputFileName, "<subroutineBody>");
		token.advance();
		writeTerminal();
		do {
			token.advance();

			if ((token.keyWord().equals("var"))) {
				CompileVarDec();
			} else if ((token.keyWord().equals("let"))
					|| (token.keyWord().equals("if"))
					|| (token.keyWord().equals("while"))
					|| (token.keyWord().equals("do"))
					|| (token.keyWord().equals("return"))) {
				if (currentSubroutine.equals("constructor")) {
					vm.WriteFunction(className + ".new", st.VarCount("var"));
					vm.WritePush("CONST", st.VarCount("field"));
					vm.WriteCall("Memory.alloc", 1);
					vm.WritePop("POINTER", 0);
				} else if (currentSubroutine.equals("function")) {
					vm.WriteFunction(className + "." + subroutineName,
							st.VarCount("var"));

				} else if (currentSubroutine.equals("method")) {
					vm.WriteFunction(className + "." + subroutineName,
							st.VarCount("var"));
					vm.WritePush("ARG", 0);
					vm.WritePop("POINTER", 0);
				}

				CompileStatements();
			}
		} while (!token.currentStr.equals("}"));
		writeTerminal();
		FilesUtil.writeToTextFile(outputFileName, "</subroutineBody>");
		FilesUtil.writeToTextFile(outputFileName, "</subroutineDec>");
	}

	void CompileClassVarDec() throws IOException {
		String tempKind = null; // static or field
		String tempType = null; // int or String or else

		FilesUtil.writeToTextFile(outputFileName, "<classVarDec>");
		writeTerminal();

		if ((token.currentStr.equals("static"))
				|| (token.currentStr.equals("field"))) {
			tempKind = token.currentStr;
		}
		token.advance();
		tempType = token.currentStr;
		writeTerminal();
		do {

			token.advance();
			if (token.tokenType() == IDENTIFIER) {
				st.Define(token.currentStr, tempType, tempKind);
				// System.out.println(token.currentStr+" " + tempType+" "+
				// tempKind +" " + st.IndexOf(token.currentStr));
			}
			writeTerminal();

		} while (!token.currentStr.equals(";"));
		FilesUtil.writeToTextFile(outputFileName, "</classVarDec>");

	}

	void CompileVarDec() throws IOException {
		String tempKind = "var";
		String tempType = null; // int or String or else
		FilesUtil.writeToTextFile(outputFileName, "<varDec>");
		writeTerminal();
		token.advance();
		tempType = token.currentStr;
		writeTerminal();
		do {
			token.advance();
			if (token.tokenType() == IDENTIFIER) {
				st.Define(token.currentStr, tempType, tempKind);
				// System.out.println(token.currentStr+" " + tempType+" "+
				// tempKind +" " + st.IndexOf(token.currentStr));
			}
			writeTerminal();

		} while (!token.currentStr.equals(";"));
		FilesUtil.writeToTextFile(outputFileName, "</varDec>");

	}

	void CompileParameterList() throws IOException {
		String tempKind = "argument";
		String tempType = null; // int or String or else
		FilesUtil.writeToTextFile(outputFileName, "<parameterList>");

		while (!token.currentStr.equals(")")) {

			do {
				writeTerminal();
				if (token.currentStr.equals(",")) {
					token.advance();
				}
				if (!token.currentStr.equals(",")) {
					tempType = token.currentStr;
					token.advance();

					writeTerminal();
					st.Define(token.currentStr, tempType, tempKind);
					// System.out.println(token.currentStr+" " + tempType+" "+
					// tempKind +" " + st.IndexOf(token.currentStr));}
					token.advance();

				}
			} while (token.currentStr.equals(","));
		}
		FilesUtil.writeToTextFile(outputFileName, "</parameterList>");
	}

	void CompileStatements() throws IOException {
		FilesUtil.writeToTextFile(outputFileName, "<statements>");
		do {
			if (token.tokenType() == KEYWORD) {
				if (token.keyWord().equals("do")) {
					CompileDo();

				} else if (token.keyWord().equals("let")) {
					CompileLet();

				} else if (token.keyWord().equals("while")) {
					CompileWhile();

				}

				else if (token.keyWord().equals("if")) {
					CompileIf();

				}
				// else if (token.keyWord().equals("else")){
				// CompileElse();
				//
				// }
				else if (token.keyWord().equals("return")) {
					CompileReturn();

				}

			}
		} while (!token.currentStr.equals("}"));

		FilesUtil.writeToTextFile(outputFileName, "</statements>");
	}

	void CompileDo() throws IOException {
		FilesUtil.writeToTextFile(outputFileName, "<doStatement>");
		String functionCalled = "";
		writeTerminal();
		nArgs = 0;

		do {
			token.advance();
			writeTerminal();

			String id = token.currentStr;
			String segment;
			functionCalled = functionCalled + st.TypeOf(token.currentStr);

			switch (st.KindOf(id)) {
			case "static":
				segment = "STATIC";

				break;
			case "field":
				segment = "THIS";

				break;
			case "var":
				segment = "LOCAL";

				break;
			case "argument":
				segment = "ARG";

				break;
			default:
				segment = "NONE";
				break;
			}
			token.advance();
			writeTerminal();
			if (!token.currentStr.equals(".")) {
				vm.WritePush("POINTER", 0);
				nArgs = 1;

				functionCalled = className + "." + id;
			} else if (segment.equals("NONE")) {
				functionCalled = id + token.currentStr;
				token.advance();
				functionCalled = functionCalled + token.currentStr;
				token.advance();
				nArgs = 0;

			} else {
				vm.WritePush(segment, st.IndexOf(id));

				functionCalled = functionCalled + token.currentStr;
				token.advance();
				functionCalled = functionCalled + token.currentStr;
				token.advance();
				nArgs = 1;
			}

			if (token.currentStr.equals("(")) {
				token.advance();

				CompileExpressionList();
				nArgs = nArgs + nArgsAdd;
				writeTerminal();
				token.advance(); // ;
				writeTerminal(); // write ;
			} // missing something
				// ????????????????????????????????????????????????????
		} while (!token.currentStr.equals(";"));
		System.out.println(functionCalled);
		vm.WriteCall(functionCalled, nArgs);

		vm.WritePop("TEMP", 0);
		token.advance();
		FilesUtil.writeToTextFile(outputFileName, "</doStatement>");
	}

	void CompileLet() throws IOException {
		String destination;
		String segment;
		String arrayName;
		boolean arrayCalled = false;
		FilesUtil.writeToTextFile(outputFileName, "<letStatement>");
		writeTerminal();

		token.advance();
		writeTerminal();
		destination = token.currentStr;
		arrayName = token.currentStr;

		token.advance();
		writeTerminal();

		if (token.currentStr.equals("[")) {
			arrayCalled = true;
			switch (st.KindOf(arrayName)) {
			case "static":
				segment = "STATIC";

				break;
			case "field":
				segment = "THIS";

				break;
			case "var":
				segment = "LOCAL";

				break;
			case "argument":
				segment = "ARG";

				break;
			default:
				segment = "NONE";
				break;
			}

			token.advance();
			CompileExpression();
			vm.WritePush(segment, st.IndexOf(arrayName));
			vm.WriteArithmetic("ADD");
			writeTerminal();
			token.advance();
			writeTerminal();
		}

		token.advance();
		CompileExpression(); // before a new method is called, advance() already
								// been called!!!!!
		writeTerminal();
		switch (st.KindOf(destination)) {
		case "static":
			segment = "STATIC";
			break;
		case "field":
			segment = "THIS";
			break;
		case "var":
			segment = "LOCAL";
			break;
		case "argument":
			segment = "ARG";
			break;
		default:
			segment = "NONE";
			break;
		}
		if (arrayCalled == false) {
			vm.WritePop(segment, st.IndexOf(destination));
		} else {
			vm.WritePop("TEMP", 0);
			vm.WritePop("POINTER", 1);
			vm.WritePush("TEMP", 0);
			vm.WritePop("THAT", 0);
		}
		token.advance();
		FilesUtil.writeToTextFile(outputFileName, "</letStatement>");
	}

	void CompileWhile() throws IOException {
		int whileIndex = whileIndexGlobal + 1;

		FilesUtil.writeToTextFile(outputFileName, "<whileStatement>");
		writeTerminal();
		vm.WriteLabel("WHILE_EXP" + whileIndex);

		do {
			token.advance();
			writeTerminal();
			if (token.currentStr.equals("(")) {
				token.advance();
				CompileExpression();
				vm.WriteArithmetic("NOT");
				vm.WriteIf("WHILE_END" + whileIndex);

				writeTerminal();
				token.advance();
				writeTerminal(); // write {
			}
		} while (!token.currentStr.equals("{"));
		token.advance();
		whileIndexGlobal++;
		CompileStatements();
		writeTerminal();
		token.advance();
		vm.WriteGoto("WHILE_EXP" + whileIndex);
		vm.WriteLabel("WHILE_END" + whileIndex);

		FilesUtil.writeToTextFile(outputFileName, "</whileStatement>");
	}

	void CompileIf() throws IOException {
		int ifIndex = ifIndexGlobal++;
		boolean elseExist = false;
		FilesUtil.writeToTextFile(outputFileName, "<ifStatement>");
		writeTerminal();
		do {
			token.advance();
			writeTerminal();
			if (token.currentStr.equals("(")) {
				token.advance();
				CompileExpression();
				vm.WriteIf("IF_TRUE" + ifIndex);
				vm.WriteGoto("IF_FALSE" + ifIndex);
				// goto IF_FALSE0
				// label IF_TRUE0
				writeTerminal();
				token.advance();
				writeTerminal(); // write {
			}
		} while (!token.currentStr.equals("{"));

		vm.WriteLabel("IF_TRUE" + ifIndex);

		token.advance();
		CompileStatements();
		writeTerminal();
		token.advance();
		if (token.currentStr.equals("else")) {
			// else??????????????????????
			elseExist = true;
			vm.WriteGoto("IF_END" + ifIndex);
			vm.WriteLabel("IF_FALSE" + ifIndex);

			CompileElse();
			vm.WriteLabel("IF_END" + ifIndex);
		} else {
			vm.WriteLabel("IF_FALSE" + ifIndex);
		}

		FilesUtil.writeToTextFile(outputFileName, "</ifStatement>");
	}

	void CompileElse() throws IOException {
		boolean elseExist = false;
		FilesUtil.writeToTextFile(outputFileName, "<elseStatement>");
		writeTerminal();

		token.advance();
		writeTerminal();

		token.advance();
		CompileStatements();
		writeTerminal();
		token.advance();

		FilesUtil.writeToTextFile(outputFileName, "</elseStatement>");
	}

	void CompileReturn() throws IOException {
		FilesUtil.writeToTextFile(outputFileName, "<returnStatement>");
		writeTerminal();
		token.advance();
		if (!token.currentStr.equals(";")) {
			CompileExpression();
			vm.WriteReturn();
		} else {
			vm.WritePush("CONST", 0);
			vm.WriteReturn();
		}

		writeTerminal();
		token.advance();
		FilesUtil.writeToTextFile(outputFileName, "</returnStatement>");
	}

	void CompileExpressionList() throws IOException {
		int nArgsAddLocal = 0;

		FilesUtil.writeToTextFile(outputFileName, "<expressionList>");

		while (!token.currentStr.equals(")")) {
			CompileExpression();
			nArgsAddLocal++;
			if (token.currentStr.equals(",")) {
				writeTerminal();
				token.advance();
			}
		}
		nArgsAdd = nArgsAddLocal;
		FilesUtil.writeToTextFile(outputFileName, "</expressionList>");
	}

	void CompileExpression() throws IOException {
		String op = "(\\+|\\-|\\*|\\/|\\&|\\||\\<|\\>|\\=)";
		String currentOperator = null;
		FilesUtil.writeToTextFile(outputFileName, "<expression>");
		// if (token.currentStr.equals("-")) {
		// currentOperator = "neg";
		//
		// token.advance();
		// CompileTerm();
		// vm.WriteArithmetic("NEG");
		// }
		CompileTerm();
		do {

			if (token.currentStr.matches(op)) {
				writeTerminal();
				currentOperator = token.currentStr;

				token.advance();
				CompileTerm();

				if (currentOperator.matches(op)) {
					switch (currentOperator) {
					case "+":
						vm.WriteArithmetic("ADD");
						break;
					case "-":
						vm.WriteArithmetic("SUB");
						break;
					case "*":
						vm.WriteCall("Math.multiply", 2);
						break;
					case "/":
						vm.WriteCall("Math.divide", 2);
						break;
					case "&":
						vm.WriteArithmetic("AND");
						break;
					case "|":
						vm.WriteArithmetic("OR");
						break;
					case "<":
						vm.WriteArithmetic("LT");
						break;
					case ">":
						vm.WriteArithmetic("GT");
						break;
					case "=":
						vm.WriteArithmetic("EQ");
						break;
					// case "neg": vm.WriteArithmetic("NEG");
					// break;
					default:
						break;
					}
				}
			}

		} while ((!token.currentStr.equals(";"))
				&& (!token.currentStr.equals(","))
				&& (!token.currentStr.equals(")"))
				&& (!token.currentStr.equals("]")));

		FilesUtil.writeToTextFile(outputFileName, "</expression>");

	}

	void CompileTerm() throws IOException {
		int nArgsMethod;
		FilesUtil.writeToTextFile(outputFileName, "<term>");
		String keywordString = "(class|constructor|function|method|field|static|var|int|char|boolean|void|true|false|null|this|let|do|if|else|while|return)";
		String op = "(\\+|\\-|\\*|\\/|\\&|\\||\\<|\\>|\\=)";
		String methodName = null;
		if (token.currentStr.equals("-")) {
			writeTerminal();
			token.advance();
			CompileTerm();
			vm.WriteArithmetic("NEG");
		} else if (token.currentStr.equals("(")) {
			writeTerminal();
			token.advance();
			CompileExpression();
			writeTerminal();
			token.advance();
		} else if (token.currentStr.equals("~")) {
			writeTerminal();
			token.advance();
			CompileTerm();
			vm.WriteArithmetic("NOT");
		}

		else {

			do {

				writeTerminal();
				if (token.tokenType() == INT_CONST) {
					vm.WritePush("CONST", Integer.parseInt(token.currentStr));
					token.advance();
				} else if (token.tokenType() == IDENTIFIER) {
					String segment = null;
					String id = token.currentStr;
					switch (st.KindOf(id)) {
					case "static":
						segment = "STATIC";

						break;
					case "field":
						segment = "THIS";

						break;
					case "var":
						segment = "LOCAL";

						break;
					case "argument":
						segment = "ARG";

						break;
					default:
						segment = "NONE";
						break;
					}
					token.advance();
					writeTerminal();
					if (!token.currentStr.equals(".")) {

						if (token.currentStr.equals("[")) {
							writeTerminal();
							token.advance();
							CompileExpression();
							vm.WritePush(segment, st.IndexOf(id));
							vm.WriteArithmetic("ADD");
							vm.WritePop("POINTER", 1);
							vm.WritePush("THAT", 0);
							writeTerminal();
							token.advance();

						} else {
							if (segment.equals("NONE")) {
								vm.WritePush("POINTER", 0);
								nArgsMethod = 1;
								token.advance();
								CompileExpressionList();
								nArgsMethod = nArgsMethod + nArgsAdd;
								vm.WriteCall(className + "." + id, nArgsMethod);
								writeTerminal();
								token.advance();
							} else {
								vm.WritePush(segment, st.IndexOf(id));
							}

						}

					}

					else {
						nArgsMethod = 0;

						methodName = null;
						if (segment.equals("NONE")) {

							methodName = id;
							methodName = methodName + token.currentStr;
						} else {
							methodName = st.TypeOf(id);
							methodName = methodName + token.currentStr;
							nArgsMethod++;
						}

						token.advance();
						writeTerminal();
						methodName = methodName + token.currentStr;
						System.out.println(methodName);
						token.advance();
						if (token.currentStr.equals("(")) {
							writeTerminal();

							if (methodName != null) {
								if (segment != "NONE") {
									vm.WritePush(segment, st.IndexOf(id));
									token.advance();
									CompileExpressionList();
									nArgsMethod = nArgsMethod + nArgsAdd;
									vm.WriteCall(methodName, nArgsMethod);

								} else {
									token.advance();
									CompileExpressionList();
									nArgsMethod = nArgsMethod + nArgsAdd;
									vm.WriteCall(methodName, nArgsMethod);

								}
							}
							writeTerminal();
							token.advance();
						}
					}
				}

				else if (token.tokenType() == STRING_CONST) {
					String st = token.currentStr;
					String mod = st.substring(1, st.length() - 1);
					System.out.println(mod.length());
					vm.WritePush("CONST", mod.length());
					vm.WriteCall("String.new", 1);
					for (int i = 0; i < mod.length(); i++) {
						char character = mod.charAt(i);
						int ascii = (int) character;
						vm.WritePush("CONST", ascii);
						vm.WriteCall("String.appendChar", 2);

					}
					token.advance();
				} else if (token.tokenType() == KEYWORD) {
					if (token.currentStr.equals("true")) {
						vm.WritePush("CONST", 0);
						vm.WriteArithmetic("NOT");
					} else if (token.currentStr.equals("false")) {
						vm.WritePush("CONST", 0);

					} else if (token.currentStr.equals("this")) {
						vm.WritePush("POINTER", 0);

					} else if (token.currentStr.equals("null")) {
						vm.WritePush("CONST", 0);

					}
					token.advance();

				}

			} while (!token.currentStr.matches(op)
					&& (!token.currentStr.equals(";"))
					&& (!token.currentStr.equals(")"))
					&& (!token.currentStr.equals("]"))
					&& (!token.currentStr.equals(",")));

		}
		FilesUtil.writeToTextFile(outputFileName, "</term>");
	}

	void writeTerminal() throws IOException {
		String currentOutput;
		if (token.tokenType() == KEYWORD) {

			currentOutput = "<keyword> " + token.keyWord() + " </keyword>";
			FilesUtil.writeToTextFile(outputFileName, currentOutput);
		}

		if (token.tokenType() == SYMBOL) {

			currentOutput = "<symbol> " + token.symbol() + " </symbol>";
			FilesUtil.writeToTextFile(outputFileName, currentOutput);
		}

		if (token.tokenType() == IDENTIFIER) {

			currentOutput = "<identifier> " + token.identifier()
					+ " </identifier>";
			FilesUtil.writeToTextFile(outputFileName, currentOutput);
		}
		if (token.tokenType() == INT_CONST) {

			currentOutput = "<integerConstant> " + token.intVal()
					+ " </integerConstant>";
			FilesUtil.writeToTextFile(outputFileName, currentOutput);
		}
		if (token.tokenType() == STRING_CONST) {

			currentOutput = "<stringConstant> " + token.stringVal()
					+ " </stringConstant>";
			FilesUtil.writeToTextFile(outputFileName, currentOutput);
		}
	}

	public static void main(String[] args) {
		try {

			CompilationEngine ce = new CompilationEngine(args[0]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
