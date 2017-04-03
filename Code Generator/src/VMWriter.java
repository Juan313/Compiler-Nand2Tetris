import java.io.File;
import java.io.IOException;


public class VMWriter {
	String outputFile;

	public VMWriter(String outputFileName) throws IOException {
		outputFile = outputFileName;
		File file = new File(outputFileName);
		if (file.exists())
			file.delete();
		
	}

	void WritePush(String Segment, int Index) throws IOException {
		String ModSegment;

		switch (Segment) {
		case "CONST":
			ModSegment = "constant";
			break;
		case "ARG":
			ModSegment = "argument";
			break;
		case "LOCAL":
			ModSegment = "local";
			break;
		case "STATIC":
			ModSegment = "static";
			break;
		case "THIS":
			ModSegment = "this";
			break;
		case "THAT":
			ModSegment = "that";
			break;
		case "POINTER":
			ModSegment = "pointer";
			break;
		case "TEMP":
			ModSegment = "temp";
			break;
		default:
			ModSegment = null;
			break;
		}
		StringBuilder Content = new StringBuilder();
		Content.append("push " + ModSegment + " "+ Index);
		FilesUtil.writeToTextFile(outputFile, Content.toString());
	}

	void WritePop(String Segment, int Index) throws IOException {
		String ModSegment;

		switch (Segment) {
		case "CONST":
			ModSegment = "constant";
			break;
		case "ARG":
			ModSegment = "argument";
			break;
		case "LOCAL":
			ModSegment = "local";
			break;
		case "STATIC":
			ModSegment = "static";
			break;
		case "THIS":
			ModSegment = "this";
			break;
		case "THAT":
			ModSegment = "that";
			break;
		case "POINTER":
			ModSegment = "pointer";
			break;
		case "TEMP":
			ModSegment = "temp";
			break;
		default:
			ModSegment = null;
			break;
		}
		StringBuilder Content = new StringBuilder();
		Content.append("pop " + ModSegment + " " + Index);
		FilesUtil.writeToTextFile(outputFile, Content.toString());
	}

	void WriteArithmetic(String Command) throws IOException {
		String keywordString = "(ADD|SUB|NEG|EQ|GT|LT|AND|OR|NOT)";
		if (Command.matches(keywordString)) {
			StringBuilder Content = new StringBuilder();
			Content.append(Command.toLowerCase());
			FilesUtil.writeToTextFile(outputFile, Content.toString());
		}
	}

	void WriteLabel(String label) throws IOException {

		StringBuilder Content = new StringBuilder();
		Content.append("label " + label);
		FilesUtil.writeToTextFile(outputFile, Content.toString());

	}

	void WriteGoto(String label) throws IOException {

		StringBuilder Content = new StringBuilder();
		Content.append("goto " + label);
		FilesUtil.writeToTextFile(outputFile, Content.toString());
	}

	void WriteIf(String label) throws IOException {

		StringBuilder Content = new StringBuilder();
		Content.append("if-goto " + label);
		FilesUtil.writeToTextFile(outputFile, Content.toString());
	}

	void WriteCall(String name, int nArgs) throws IOException {

		StringBuilder Content = new StringBuilder();
		Content.append("call " + name + " "+ nArgs);
		FilesUtil.writeToTextFile(outputFile, Content.toString());
	}

	void WriteFunction(String name, int nLocals) throws IOException {

		StringBuilder Content = new StringBuilder();
		Content.append("function " + name + " "+ nLocals);
		FilesUtil.writeToTextFile(outputFile, Content.toString());
	}

	void WriteReturn() throws IOException {

		StringBuilder Content = new StringBuilder();
		Content.append("return");
		FilesUtil.writeToTextFile(outputFile, Content.toString());
	}


}
	
	

