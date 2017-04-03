
// CODE Module
public class Code {

	// return the binary code of the dest mnemonic

	static String dest(String destInput) {
		StringBuilder binaryOut = new StringBuilder("000");
		if (destInput != null) {

			if (destInput.contains("A")) {
				binaryOut.setCharAt(0, '1');
			}
			if (destInput.contains("D")) {
				binaryOut.setCharAt(1, '1');
			}
			if (destInput.contains("M")) {
				binaryOut.setCharAt(2, '1');
			}
			return binaryOut.toString();
		} else
			return binaryOut.toString();
	}

	// return the binary code of the comp mnemonic

	static String comp(String compInput) {
		StringBuilder binaryOut;
		if (compInput != null) {
			switch (compInput) {
			case "0":
				binaryOut = new StringBuilder("0101010");
				break;
			case "1":
				binaryOut = new StringBuilder("0111111");
				break;
			case "-1":
				binaryOut = new StringBuilder("0111010");
				break;
			case "D":
				binaryOut = new StringBuilder("0001100");
				break;
			case "-D":
				binaryOut = new StringBuilder("0001111");
				break;
			case "D+1":
				binaryOut = new StringBuilder("0011111");
				break;
			case "D-1":
				binaryOut = new StringBuilder("0001110");
				break;
			case "A":
				binaryOut = new StringBuilder("0110000");
				break;
			case "!A":
				binaryOut = new StringBuilder("0110001");
				break;
			case "-A":
				binaryOut = new StringBuilder("0110011");
				break;
			case "A+1":
				binaryOut = new StringBuilder("0110111");
				break;
			case "A-1":
				binaryOut = new StringBuilder("0110010");
				break;
			case "D+A":
				binaryOut = new StringBuilder("0000010");
				break;
			case "D-A":
				binaryOut = new StringBuilder("0010011");
				break;
			case "A-D":
				binaryOut = new StringBuilder("0000111");
				break;
			case "D&A":
				binaryOut = new StringBuilder("0000000");
				break;
			case "D|A":
				binaryOut = new StringBuilder("0010101");
				break;
			case "M":
				binaryOut = new StringBuilder("1110000");
				break;
			case "!M":
				binaryOut = new StringBuilder("1110001");
				break;
			case "-M":
				binaryOut = new StringBuilder("1110011");
				break;
			case "M+1":
				binaryOut = new StringBuilder("1110111");
				break;
			case "M-1":
				binaryOut = new StringBuilder("1110010");
				break;
			case "D+M":
				binaryOut = new StringBuilder("1000010");
				break;
			case "D-M":
				binaryOut = new StringBuilder("1010011");
				break;
			case "M-D":
				binaryOut = new StringBuilder("1000111");
				break;
			case "D&M":
				binaryOut = new StringBuilder("1000000");
				break;
			case "D|M":
				binaryOut = new StringBuilder("1010101");
				break;
			default:
				binaryOut = new StringBuilder("invalid command");
				break;
			}
			return binaryOut.toString();
		} else
			return null;
	}

	// return the binary code of the jump mnemonic

	static String jump(String jumpInput) {
		StringBuilder binaryOut;
		if (jumpInput != null) {
			switch (jumpInput) {
			case "JGT":
				binaryOut = new StringBuilder("001");
				break;
			case "JEQ":
				binaryOut = new StringBuilder("010");
				break;
			case "JGE":
				binaryOut = new StringBuilder("011");
				break;
			case "JLT":
				binaryOut = new StringBuilder("100");
				break;
			case "JNE":
				binaryOut = new StringBuilder("101");
				break;
			case "JLE":
				binaryOut = new StringBuilder("110");
				break;
			case "JMP":
				binaryOut = new StringBuilder("111");
				break;
			default:
				binaryOut = new StringBuilder("invalid command");
				break;
			}
			return binaryOut.toString();

		} else {
			binaryOut = new StringBuilder("000");
			return binaryOut.toString();
		}
	}

}
